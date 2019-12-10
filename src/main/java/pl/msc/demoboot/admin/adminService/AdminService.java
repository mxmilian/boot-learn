package pl.msc.demoboot.admin.adminService;

import org.springframework.data.domain.Pageable;
import pl.msc.demoboot.user.db.User;
import org.springframework.data.domain.Page;

import java.util.List;

//[3]
//a przez ten interface to przechodzi i tego uzywam do kontrollera
public interface AdminService {
    Page<User> findAllUsers(Pageable pageable);
    User findUserById(int id);
    void updateUser(int id, int nrRoll, int active);
    Page<User> findAllSearchUsers(String param, Pageable pageable);
    void insertInBatch(List<User> userList);
    void deleteUserById(int id);
    
}
