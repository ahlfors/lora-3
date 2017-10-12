package yaruliy.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UDService udService;
    private final LoraAuthEntryPoint authenticationEntryPoint;
    private final LoraAuthFailureHandler authenticationFailureHandler;
    private final LoraAuthSuccessHandler authSuccessHandler;
    private final LoraFilter loraFilter;

    @Autowired
    public WebSecurityConfig(UDService udService,
                             LoraAuthEntryPoint authenticationEntryPoint,
                             LoraAuthFailureHandler authenticationFailureHandler,
                             LoraAuthSuccessHandler loraAuthSuccessHandler,
                             LoraFilter loraFilter) {
        this.udService = udService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.authSuccessHandler = loraAuthSuccessHandler;
        this.loraFilter = loraFilter;
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder amb) throws Exception {
        amb.userDetailsService(this.udService).passwordEncoder(new BCryptPasswordEncoder());
    }


    @Override
    public void configure(HttpSecurity https) throws Exception {
        https.headers().defaultsDisabled().cacheControl();
        https.csrf().disable().authorizeRequests()
                .antMatchers("/registration").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/map/**").permitAll()
                .antMatchers("/device/**").permitAll()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/img/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .anyRequest().authenticated()
                .antMatchers("/lora/**").hasRole("USER")
                .antMatchers("/messages").hasRole("USER")
                .antMatchers("/devices").hasRole("USER")
                .antMatchers("/").hasRole("USER")
                .and();
        https.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        https.formLogin().successHandler(authSuccessHandler);
        https.formLogin().failureHandler(authenticationFailureHandler);
        https.formLogin().defaultSuccessUrl("/devices", true);
        https.httpBasic().disable();
        https.addFilterAfter(loraFilter, BasicAuthenticationFilter.class);
    }
}