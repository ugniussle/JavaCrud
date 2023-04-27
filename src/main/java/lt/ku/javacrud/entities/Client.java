package lt.ku.javacrud.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name="clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Vardas yra privalomas laukelis.")
    @Length(min = 3, max = 20, message = "Vardas turi būti nuo 3 iki 20 simbolių.")
    @Column
    private String name;

    @NotEmpty(message = "Pavardė yra privalomas laukelis.")
    @Length(min = 3, max = 25, message = "Pavardė turi būti nuo 3 iki 25 simbolių.")
    @Column
    private String surname;

    @NotEmpty(message = "El. paštas yra privalomas laukelis.")
    @Email(message = "Blogas el. paštas.")
    @Column
    private String email;

    @Column(nullable = true)
    private String document = null;

    @Length(max = 15, message = "Numeris turi būti ne ilgesnis nei 15 simbolių.")
    @Column
    private String phone;

    @Override
    public String toString() {
        return name + " " + surname;
    }

    public Client() {
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
