package de.tub.ise.anwsys.repos;

import org.springframework.data.repository.CrudRepository;

import de.tub.ise.anwsys.models.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByName(String name);
}
