package pl.msc.demoboot.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DataSource dataSource;


    @Value("${spring.queries.users-query}")
    private String userQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { //Funkcja sprawdzajaca permission ludzi
        auth.jdbcAuthentication().usersByUsernameQuery(userQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()//Dla http Request my wywolujemy zadanie autoryzacji
                .antMatchers("/").permitAll()//+
                .antMatchers("/login").permitAll()//+ Te linijki definiuja 4 wywolania dla wszystkich uzytkownikow
                .antMatchers("/register").permitAll()//+
                .antMatchers("/adduser").permitAll()//+
                .antMatchers("/activatelink/**").permitAll()//+
                //.antMatchers("/admin").hasAuthority("ROLE_ADMIN")//A Ta linijka definiuje wywolanie tylko dla admina
                .anyRequest().authenticated()//A
                .and().csrf().disable()
                .formLogin()//Forumalrz logowania
                .loginPage("/login")//wywoluje login page jesli bedziemy probowali sie dostac do jakichkolwiek podstron oprocz permitAll
                .failureUrl("/login?error=true")//jesli ktos sie nie zaloguje to to sie wywola
                .defaultSuccessUrl("/").usernameParameter("email")//jesli email
                .passwordParameter("password")//i haslo bedzie pasowalo to zaloguje sie i bedziemy na / jako zalogowani uzytkownicy
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")) //Tutaj bedziemy sie wylogowywac i zniszczy sesje
                .logoutSuccessUrl("/")
                .and().exceptionHandling().accessDeniedPage("/denied");//Jesli jestesmy zalogowani a chcemy do admina to tutaj nas przekieruje
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/resources/**", "/statics/**","/css/**", "/js/**", "/images/**", "/incl/**");
    }
}
