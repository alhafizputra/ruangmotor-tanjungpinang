package ruangmotor.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ruangmotor.app.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);
}
