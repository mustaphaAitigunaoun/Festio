package ma.fsk.pge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.builder()
                .username("mustapha")
                .password(passwordEncoder().encode("1234")) // Encrypt the password
                .roles("USER") // Assign roles
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf( csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/h2-console/**", "/login","/register", "/logout").permitAll() // Permettre l'accès public à certaines URL
                        .anyRequest().authenticated() // Tout le reste nécessite une authentification
                )
                .formLogin(form -> form
                        .loginPage("/login") // Page de connexion personnalisée
                        .defaultSuccessUrl("/evenements")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout") // Redirect after logout
                        .permitAll()
                );
        // formLogin().loginPage("/my-login.html").loginProcessingUrl("/my-login").defaultSuccessUrl("/my-home.html", true).permitAll()
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
