package leviathan.CarPartsStore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
              .csrf().disable()
              .authorizeHttpRequests()
              .requestMatchers("/cart/**", "/admin/**").authenticated()
              .anyRequest().permitAll()
              .and()
              .logout()
              .logoutSuccessUrl("/")
              .and()
              .oauth2Login()
              .loginPage("/login");

        return httpSecurity.build();
    }
}
