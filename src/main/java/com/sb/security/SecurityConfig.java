package com.sb.security;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	 private final CustomLoginSuccessHandler successHandler;
	    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

	    public SecurityConfig(CustomLoginSuccessHandler successHandler,
                OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) {
this.successHandler = successHandler;
this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
}


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    	http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
        	    .loginPage("/login")
        	    .successHandler(successHandler)
        	    .failureUrl("/login?error")   
        	    .permitAll()
        	)

        .oauth2Login(oauth -> oauth
            .loginPage("/login")
            .successHandler(oAuth2LoginSuccessHandler)
        )
        .logout(logout -> logout
            .logoutSuccessUrl("/login?logout")
            .permitAll()
        );


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    

}
