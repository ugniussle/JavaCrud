package lt.ku.javacrud.repositories;

import lt.ku.javacrud.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {

}
