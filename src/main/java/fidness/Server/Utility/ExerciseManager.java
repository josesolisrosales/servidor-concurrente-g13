package fidness.Server.Utility;

import fidness.Objects.Exercise;
import java.util.List;

public class ExerciseManager {
    private static ExerciseManager instance;

    private ExerciseManager() {}

    public static synchronized ExerciseManager getInstance() {
        if (instance == null) {
            instance = new ExerciseManager();
        }
        return instance;
    }

    public List<Exercise> getAllExercises() {
        return DerbyManager.getInstance().selectFromExercises();
    }

    public List<Exercise> getExercisesByMuscleGroup(String muscleGroup) {
        String query = "muscleGroup = '" + muscleGroup + "'";
        return DerbyManager.getInstance().selectFromExercises(query);
    }

    public boolean addExercise(Exercise exercise) {
        return DerbyManager.getInstance().insertExercise(exercise);
    }

    public boolean deleteExercise(String name) {
        return DerbyManager.getInstance().deleteExercise(name);
    }

    // Método para actualizar un ejercicio existente
    public boolean updateExercise(Exercise exercise) {
        return DerbyManager.getInstance().updateExercise(exercise);
    }

    // Método para obtener un ejercicio por su nombre
    public Exercise getExerciseByName(String name) {
        String query = "name = '" + name + "'";
        List<Exercise> exercises = DerbyManager.getInstance().selectFromExercises(query);
        return exercises.isEmpty() ? null : exercises.get(0);
    }
}