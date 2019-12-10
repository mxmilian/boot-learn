package pl.msc.demoboot.user.userRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.msc.demoboot.user.db.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    @Modifying
    @Query("UPDATE User user SET user.password = :newPassword WHERE user.email = :email")
    void updateUserPassword(@Param("newPassword") String password, @Param("email") String email);

    @Modifying
    @Query("UPDATE User user SET user.name = :newName, user.lastName = :newLastName, user.email = :newEmail WHERE user.id = :id ")
    void updateUserProfile(@Param("newName") String newName, @Param("newLastName") String newLastName, @Param("newEmail") String newEmail, @Param("id") int id);

    @Modifying
    @Query("UPDATE User user SET user.active = :activeParam WHERE user.activationCode = :activationCode")
    public void updateActivation(@Param("activeParam") int activeParam, @Param("activationCode") String activationCode);
}

//select * from user where email = ?