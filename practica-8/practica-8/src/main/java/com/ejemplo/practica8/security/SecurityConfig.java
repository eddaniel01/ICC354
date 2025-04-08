package com.ejemplo.practica8.security;

import com.ejemplo.practica8.views.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends VaadinWebSecurity {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // ✅ Desactiva CSRF solo para H2 console
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));

        // ✅ Nueva forma moderna de desactivar frameOptions
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        // ✅ Autorizar acceso libre a rutas específicas
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**", "/images/**", "/VAADIN/**").permitAll()
        );

        // ⚙️ Configuración por defecto de Vaadin
        super.configure(http);

        // 👤 Vista de login
        setLoginView(http, LoginView.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
