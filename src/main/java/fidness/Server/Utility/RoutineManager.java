package fidness.Server.Utility;

import fidness.Objects.Routine;
import fidness.Objects.Exercise;
import java.util.List;

public class RoutineManager {
    private static RoutineManager instance;

    private RoutineManager() {}

    public static synchronized RoutineManager getInstance() {
        if (instance == null) {
            instance = new RoutineManager();
        }
        return instance;
    }

    public List<Routine> getRoutines(String username) {
        String query = "username = '" + username + "'";
        return DerbyManager.getInstance().selectFromRoutines(query);
    }

    public boolean insertRoutine(Routine routine, String username) {
        return DerbyManager.getInstance().insertRoutine(routine, username);
    }

    public boolean addRoutine(Routine routine, String username) {
        return DerbyManager.getInstance().insertRoutine(routine, username);
    }

    public boolean removeRoutine(Routine routine, String username) {
        return DerbyManager.getInstance().deleteRoutine(routine.getName(), username);
    }

    // Método para actualizar una rutina existente
    public boolean updateRoutine(Routine routine, String username) {
        return DerbyManager.getInstance().updateRoutine(routine, username);
    }

    // Método para obtener una rutina por su nombre y usuario
    public Routine getRoutineByNameAndUser(String name, String username) {
        String query = "name = '" + name + "' AND username = '" + username + "'";
        List<Routine> routines = DerbyManager.getInstance().selectFromRoutines(query);
        return routines.isEmpty() ? null : routines.get(0);
    }

    // Método para agregar un ejercicio a una rutina
    public boolean addExerciseToRoutine(String routineName, String username, Exercise exercise) {
        Routine routine = getRoutineByNameAndUser(routineName, username);
        if (routine != null) {
            routine.addExcercise(exercise);
            return updateRoutine(routine, username);
        }
        return false;
    }

    // Método para eliminar un ejercicio de una rutina
    public boolean removeExerciseFromRoutine(String routineName, String username, Exercise exercise) {
        Routine routine = getRoutineByNameAndUser(routineName, username);
        if (routine != null) {
            routine.removeExcercise(exercise);
            return updateRoutine(routine, username);
        }
        return false;
    }
}