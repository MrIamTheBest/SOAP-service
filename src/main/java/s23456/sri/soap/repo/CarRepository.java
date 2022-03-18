package s23456.sri.soap.repo;

import org.springframework.data.repository.CrudRepository;
import s23456.sri.soap.model.Car;

import java.util.List;

public interface CarRepository extends CrudRepository<Car, Long> {
    List<Car> findAll();

}
