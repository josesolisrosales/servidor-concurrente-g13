package fidness.Objects;

import java.io.Serializable;

public class Exercise implements Serializable {
    // Serializamos la clase para poder guardarla en un archivo
    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private String videoUrl;
    private String muscleGroup;

    public Exercise(String name, String description, String videoUrl, String muscleGroup) {
        this.name = name;
        this.description = description;
        this.videoUrl = videoUrl;
        this.muscleGroup = muscleGroup;
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

    public String getVideoUrl() {
        return videoUrl;
    }
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }
    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    @Override
    public String toString() {
        return name;
    }
}
