package pl.msc.demoboot.admin.adminRepo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.msc.demoboot.user.db.User;

//[1]
//Tutaj selecty/updaty itd...
@Repository("adminRepository")
public interface AdminRepository extends JpaRepository<User, Integer> {
    User findUserById(int id); //Pobieramy te metode z spring jpa

    @Modifying
    @Query("UPDATE User user SET user.active = :intActive WHERE user.id = :id")
    void updateActivationUser(@Param("intActive") int active, @Param("id") int id);

    @Modifying
    @Query(value = "UPDATE user_role role SET role.role_id = :roleId WHERE role.user_id = :id", nativeQuery = true)
    void updateRoleUser(@Param("roleId") int nrRoll, @Param("id") int id);

    @Query(value = "SELECT * FROM User user WHERE user.name LIKE %:param% OR user.last_name LIKE %:param% OR email like %:param%", nativeQuery = true)
    Page<User> findAllSearchUsers(@Param("param") String param, Pageable pageable);

    @Modifying
    @Query(value = "DELETE FROM user_role WHERE user_id = :id", nativeQuery = true)
    void deleteUserFromUserRole (@Param("id") int id);

    @Modifying
    @Query(value = "DELETE FROM user WHERE user_id = :id", nativeQuery = true)
    void deleteUserFromUser (@Param("id") int id);
}
