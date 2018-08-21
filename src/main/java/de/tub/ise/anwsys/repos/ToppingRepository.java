package de.tub.ise.anwsys.repos;

import org.springframework.data.repository.CrudRepository;

import de.tub.ise.anwsys.models.Pizza;
import de.tub.ise.anwsys.models.Topping;

import java.util.List;
import java.util.Optional;

public interface ToppingRepository extends CrudRepository<Topping,Integer> {
    Optional<Topping> findByName(String name);
    
    List<Topping> findById(Integer id);
    List<Topping> findByPizza(Pizza pizza);
    
}
