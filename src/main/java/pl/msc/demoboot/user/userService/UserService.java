package pl.msc.demoboot.user.userService;

import org.springframework.stereotype.Service;
import pl.msc.demoboot.user.db.User;

@Service
public interface UserService {
    User findUserByEmail(String email);
    void saveUser(User user);
    void updateUserPassword(String newPassword, String email);
    void updateUserProfile(String newName, String newLastName, String newEmail, int id);
    void updateUserActivation(int activeCode, String activationCode);
}
