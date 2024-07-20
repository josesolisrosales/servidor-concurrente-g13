package fidness;

import java.util.ArrayList;
import java.util.List;

// Clase para manejar la lista de usuarios asi como la creacion, eliminacion y autenticacion de los mismos.
public class UserManager {
    private List<User> users;

    public UserManager() {
        this.users = FileManager.loadUsers();

        if (users.isEmpty()) {
            createUser("admin", "Admin", "User", "admin123", "admin@local.com", true);
        }
    }

    // Crea un nuevo usuario y lo agrega a la lista de usuarios
    public User createUser(String username, String name, String lastName, String password, String email, boolean isAdmin) {
        if (getUserByUsername(username) != null || getUserByEmail(email) != null) {
            throw new IllegalArgumentException("Username or Email already exists");
        }

        User newUser = new User(username, name, lastName, password, email, isAdmin);
        users.add(newUser);
        saveUsers();
        return newUser;
    }

    // Autentica un usuario con el username y password proporcionados
    public User authenticateUser(String username, String password) {
        User user = getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    // Obtiene un usuario por su username
    public User getUserByUsername(String username) {
        return users.stream().filter( u -> u.getUsername().equals(username)).findFirst().orElse(null);
    }

    // Obtiene un usuario por su email
    public User getUserByEmail(String email) {
        return users.stream().filter( u -> u.getEmail().equals(email)).findFirst().orElse(null);
    }

    // Actualiza los datos de un usuario
    public void updateUser(User user, String name, String lastName, String email) {
        User existingUser = getUserByEmail(email);

        if (existingUser != null && !existingUser.getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setName(name);
        user.setLastName(lastName);
        user.setEmail(email);
        saveUsers();
    }

    // Elimina un usuario de la lista
    public void deleteUser(User user) {
        users.remove(user);
        saveUsers();
    }

    // Guarda la lista de usuarios en un archivo
    private void saveUsers() {
        FileManager.saveUsers(users);
    }

    // Obtiene la lista de usuarios
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}
