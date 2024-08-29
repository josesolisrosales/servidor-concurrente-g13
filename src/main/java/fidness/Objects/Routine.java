package fidness.Objects;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.List;

public class Routine implements  Serializable {
    // Serializamos la clase para poder guardarla en un archivo
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String description;
    private ArrayList<Exercise> exercises;
    private String owner;

    public Routine(int id, String name, String description, String owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.exercises = new ArrayList<Exercise>();
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

}
