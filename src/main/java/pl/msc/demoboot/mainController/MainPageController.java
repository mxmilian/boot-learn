package pl.msc.demoboot.mainController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.ws.rs.GET;

@Controller
public class MainPageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainPageController.class);

    @GET
    @RequestMapping(value = {"/", "/index"})
    public String showMainPage(){
        LOGGER.debug("**** WYWOŁANO > showMainPage");
        return "index";
    }
}
