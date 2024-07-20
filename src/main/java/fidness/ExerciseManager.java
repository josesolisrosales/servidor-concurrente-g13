package fidness;

import java.util.ArrayList;
import java.util.List;

public class ExerciseManager {
    private List<Exercise> exercises;

    public ExerciseManager() {
        this.exercises = FileManager.loadExercises();
        if (exercises.isEmpty()) {
            initializeDefaultExercises();
        }
    }

    public void addExercise(String name, String description, String videoUrl, String muscleGroup) {
        Exercise newExercise = new Exercise(name, description, videoUrl, muscleGroup);
        exercises.add(newExercise);
        saveExercises();
    }

    public List<Exercise> getAllExercises() {
        return new ArrayList<>(exercises);
    }

    public List<Exercise> getExercisesByMuscleGroup(String muscleGroup) {
        List<Exercise> filteredExercises = new ArrayList<>();
        for (Exercise exercise : exercises) {
            if (exercise.getMuscleGroup().equalsIgnoreCase(muscleGroup)) {
                filteredExercises.add(exercise);
            }
        }
        return filteredExercises;
    }

    public void deleteExercise(Exercise exercise) {
        exercises.remove(exercise);
        saveExercises();
    }

    private void saveExercises() {
        FileManager.saveExercises(exercises);
    }

    private void initializeDefaultExercises() {
        addExercise("Push-ups", "Standard push-ups", "", "Chest");
        addExercise("Squats", "Bodyweight squats", "", "Legs");
        addExercise("Pull-ups", "Standard pull-ups", "", "Back");
        addExercise("Lunges", "Forward lunges", "", "Legs");
        addExercise("Plank", "Standard plank hold", "", "Core");
        addExercise("Dips", "Tricep dips", "", "Arms");
        addExercise("Crunches", "Standard crunches", "", "Core");
        addExercise("Jumping Jacks", "Standard jumping jacks", "", "Cardio");
        addExercise("Mountain Climbers", "Mountain climbers exercise", "", "Core");
        addExercise("Burpees", "Standard burpees", "", "Full Body");
        addExercise("Bicycle Crunches", "Bicycle crunches for abs", "", "Core");
        addExercise("Leg Raises", "Lying leg raises", "", "Core");
        addExercise("Tricep Extensions", "Overhead tricep extensions", "", "Arms");
        addExercise("Calf Raises", "Standing calf raises", "", "Legs");
        addExercise("Russian Twists", "Seated russian twists", "", "Core");
    }
}