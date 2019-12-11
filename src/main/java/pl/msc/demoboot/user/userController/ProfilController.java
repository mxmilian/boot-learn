package pl.msc.demoboot.user.userController;

import java.util.Locale;

import javax.ws.rs.GET;
import javax.ws.rs.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.msc.demoboot.user.db.User;
import pl.msc.demoboot.user.userService.UserService;
import pl.msc.demoboot.user.validators.ChangePasswordValidator;
import pl.msc.demoboot.user.validators.EditProfileValidator;
import pl.msc.demoboot.utilties.UserUtils;

@Controller
@Secured(value = {"ROLE_USER", "ROLE_ADMIN"})
public class ProfilController {

    @Autowired
    UserService userService; //tutaj pobieramy dane od uzytkownika

    @Autowired
    MessageSource messageSource;

    @GET
    @RequestMapping (value = "/profil")
    public String showUserProfilePage(Model model){
        String userEmail = UserUtils.getEmailLoggedUser();
        User user = userService.findUserByEmail(userEmail);
        int nrRole = user.getRoles().iterator().next().getId(); //odczytujemy numer roli z bazy danych
        user.setNrRoll(nrRole);
        model.addAttribute("user", user);
        return "profil";
    }

    //To wywoluje nam localhost:8080/editpassword i pobiera nam imie zalogowanego
    @GET
    @RequestMapping (value = "/editpassword")
    public String editUserPassword(Model model){
        String userEmail = UserUtils.getEmailLoggedUser();
        User user = userService.findUserByEmail(userEmail);
        model.addAttribute("user", user);
        return "editpassword";
    }

    @POST
    @RequestMapping( value = "/updatepass")
    public String changeUserPassword(User user, BindingResult result, Model model, Locale locale){
        String returnPage = null;
        String userEmail = UserUtils.getEmailLoggedUser();
        new ChangePasswordValidator().validate(user, result);
        new ChangePasswordValidator().checkPasswords(user.getNewPassword(), result);

        if(result.hasErrors()){
            returnPage = "editpassword";
        }else {
            userService.updateUserPassword(user.getNewPassword(), userEmail);
            returnPage = "editpassword";
            model.addAttribute("message", messageSource.getMessage("passwordChange.success", null, locale));
        }
        return returnPage;
    }

    @GET
    @RequestMapping (value = "/editprofile")
    public String editUserProfile(Model model){
        String userEmail = UserUtils.getEmailLoggedUser();
        User user = userService.findUserByEmail(userEmail);
        model.addAttribute("user", user);
        return "editprofile";
    }

    @POST
    @RequestMapping (value = "/updateprofile")
    public String changeUserDataAction(User user, BindingResult result, Model model, Locale locale){
        String returnPage = null;
        String emailUser = user.getEmail();

        User userExist = userService.findUserByEmail(emailUser);

        new EditProfileValidator().validateEmailExists(userExist, result);
        new EditProfileValidator().validate(user, result);

        if(result.hasErrors()){
            returnPage = "editprofile";
        } else {
            userService.updateUserProfile(user.getName(), user.getLastName(), user.getEmail(), user.getId());
            model.addAttribute("message", messageSource.getMessage("profilEdit.success", null, locale));
            returnPage = "afteredit";
        }
        return returnPage;
    }
}
