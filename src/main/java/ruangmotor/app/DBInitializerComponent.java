package ruangmotor.app;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ruangmotor.app.model.Role;
import ruangmotor.app.repo.RoleRepository;

@Component
//ConditionalOnProperty is an annotation that checks if the property has value "true" and then it runs
@ConditionalOnProperty(name = "app.db-init", havingValue = "true")
//https://dzone.com/articles/spring-boot-applicationrunner-and-commandlinerunne

public class DBInitializerComponent implements CommandLineRunner {
    
    @Autowired
    RoleRepository roleRepository;
    
    
     @Override
    public void run(String... strings) {
        initlialiseRoles();
    }

    //initialises the DB with Roles
    private boolean initlialiseRoles() {
        try {
            roleRepository.deleteAll();
            List<Role> roleList = new ArrayList<>();
            roleList.add(new Role("ADMIN"));
            roleList.add(new Role("MANAGER"));
            roleRepository.save(roleList);
        } catch (Exception e) {
            System.out.println("Error Initialising Roles");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    
}