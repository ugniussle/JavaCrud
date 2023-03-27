package lt.ku.javacrud.entities;

import jakarta.persistence.*;

@Entity
@Table(name="workouts")
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String date;

    @Column
    private String max_size;

    @Column
    private String location;

    public Object getAttribute(String name) {
        return switch (name.toLowerCase()) {
            case "id" -> getId();
            case "name" -> getName();
            case "date" -> getDate();
            case "max_size" -> getMax_size();
            case "location" -> getLocation();
            default -> "";
        };
    }

    @Override
    public String toString() {
        return name + ", " + date;
    }

    public Workout() {
    }

    public Workout(String name, String date, String max_size, String location) {
        this.name = name;
        this.date = date;
        this.max_size = max_size;
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMax_size() {
        return max_size;
    }

    public void setMax_size(String max_size) {
        this.max_size = max_size;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
