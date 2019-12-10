package pl.msc.demoboot.admin.adminImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.msc.demoboot.admin.adminRepo.AdminRepository;
import pl.msc.demoboot.admin.adminService.AdminService;
import pl.msc.demoboot.user.db.Role;
import pl.msc.demoboot.user.db.User;
import pl.msc.demoboot.user.userRepo.RoleRepository;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

//[2]
//tutaj implementuje pobrane rzeczy z admin repo
@Service("adminService")
@Transactional
public class AdminServiceImpl implements AdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Qualifier("adminRepository")
    @Autowired
    private AdminRepository adminRepository;

    @Qualifier("roleRepository")
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JpaContext jpaContext;

    @Override
    public Page<User> findAllUsers(Pageable pageable) {
        Page<User> usersList = adminRepository.findAll(pageable);
        return usersList;
    }

    @Override
    public User findUserById(int id) {
        return adminRepository.findUserById(id);
    }

    @Override
    public void updateUser(int id, int nrRoll, int active) {
        adminRepository.updateActivationUser(active, id);
        adminRepository.updateRoleUser(nrRoll, id);
    }

    @Override
    public Page<User> findAllSearchUsers(String param, Pageable pageable) {
        return adminRepository.findAllSearchUsers(param,pageable);
    }

    @Override
    public void insertInBatch(List<User> userList) {
        EntityManager em = jpaContext.getEntityManagerByManagedType(User.class);

        for(int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            Role role = roleRepository.findByRole("ROLE_USER");
            user.setRoles(new HashSet<Role>(Arrays.asList(role)));
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            em.persist(user);
            if (i%50 == 0 && i >0) {
                em.flush();
                em.clear();
                System.out.printf("Loaded " + i + " records from " + userList.size());
            }
        }
    }

    @Override // Mozemy tak sobie wykonywac metody jedna po drugiej dlatego, ze mamy tranzakcyjnosc
    public void deleteUserById(int id) {
        LOGGER.debug("[CALL >> AdminServiceImpl.deleteUserById > PARAM: ]" + id);
        LOGGER.debug("[CALL >> AdminRepository.deleteUserFromUserRole > PARAM: ]" + id);
        adminRepository.deleteUserFromUserRole(id);
        LOGGER.debug("[CALL >> AdminRepository.eleteUserFromUser > PARAM: ]" + id);
        adminRepository.deleteUserFromUser(id);
    }
}
