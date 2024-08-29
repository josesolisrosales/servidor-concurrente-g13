package fidness.Server.Utility;

import fidness.Objects.User;
import java.util.List;

public class UserManager {
    private static UserManager instance;

    private UserManager() {}

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public boolean authenticateUser(String username, String password) {
        User user = getUserByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    public boolean registerUser(String username, String name, String lastName, String password, String email, boolean isAdmin) {
        if (getUserByUsername(username) != null) {
            return false; // Usuario ya existe
        }
        User user = new User(username, name, lastName, password, email, isAdmin);
        return DerbyManager.getInstance().insertUser(user);
    }

    public boolean isUsernameAdmin(String username) {
        User user = getUserByUsername(username);
        return user != null && user.isAdmin();
    }

    public boolean updateUser(String username, String name, String lastName, String email) {
        User user = getUserByUsername(username);
        if (user == null) {
            return false;
        }
        user.setName(name);
        user.setLastName(lastName);
        user.setEmail(email);
        return DerbyManager.getInstance().updateUser(user);
    }

    public List<User> getAllUsers() {
        return DerbyManager.getInstance().selectFromUsers();
    }

    public User getUserByUsername(String username) {
        List<User> users = DerbyManager.getInstance().selectFromUsers("username = '" + username + "'");
        return users.isEmpty() ? null : users.get(0);
    }

    public boolean deleteUser(String username) {
        return DerbyManager.getInstance().deleteUser(username);
    }

    // Método para cambiar la contraseña de un usuario
    public boolean changeUserPassword(String username, String newPassword) {
        User user = getUserByUsername(username);
        if (user == null) {
            return false;
        }
        user.setPassword(newPassword);
        return DerbyManager.getInstance().updateUser(user);
    }

    // Método para cambiar el estado de administrador de un usuario
    public boolean changeUserAdminStatus(String username, boolean isAdmin) {
        User user = getUserByUsername(username);
        if (user == null) {
            return false;
        }
        user.setAdmin(isAdmin);
        return DerbyManager.getInstance().updateUser(user);
    }
}