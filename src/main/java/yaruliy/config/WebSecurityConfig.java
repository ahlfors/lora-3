package yaruliy.config;
/*import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import yaruliy.security.UDService;*/

/*@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)*/
public class WebSecurityConfig /*extends WebSecurityConfigurerAdapter*/{
    /*private UDService udService;
    @Autowired public WebSecurityConfig(UDService udService) { this.udService = udService; }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder amb) throws Exception {
        amb.userDetailsService(this.udService).passwordEncoder(new BCryptPasswordEncoder());
    }*/

    /*@Override
    public void configure(HttpSecurity https) throws Exception {
        https.headers().defaultsDisabled().cacheControl();
        https.csrf().disable().authorizeRequests()
                .antMatchers("/img/**").permitAll()
                .antMatchers("/registration").permitAll()
                .antMatchers("/lora/**").permitAll()
                .antMatchers("/map/**").permitAll()
                .antMatchers("/device/**").permitAll()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .anyRequest().authenticated()
                .antMatchers("/messages").hasRole("USER")
                .antMatchers("/devices").hasRole("USER")
                .antMatchers("/").hasRole("USER")
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/devices")
                    .failureUrl("/login?fail=true")
                    .permitAll()
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout=success")
                    .permitAll();
    }*/
}