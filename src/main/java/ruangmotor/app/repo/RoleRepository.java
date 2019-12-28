package ruangmotor.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ruangmotor.app.model.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {

}
