package yaruliy.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yaruliy.datastore.UserService;
import yaruliy.model.User;
import java.util.ArrayList;
import java.util.List;


@Service

public class UDService implements UserDetailsService {
    private UserService userService;
    @Autowired public void setUserService(UserService userService) { this.userService = userService; }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = userService.findByLogin(username);
        List<GrantedAuthority> gas = new ArrayList<>();
        gas.add(new SimpleGrantedAuthority("USER"));
        try{
            return new org.springframework.security.core.userdetails.User(
                    user.getLogin(),
                    passwordEncoder.encode(user.getPassword()),
                    true,
                    true,
                    true,
                    true,
                    gas);
        }
        catch (NullPointerException e){ System.out.println("User " + username + " not found."); }
        throw new UsernameNotFoundException("User " + username + " not found.");
    }

}
