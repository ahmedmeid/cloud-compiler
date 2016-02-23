package nr.co.ahmedeid.cloudcomp.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@PropertySource(value = { "classpath:database.properties" })
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private DriverManagerDataSource dataSource;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/", "/login", "/resources/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/home")
                .permitAll()
                .and()
            .logout()
                .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery("select username,password, enabled from users where username=?")
            .authoritiesByUsernameQuery("select username, role from user_roles where username=?");
    }
    
    @Bean(name = "dataSource")
   	public DriverManagerDataSource dataSource() {
   	    DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
   	    driverManagerDataSource.setDriverClassName(environment.getProperty("database.driver-class-name"));
   	    driverManagerDataSource.setUrl(environment.getProperty("database.url"));
   	    driverManagerDataSource.setUsername(environment.getProperty("database.username"));
   	    driverManagerDataSource.setPassword(environment.getProperty("database.password"));
   	    return driverManagerDataSource;
   	}
}
