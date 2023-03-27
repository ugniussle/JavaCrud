package lt.ku.javacrud.entities;

import jakarta.persistence.*;

@Entity
@Table(name="registrations")
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private Client client;

    @OneToOne
    private Workout workout;

    @Column
    private String registration_date;

    public Object getAttribute(String name) {
        System.out.println(name);
        return switch (name.toLowerCase()) {
            case "id" -> getId();
            case "registration_date" -> getRegistration_date();
            case "client" -> getClient().toString();
            case "workout" -> getWorkout().toString();
            default -> "";
        };
    }

    public Registration() {
    }

    public Registration(Client client, Workout workout, String registration_date) {
        this.client = client;
        this.workout = workout;
        this.registration_date = registration_date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(String registration_date) {
        this.registration_date = registration_date;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }
}
