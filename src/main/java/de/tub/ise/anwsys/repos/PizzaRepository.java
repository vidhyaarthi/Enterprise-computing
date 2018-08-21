package de.tub.ise.anwsys.repos;

import org.springframework.data.repository.CrudRepository;

import de.tub.ise.anwsys.models.Pizza;
import de.tub.ise.anwsys.models.User;

import java.util.Optional;

public interface PizzaRepository extends CrudRepository<Pizza,Integer> {
    Optional<Pizza> findByName(String name);
    
}
