package yaruliy.controllers.views;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PageController{
    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public String messagesPage(){ return "messages"; }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(){ return "login"; }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registrationPage(){ return "registration"; }

    @RequestMapping(value = "/device/*", method = RequestMethod.GET)
    public String devicePage(){ return "device"; }

    @RequestMapping(value = {"/", "/devices"}, method = RequestMethod.GET)
    public String devicesPage(){ return "devices"; }
}