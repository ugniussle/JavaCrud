package lt.ku.javacrud.repositories;

import lt.ku.javacrud.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    default List<Client> getClients() {
        return this.findAll();
    }

}
