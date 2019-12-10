package pl.msc.demoboot.mainController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.ws.rs.GET;

@Controller
public class LoginController {
    @GET
    @RequestMapping(value = "/login")
    public String showLoginPage(){
        return "login";
    }

}
