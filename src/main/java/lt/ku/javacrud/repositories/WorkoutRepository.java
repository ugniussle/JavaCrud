package lt.ku.javacrud.repositories;

import lt.ku.javacrud.entities.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Integer> {
    default List<Workout> getWorkouts() {
        return this.findAll();
    }

}
