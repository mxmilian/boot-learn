package pl.msc.demoboot.admin.adminController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.msc.demoboot.admin.adminService.AdminService;
import pl.msc.demoboot.user.db.User;
import pl.msc.demoboot.utilties.UserUtils;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//[4]
@Controller
public class AdminPageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminPageController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private MessageSource messageSource;

    private final int ELEMENTS = 15;

    @GET
    @RequestMapping(value = "/admin")
    @Secured(value = "ROLE_ADMIN")
    public String openAdminPage(){
        return "admin/admin";
    }

    @GET
    @RequestMapping(value = "/admin/users/{page}")
    @Secured(value = "ROLE_ADMIN")
    public String openAdminAllUsersPage(Model model, @PathVariable("page") int page){
        Page<User> pages = getAllUsersPage(page - 1, false, null);
        int totalPages = pages.getTotalPages();
        int currentPage = pages.getNumber();

        List<User> usersList = pages.getContent();
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage + 1);
        model.addAttribute("usersList", usersList);
        return "admin/users";
    }

    @GET
    @RequestMapping(value = "/admin/users/edit/{id}")
    @Secured(value = "ROLE_ADMIN")
    public String getUserToEdit(Model model, @PathVariable("id") int id){
        User user = new User();
        user = adminService.findUserById(id);

        Map<Integer, String> roleMap = new HashMap<Integer, String>();
        roleMap = prepareRoleMap();

        Map<Integer, String> activityMap = new HashMap<Integer, String>(); //te mapy buduja mape klucz wartosc
        activityMap = prepareActivityMap();

        int rola = user.getRoles().iterator().next().getId();

        user.setNrRoll(rola);

        model.addAttribute("roleMap", roleMap);
        model.addAttribute("activityMap", activityMap);
        model.addAttribute("user", user);

        return "admin/useredit";
    }

    @POST
    @RequestMapping(value = "/admin/updateuser/{id}")
    @Secured(value = "ROLE_ADMIN")
    public String updateUser(@PathVariable("id") int id, User user){
        int nrRoll = user.getNrRoll();
        int isActive = user.getActive();
        adminService.updateUser(id, nrRoll, isActive);
        return "redirect:/admin/users/1";
    }

    @GET
    @RequestMapping(value = "/admin/users/search/{searchWord}/{page}")
    @Secured(value = "ROLE_ADMIN")
    public String searchUsers(@PathVariable("searchWord") String searchWord, @PathVariable("page") int page, Model model) {
        Page<User> userSearchPage = getAllUsersPage(page -1, true, searchWord);
        int totalPages = userSearchPage.getTotalPages();
        int currentPage = userSearchPage.getNumber();
        List<User> usersList = userSearchPage.getContent();

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage + 1);
        model.addAttribute("usersList", usersList);
        return "admin/usersearch";
    }

    @GET
    @RequestMapping(value = "/admin/users/importusers")
    @Secured(value = "ROLE_ADMIN")
    public String uploadPageFromXML (Model model){
        return "admin/importusers";
    }

    @POST
    @RequestMapping(value = "/admin/users/upload")
    @Secured(value = "ROLE_ADMIN")
    public String importUsersFromXML(@RequestParam("filename") MultipartFile multipartFile) {
        String uploadDir = System.getProperty("user.dir") + "/uploads";
        File file;
        try {
            file = new File(uploadDir);
            if(!file.exists()) {
                file.mkdir();
            }
            Path fileAndPath = Paths.get(uploadDir, multipartFile.getOriginalFilename());
            Files.write(fileAndPath, multipartFile.getBytes());
            file = new File(fileAndPath.toString());
            List<User> userList = UserUtils.usersDataLoader(file);
            for (User user : userList) {
                System.out.println(user.getEmail() + " > " + user.getName());
            }
            adminService.insertInBatch(userList);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(uploadDir);

        return "redirect:/admin/users/1";
    }

    @DELETE
    @RequestMapping(value = "/admin/users/delete/{id}")
    @Secured(value = "ROLE_ADMIN")
    public String deleteUser(@PathVariable("id") int id){
        LOGGER.debug("[CALL >> AdminServiceImpl.deleteUserById > PARAM: ]" + id);
        adminService.deleteUserById(id);
        return "redirect:/admin/users/1";
    }

    //Pobranie uzytkownikow
    private Page<User> getAllUsersPage(int page, boolean search, String param){
        Page<User> usersPage = null;
        if(search == false) {
            usersPage = adminService.findAllUsers(PageRequest.of(page, ELEMENTS));
        } else {
            usersPage = adminService.findAllSearchUsers(param, PageRequest.of(page, ELEMENTS));
        }
        for(User users : usersPage){
            int numberOfRole = users.getRoles().iterator().next().getId();
            users.setNrRoll(numberOfRole);
        }
        return usersPage;
    }

    //przygorowanie mapy rol
    Map<Integer, String> prepareRoleMap(){
        Locale locale = Locale.getDefault();
        Map<Integer, String> roleMap = new HashMap<Integer, String>();
        roleMap.put(1, messageSource.getMessage("word.admin",null,locale));
        roleMap.put(2, messageSource.getMessage("word.user",null,locale));
        return roleMap;
    }
    //przygotowanie map aktywny nie aktywny
    Map<Integer, String> prepareActivityMap(){
        Locale locale = Locale.getDefault();
        Map<Integer, String> activityMap = new HashMap<Integer, String>();
        activityMap.put(0, messageSource.getMessage("word.nie",null,locale));
        activityMap.put(1, messageSource.getMessage("word.tak",null,locale));
        return activityMap;

    }
}
