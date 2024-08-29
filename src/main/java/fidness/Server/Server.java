package fidness.Server;

import fidness.Objects.Exercise;
import fidness.Objects.User;
import fidness.Server.Utility.ClientHandler;
import fidness.Server.Utility.DerbyManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

@SuppressWarnings("ALL")
public class Server {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            initializeDatabaseIfEmpty();


            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Crear un nuevo hilo para manejar la conexión del cliente
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializeDatabaseIfEmpty() {
        List<Exercise> exercises = DerbyManager.getInstance().selectFromExercises();

        if (exercises.isEmpty()) {
            DerbyManager.getInstance().insertExercise(new Exercise("Push-ups", "Standard push-ups", "", "Chest"));
            DerbyManager.getInstance().insertExercise(new Exercise("Squats", "Bodyweight squats", "", "Legs"));
            DerbyManager.getInstance().insertExercise(new Exercise("Pull-ups", "Standard pull-ups", "", "Back"));
            DerbyManager.getInstance().insertExercise(new Exercise("Lunges", "Forward lunges", "", "Legs"));
            DerbyManager.getInstance().insertExercise(new Exercise("Plank", "Standard plank hold", "", "Core"));
            DerbyManager.getInstance().insertExercise(new Exercise("Dips", "Tricep dips", "", "Arms"));
            DerbyManager.getInstance().insertExercise(new Exercise("Crunches", "Standard crunches", "", "Core"));
            DerbyManager.getInstance().insertExercise(new Exercise("Jumping Jacks", "Standard jumping jacks", "", "Cardio"));
            DerbyManager.getInstance().insertExercise(new Exercise("Mountain Climbers", "Mountain climbers exercise", "", "Core"));
            DerbyManager.getInstance().insertExercise(new Exercise("Burpees", "Standard burpees", "", "Full Body"));
            DerbyManager.getInstance().insertExercise(new Exercise("Bicycle Crunches", "Bicycle crunches for abs", "", "Core"));
            DerbyManager.getInstance().insertExercise(new Exercise("Leg Raises", "Lying leg raises", "", "Core"));
            DerbyManager.getInstance().insertExercise(new Exercise("Tricep Extensions", "Overhead tricep extensions", "", "Arms"));
            DerbyManager.getInstance().insertExercise(new Exercise("Calf Raises", "Standing calf raises", "", "Legs"));
            DerbyManager.getInstance().insertExercise(new Exercise("Russian Twists", "Seated russian twists", "", "Core"));
        }

        List<User> users = DerbyManager.getInstance().selectFromUsers();

        if (users.isEmpty()) {
            DerbyManager.getInstance().insertUser(new User("admin", "Admin", "Admin", "admin", "admin@local.com", true));
        }

    }
}