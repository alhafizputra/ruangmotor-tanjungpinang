package ruangmotor.app.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ruangmotor.app.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    public List<User> findAllByStatus(User.Status status);

    public Integer countByStatus(User.Status status);
}
