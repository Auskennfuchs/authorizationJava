package de.afb.persistence.repository;

import de.afb.persistence.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
