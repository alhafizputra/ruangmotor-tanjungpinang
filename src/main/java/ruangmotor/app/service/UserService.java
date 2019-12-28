package ruangmotor.app.service;

import ruangmotor.app.model.User;

public interface UserService {

    void save(User user);

    User findByUsername(String username);

    User findByEmail(String email);

}
