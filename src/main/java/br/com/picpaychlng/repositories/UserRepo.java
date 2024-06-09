package br.com.picpaychlng.repositories;

import br.com.picpaychlng.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

}