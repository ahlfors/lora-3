package yaruliy.controllers.rest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yaruliy.datastore.UserService;
import yaruliy.model.User;

@RestController
@RequestMapping(value = "/user")
public class UserController{
    private UserService userService;
    @Autowired public void setUserService(UserService userService){ this.userService = userService; }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String addUser(@RequestParam(name = "name") String name,
                          @RequestParam(name = "surname") String surname,
                          @RequestParam(name = "login") String login,
                          @RequestParam(name = "password") String password){
        userService.saveUser(new User(login, password, name, surname));
        return "success";
    }

    @RequestMapping(value = "get/{login}", method = RequestMethod.GET)
    public String getUser(@PathVariable String login){
        return userService.findByLogin(login).toString();
    }
}