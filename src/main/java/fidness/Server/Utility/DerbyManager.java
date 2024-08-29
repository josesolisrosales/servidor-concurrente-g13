package fidness.Server.Utility;

import fidness.Objects.Exercise;
import fidness.Objects.Routine;
import fidness.Objects.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DerbyManager {
    private static final String DB_URL = "jdbc:derby:FidnessDB;create=true";
    private static final String USER_TABLE = "Users";
    private static final String EXERCISE_TABLE = "Exercises";
    private static final String ROUTINE_TABLE = "Routines";
    private static final String ROUTINE_EXERCISE_TABLE = "RoutineExercises";
    private static DerbyManager instance;

    private DerbyManager() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        initializeDatabase();
    }

    public static synchronized DerbyManager getInstance() {
        if (instance == null) {
            instance = new DerbyManager();
        }
        return instance;
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            createUserTable(conn);
            createExerciseTable(conn);
            createRoutineTable(conn);
            createRoutineExerciseTable(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createUserTable(Connection conn) throws SQLException {
        if (!tableExists(conn, USER_TABLE)) {
            String sql = "CREATE TABLE " + USER_TABLE + " (" +
                    "username VARCHAR(50) PRIMARY KEY, " +
                    "name VARCHAR(50), " +
                    "lastName VARCHAR(50), " +
                    "password VARCHAR(50), " +
                    "email VARCHAR(50), " +
                    "isAdmin BOOLEAN)";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            }
        }
    }

    private void createExerciseTable(Connection conn) throws SQLException {
        if (!tableExists(conn, EXERCISE_TABLE)) {
            String sql = "CREATE TABLE " + EXERCISE_TABLE + " (" +
                    "name VARCHAR(50) PRIMARY KEY, " +
                    "description VARCHAR(255), " +
                    "videoUrl VARCHAR(255), " +
                    "muscleGroup VARCHAR(50))";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            }
        }
    }

    private void createRoutineTable(Connection conn) throws SQLException {
        if (!tableExists(conn, ROUTINE_TABLE)) {
            String sql = "CREATE TABLE " + ROUTINE_TABLE + " (" +
                    "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                    "name VARCHAR(50), " +
                    "description VARCHAR(255), " +
                    "username VARCHAR(50), " +
                    "FOREIGN KEY (username) REFERENCES " + USER_TABLE + "(username))";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            }
        }
    }

    private void createRoutineExerciseTable(Connection conn) throws SQLException {
        if (!tableExists(conn, ROUTINE_EXERCISE_TABLE)) {
            String sql = "CREATE TABLE " + ROUTINE_EXERCISE_TABLE + " (" +
                    "routineId INT, " +
                    "exerciseName VARCHAR(50), " +
                    "PRIMARY KEY (routineId, exerciseName), " +
                    "FOREIGN KEY (routineId) REFERENCES " + ROUTINE_TABLE + "(id), " +
                    "FOREIGN KEY (exerciseName) REFERENCES " + EXERCISE_TABLE + "(name))";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            }
        }
    }

    private boolean tableExists(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet resultSet = meta.getTables(null, null, tableName.toUpperCase(), new String[] {"TABLE"});
        return resultSet.next();
    }

    // Métodos para User
    public List<User> selectFromUsers(String query) {
        List<User> users = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + USER_TABLE + " WHERE " + query)) {
            while (rs.next()) {
                users.add(new User(
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getString("lastName"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getBoolean("isAdmin")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<User> selectFromUsers() {
        return selectFromUsers("1=1");
    }

    public boolean insertUser(User user) {
        String sql = "INSERT INTO " + USER_TABLE + " (username, name, lastName, password, email, isAdmin) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getLastName());
            pstmt.setString(4, user.getPassword());
            pstmt.setString(5, user.getEmail());
            pstmt.setBoolean(6, user.isAdmin());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE " + USER_TABLE + " SET name = ?, lastName = ?, password = ?, email = ?, isAdmin = ? WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getEmail());
            pstmt.setBoolean(5, user.isAdmin());
            pstmt.setString(6, user.getUsername());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(String username) {
        String sql = "DELETE FROM " + USER_TABLE + " WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Métodos para Exercise
    public List<Exercise> selectFromExercises(String query) {
        List<Exercise> exercises = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + EXERCISE_TABLE + " WHERE " + query)) {
            while (rs.next()) {
                exercises.add(new Exercise(
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("videoUrl"),
                        rs.getString("muscleGroup")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exercises;
    }

    public List<Exercise> selectFromExercises() {
        return selectFromExercises("1=1");
    }

    public boolean insertExercise(Exercise exercise) {
        String sql = "INSERT INTO " + EXERCISE_TABLE + " (name, description, videoUrl, muscleGroup) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, exercise.getName());
            pstmt.setString(2, exercise.getDescription());
            pstmt.setString(3, exercise.getVideoUrl());
            pstmt.setString(4, exercise.getMuscleGroup());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateExercise(Exercise exercise) {
        String sql = "UPDATE " + EXERCISE_TABLE + " SET description = ?, videoUrl = ?, muscleGroup = ? WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, exercise.getDescription());
            pstmt.setString(2, exercise.getVideoUrl());
            pstmt.setString(3, exercise.getMuscleGroup());
            pstmt.setString(4, exercise.getName());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteExercise(String name) {
        String sql = "DELETE FROM " + EXERCISE_TABLE + " WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Métodos para Routine
    public List<Routine> selectFromRoutines(String query) {
        List<Routine> routines = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + ROUTINE_TABLE + " WHERE " + query)) {
            while (rs.next()) {
                Routine routine = new Routine(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("username")
                );
                loadRoutineExercises(conn, routine);
                routines.add(routine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routines;
    }

    private void loadRoutineExercises(Connection conn, Routine routine) throws SQLException {
        String sql = "SELECT e.* FROM " + EXERCISE_TABLE + " e JOIN " + ROUTINE_EXERCISE_TABLE + " re ON e.name = re.exerciseName WHERE re.routineId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, routine.getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                routine.addExcercise(new Exercise(
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("videoUrl"),
                        rs.getString("muscleGroup")
                ));
            }
        }
    }

    public boolean insertRoutine(Routine routine, String username) {
        String sql = "INSERT INTO " + ROUTINE_TABLE + " (name, description, username) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, routine.getName());
            pstmt.setString(2, routine.getDescription());
            pstmt.setString(3, username);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    routine.setId(generatedKeys.getInt(1));
                    insertRoutineExercises(conn, routine);
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void insertRoutineExercises(Connection conn, Routine routine) throws SQLException {
        String sql = "INSERT INTO " + ROUTINE_EXERCISE_TABLE + " (routineId, exerciseName) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Exercise exercise : routine.getExcercises()) {
                pstmt.setInt(1, routine.getId());
                pstmt.setString(2, exercise.getName());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    public boolean updateRoutine(Routine routine, String username) {
        String sql = "UPDATE " + ROUTINE_TABLE + " SET name = ?, description = ? WHERE id = ? AND username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, routine.getName());
            pstmt.setString(2, routine.getDescription());
            pstmt.setInt(3, routine.getId());
            pstmt.setString(4, username);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            // Actualizar ejercicios de la rutina
            deleteRoutineExercises(conn, routine.getId());
            insertRoutineExercises(conn, routine);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void deleteRoutineExercises(Connection conn, int routineId) throws SQLException {
        String sql = "DELETE FROM " + ROUTINE_EXERCISE_TABLE + " WHERE routineId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, routineId);
            pstmt.executeUpdate();
        }
    }

    public boolean deleteRoutine(String name, String username) {
        String sql = "DELETE FROM " + ROUTINE_TABLE + " WHERE name = ? AND username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, username);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}