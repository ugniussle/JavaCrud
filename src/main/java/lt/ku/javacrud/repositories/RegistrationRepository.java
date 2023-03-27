package lt.ku.javacrud.repositories;

import lt.ku.javacrud.entities.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Integer> {
    default List<Registration> getRegistrations() {
        return this.findAll();
    }

}
