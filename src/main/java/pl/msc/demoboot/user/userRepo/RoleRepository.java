package pl.msc.demoboot.user.userRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.msc.demoboot.user.db.Role;

@Repository("roleRepository")
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRole(String role);
}
