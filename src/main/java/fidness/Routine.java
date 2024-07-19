package fidness;

import java.util.ArrayList;

public class Routine {
    private String name;
    private String description;
    private ArrayList<Excercise> excercises;

    public Routine(String name, String description) {
        this.name = name;
        this.description = description;
        this.excercises = new ArrayList<Excercise>();
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

    public ArrayList<Excercise> getExcercises() {
        return excercises;
    }
    public void addExcercise(Excercise excercise) {
        this.excercises.add(excercise);
    }
    public void removeExcercise(Excercise excercise) {
        this.excercises.remove(excercise);
    }

}
