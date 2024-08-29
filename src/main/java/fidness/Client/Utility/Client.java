package fidness.Client.Utility;

import fidness.Objects.Routine;
import fidness.Objects.Exercise;
import fidness.Objects.User;

import java.util.List;

public class Client {
    private ClientService clientService;
    private User currentUser;

    private static Client instance;

    private Client() {
        clientService = ClientService.getInstance();
    }

    public static synchronized Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    public boolean authenticateUser(String username, String password) {
        String request = "AUTHENTICATE|" + username + "|" + password;
        clientService.sendRequest(request);
        String response = (String) clientService.receiveResponse();

        if (response != null && response.startsWith("SUCCESS")) {
            currentUser = new User(username, response.endsWith("1"));
            return true;
        }

        return false;
    }

    public boolean registerUser(String username, String name, String lastName, String password, String email, boolean isAdmin) {
        String isAdminString = isAdmin ? "1" : "0";
        String request = "REGISTER|" + username + "|" + name + "|" + lastName + "|" + password + "|" + email + "|" + isAdminString;
        clientService.sendRequest(request);
        String response = (String) clientService.receiveResponse();

        return response != null && response.equals("SUCCESS");
    }

    public List<Exercise> getExercises() {
        String request = "GET_EXERCISES";
        clientService.sendRequest(request);
        return (List<Exercise>) clientService.receiveResponse();
    }

    public List<Exercise> getExercisesByMuscleGroup(String muscleGroup) {
        String request = "GET_EXERCISES_BY_MUSCLE_GROUP|" + muscleGroup;
        clientService.sendRequest(request);
        return (List<Exercise>) clientService.receiveResponse();
    }

    public boolean createExercise(String name, String muscleGroup, String description, String videoUrl) {
        String request = "CREATE_EXERCISE|" + name + "|" + muscleGroup + "|" + description + "|" + videoUrl;
        clientService.sendRequest(request);
        String response = (String) clientService.receiveResponse();
        return response != null && response.equals("SUCCESS");
    }

    public boolean deleteExercise(String name) {
        String request = "DELETE_EXERCISE|" + name;
        clientService.sendRequest(request);
        String response = (String) clientService.receiveResponse();
        return response != null && response.equals("SUCCESS");
    }

    public boolean insertRoutine(Routine routine) {
        String request = "INSERT_ROUTINE";
        clientService.sendRequest(new Object[]{request, routine, currentUser.getUsername()});
        String response = (String) clientService.receiveResponse();
        return response != null && response.equals("SUCCESS");
    }

    public List<Routine> getRoutines(String username) {
        String request = "GET_ROUTINES|" + username;
        clientService.sendRequest(request);
        return (List<Routine>) clientService.receiveResponse();
    }

    public boolean addRoutine(Routine routine, String username) {
        String request = "ADD_ROUTINE";
        clientService.sendRequest(new Object[]{request, routine, username});
        String response = (String) clientService.receiveResponse();
        return response != null && response.equals("SUCCESS");
    }

    public boolean removeRoutine(Routine routine, String username) {
        String request = "REMOVE_ROUTINE";
        clientService.sendRequest(new Object[]{request, routine, username});
        String response = (String) clientService.receiveResponse();
        return response != null && response.equals("SUCCESS");
    }

    public List<User> getAllUsers() {
        String request = "GET_ALL_USERS";
        clientService.sendRequest(request);
        return (List<User>) clientService.receiveResponse();
    }

    public boolean createUser(String username, String name, String lastName, String password, String email, boolean isAdmin) {
        String isAdminString = isAdmin ? "1" : "0";
        String request = "CREATE_USER|" + username + "|" + name + "|" + lastName + "|" + password + "|" + email + "|" + isAdminString;
        clientService.sendRequest(request);
        String response = (String) clientService.receiveResponse();
        return response != null && response.equals("SUCCESS");
    }

    public boolean updateUser(String username, String name, String lastName, String email) {
        String request = "UPDATE_USER|" + username + "|" + name + "|" + lastName + "|" + email;
        clientService.sendRequest(request);
        String response = (String) clientService.receiveResponse();
        return response != null && response.equals("SUCCESS");
    }

    public boolean deleteUser(String username) {
        String request = "DELETE_USER|" + username;
        clientService.sendRequest(request);
        String response = (String) clientService.receiveResponse();
        return response != null && response.equals("SUCCESS");
    }

    public User getUserByUsername(String username) {
        String request = "GET_USER_BY_USERNAME|" + username;
        clientService.sendRequest(request);
        return (User) clientService.receiveResponse();
    }

    public void close() {
        clientService.close();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public boolean changeUserPassword(String username, String currentPassword, String newPassword) {
        String request = "CHANGE_PASSWORD|" + username + "|" + currentPassword + "|" + newPassword;
        clientService.sendRequest(request);
        String response = (String) clientService.receiveResponse();
        return response != null && response.equals("SUCCESS");
    }

    public boolean updateExercise(Exercise exercise) {
        String request = "UPDATE_EXERCISE|" + exercise.getName() + "|" + exercise.getDescription() + "|" + exercise.getVideoUrl() + "|" + exercise.getMuscleGroup();
        clientService.sendRequest(request);
        String response = (String) clientService.receiveResponse();
        return response != null && response.equals("SUCCESS");
    }
    public boolean updateRoutine(Routine routine) {
        String request = "UPDATE_ROUTINE";
        clientService.sendRequest(new Object[]{request, routine, getCurrentUser().getUsername()});
        String response = (String) clientService.receiveResponse();
        return response != null && response.equals("SUCCESS");
    }

    public boolean changeUserAdminStatus(String username, boolean isAdmin) {
        String isAdminString = isAdmin ? "1" : "0";
        String request = "CHANGE_ADMIN_STATUS|" + username + "|" + isAdminString;
        clientService.sendRequest(request);
        String response = (String) clientService.receiveResponse();
        return response != null && response.equals("SUCCESS");
    }
}