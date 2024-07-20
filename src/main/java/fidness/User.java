package fidness;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    // Serializamos la clase para poder guardarla en un archivo
    private static final long serialVersionUID = 1L;

    private String username;
    private String name;
    private String lastName;
    private String password;
    private String email;
    private ArrayList<Routine> routines;
    private boolean isAdmin;

    public User(String username, String name, String lastName, String password, String email, boolean isAdmin) {
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
        this.routines = new ArrayList<Routine>();
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return this.username;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getLastName() {
        return this.lastName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Routine> getRoutines() {
        return routines;
    }
    public void addRoutine(Routine routine) {
        this.routines.add(routine);
    }
    public void removeRoutine(Routine routine) {
        this.routines.remove(routine);
    }

    public boolean isAdmin() {
        return isAdmin;
    }
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

}
