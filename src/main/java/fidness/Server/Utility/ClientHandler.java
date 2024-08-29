package fidness.Server.Utility;

import fidness.Objects.Exercise;
import fidness.Objects.Routine;
import fidness.Objects.User;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            Object request;
            while ((request = in.readObject()) != null) {
                if (request instanceof String) {
                    handleStringRequest((String) request);
                } else if (request instanceof Object[]) {
                    handleObjectArrayRequest((Object[]) request);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
    }

    private void handleStringRequest(String request) throws IOException {
        String[] parts = request.split("\\|");
        String action = parts[0];
        switch (action) {
            case "AUTHENTICATE":
                handleAuthenticate(parts[1], parts[2]);
                break;
            case "REGISTER":
                handleRegister(parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
                break;
            case "GET_EXERCISES":
                handleGetExercises();
                break;
            case "GET_EXERCISES_BY_MUSCLE_GROUP":
                handleGetExercisesByMuscleGroup(parts[1]);
                break;
            case "GET_ROUTINES":
                handleGetRoutines(parts[1]);
                break;
            case "CREATE_EXERCISE":
                handleCreateExercise(parts[1], parts[2], parts[3], parts[4]);
                break;
            case "DELETE_EXERCISE":
                handleDeleteExercise(parts[1]);
                break;
            case "GET_ALL_USERS":
                handleGetAllUsers();
                break;
            case "CREATE_USER":
                handleCreateUser(parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
                break;
            case "UPDATE_USER":
                handleUpdateUser(parts[1], parts[2], parts[3], parts[4]);
                break;
            case "DELETE_USER":
                handleDeleteUser(parts[1]);
                break;
            case "GET_USER_BY_USERNAME":
                handleGetUserByUsername(parts[1]);
                break;
            default:
                sendResponse("ERROR: Unknown action");
                break;
        }
    }

    private void handleObjectArrayRequest(Object[] requestArray) throws IOException {
        String action = (String) requestArray[0];
        switch (action) {
            case "INSERT_ROUTINE":
                handleInsertRoutine((Routine) requestArray[1], (String) requestArray[2]);
                break;
            case "ADD_ROUTINE":
                handleAddRoutine((Routine) requestArray[1], (String) requestArray[2]);
                break;
            case "REMOVE_ROUTINE":
                handleRemoveRoutine((Routine) requestArray[1], (String) requestArray[2]);
                break;
            case "UPDATE_ROUTINE":
                handleUpdateRoutine((Routine) requestArray[1], (String) requestArray[2]);
                break;
            default:
                sendResponse("ERROR: Unknown action");
                break;
        }
    }

    private void handleAuthenticate(String username, String password) throws IOException {
        boolean isAdmin = UserManager.getInstance().isUsernameAdmin(username);
        boolean isAuthenticated = UserManager.getInstance().authenticateUser(username, password);
        sendResponse(isAuthenticated ? "SUCCESS|" + (isAdmin ? "1" : "0") : "ERROR: Authentication failed");
    }

    private void handleRegister(String username, String name, String lastName, String password, String email, String isAdmin) throws IOException {
        boolean isAdminBool = isAdmin.equals("1");
        boolean isRegistered = UserManager.getInstance().registerUser(username, name, lastName, password, email, isAdminBool);
        sendResponse(isRegistered ? "SUCCESS" : "ERROR: Username already exists");
    }

    private void handleGetExercises() throws IOException {
        List<Exercise> exercises = ExerciseManager.getInstance().getAllExercises();
        sendResponse(exercises);
    }

    private void handleGetExercisesByMuscleGroup(String muscleGroup) throws IOException {
        List<Exercise> exercises = ExerciseManager.getInstance().getExercisesByMuscleGroup(muscleGroup);
        sendResponse(exercises);
    }

    private void handleGetRoutines(String username) throws IOException {
        List<Routine> routines = RoutineManager.getInstance().getRoutines(username);
        sendResponse(routines);
    }

    private void handleInsertRoutine(Routine routine, String username) throws IOException {
        boolean success = RoutineManager.getInstance().insertRoutine(routine, username);
        sendResponse(success ? "SUCCESS" : "ERROR: Could not insert routine");
    }

    private void handleAddRoutine(Routine routine, String username) throws IOException {
        boolean success = RoutineManager.getInstance().addRoutine(routine, username);
        sendResponse(success ? "SUCCESS" : "ERROR: Could not add routine");
    }

    private void handleRemoveRoutine(Routine routine, String username) throws IOException {
        boolean success = RoutineManager.getInstance().removeRoutine(routine, username);
        sendResponse(success ? "SUCCESS" : "ERROR: Could not remove routine");
    }

    private void handleUpdateRoutine(Routine routine, String username) throws IOException {
        boolean success = RoutineManager.getInstance().updateRoutine(routine, username);
        sendResponse(success ? "SUCCESS" : "ERROR: Could not update routine");
    }

    private void handleCreateExercise(String name, String muscleGroup, String description, String videoUrl) throws IOException {
        Exercise exercise = new Exercise(name, description, videoUrl, muscleGroup);
        boolean success = ExerciseManager.getInstance().addExercise(exercise);
        sendResponse(success ? "SUCCESS" : "ERROR: Could not create exercise");
    }

    private void handleDeleteExercise(String name) throws IOException {
        boolean success = ExerciseManager.getInstance().deleteExercise(name);
        sendResponse(success ? "SUCCESS" : "ERROR: Could not delete exercise");
    }

    private void handleGetAllUsers() throws IOException {
        List<User> users = UserManager.getInstance().getAllUsers();
        sendResponse(users);
    }

    private void handleCreateUser(String username, String name, String lastName, String password, String email, String isAdmin) throws IOException {
        boolean isAdminBool = isAdmin.equals("1");
        boolean success = UserManager.getInstance().registerUser(username, name, lastName, password, email, isAdminBool);
        sendResponse(success ? "SUCCESS" : "ERROR: Could not create user");
    }

    private void handleUpdateUser(String username, String name, String lastName, String email) throws IOException {
        boolean success = UserManager.getInstance().updateUser(username, name, lastName, email);
        sendResponse(success ? "SUCCESS" : "ERROR: Could not update user");
    }

    private void handleDeleteUser(String username) throws IOException {
        boolean success = UserManager.getInstance().deleteUser(username);
        sendResponse(success ? "SUCCESS" : "ERROR: Could not delete user");
    }

    private void handleGetUserByUsername(String username) throws IOException {
        User user = UserManager.getInstance().getUserByUsername(username);
        sendResponse(user != null ? user : "ERROR: User not found");
    }

    private void sendResponse(Object response) throws IOException {
        out.writeObject(response);
        out.flush();
    }

    private void closeConnections() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}