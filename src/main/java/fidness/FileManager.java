package fidness;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Clase para manejar la lectura y escritura de archivos para persistir los objetos creados.
public class FileManager {
    private static final String USER_FILE="users.dat";

    // Guarda la lista de usuarios en un archivo
    public static void saveUsers(List<User> users){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream((USER_FILE)))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Carga la lista de usuarios desde un archivo
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(USER_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_FILE))) {
                users = (List<User>) ois.readObject();
            } catch (IOException e) {
                System.err.println("Error cargando los usuarios: " + e.getMessage());
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.err.println("Error: Definicion de claes serializadas cambio. " + e.getMessage());
                e.printStackTrace();
            }
        }
        return users;
    }
}
