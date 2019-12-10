package pl.msc.demoboot.user.userController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.msc.demoboot.Email.emailInterface.EmailSender;
import pl.msc.demoboot.user.db.User;
import pl.msc.demoboot.user.validators.UserRegisterValidator;
import pl.msc.demoboot.user.userService.UserService;
import pl.msc.demoboot.utilties.AppUtils;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import java.util.Locale;

@Controller
public class RegisterController {
    @Autowired
    private UserService userService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    private EmailSender emailSender;

    @GET
    @RequestMapping(value = "/register")
    public String registerForm(Model model){
        User u = new User();
        model.addAttribute("user", u);
        return "register";
    }

    @POST
    @RequestMapping(value = "/adduser")
    public String registerAction(User user, BindingResult result, Model model, Locale locale){

        String returnPage = null;

        User userExist = userService.findUserByEmail(user.getEmail());

        new UserRegisterValidator().validateEmailExists(userExist, result);
        new UserRegisterValidator().validate(user,result);

        if(result.hasErrors()){
            returnPage = "register";
        }else {
            user.setActivationCode(AppUtils.activationCodeGenerator());
            String content = "Wymagane potwierdzenie reejestracji poprzez klikniecie w ponizszy link:\n" + "http://localhost:8080/activatelink/"+user.getActivationCode();

            userService.saveUser(user);
            emailSender.sendEmail(user.getEmail(), "Potwierdzenie rejestracji", content);
            model.addAttribute("message", messageSource.getMessage("user.register.success.email",null,locale));
            //model.addAttribute("user", new User());
            returnPage = "index";
        }
        return returnPage;
    }

    @POST
    @RequestMapping(value = "/activatelink/{activationCode}")
    public String activateAccont(@PathVariable("activationCode") String activationCode, Model model, Locale locale){
        userService.updateUserActivation(1, activationCode);
        model.addAttribute("message", messageSource.getMessage("user.register.success", null, locale));
        return "index";
    }
}
