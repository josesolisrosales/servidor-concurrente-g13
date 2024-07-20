package fidness;

import java.util.ArrayList;
import java.io.Serializable;

public class Routine implements  Serializable {
    // Serializamos la clase para poder guardarla en un archivo
    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private ArrayList<Exercise> exercises;

    public Routine(String name, String description) {
        this.name = name;
        this.description = description;
        this.exercises = new ArrayList<Exercise>();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Exercise> getExcercises() {
        return exercises;
    }
    public void addExcercise(Exercise exercise) {
        this.exercises.add(exercise);
    }
    public void removeExcercise(Exercise exercise) {
        this.exercises.remove(exercise);
    }

}
