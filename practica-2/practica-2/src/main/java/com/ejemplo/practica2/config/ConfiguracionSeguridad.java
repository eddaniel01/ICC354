package com.ejemplo.practica2.config;

import com.ejemplo.practica2.servicios.seguridad.SeguridadServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ConfiguracionSeguridad {

    //Configuación para la validación del acceso modo JDBC
    private DataSource dataSource;
    @Value("${query.user-jdbc}")
    private String queryUsuario;
    @Value("${query.rol-jdbc}")
    private String queryRol;
    //Opción JPA
    private SeguridadServices seguridadServices;
    private PasswordEncoder passwordEncoder;
    private JWTAutorizacionFilter jwtAutorizacionFilter;

    public ConfiguracionSeguridad(DataSource dataSource, SeguridadServices seguridadServices, PasswordEncoder passwordEncoder, JWTAutorizacionFilter jwtAutorizacionFilter) {
        this.dataSource = dataSource;
        this.seguridadServices = seguridadServices;
        this.passwordEncoder = passwordEncoder;
        this.jwtAutorizacionFilter = jwtAutorizacionFilter;
    }

    /**
     * Manejando el MvcRequestMatcher como mitigación al problema de https://spring.io/security/cve-2023-34035
     * Ver: https://stackoverflow.com/questions/76809698/spring-security-method-cannot-decide-pattern-is-mvc-or-not-spring-boot-applicati
     *
     * @param introspector
     * @return
     */
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    /**
     * Authenticador Provider utilizando JPA.
     * @return
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(seguridadServices);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    /**
     * La autentificación de los usuarios.
     * Para habilitar la autentificación de vía JDBC y en Memoria es necesario desconfigurar la clase
     * JPA por la inyección de dependencia.
     *
     * @param http
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        //En Memoria
        /*System.out.println("Autentificación en Memoria");
        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder)
                .withUser("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN","USER")
                .and()
                .withUser("usuario")
                .password(passwordEncoder.encode("1234"))
                .roles("USER")
                .and()
                .withUser("vendedor")
                .password(passwordEncoder.encode("1234"))
                .roles("VENDEDOR");*/

        //Configuración JDBC
        /*System.out.println("Autentificación JDBC");
        auth.jdbcAuthentication().usersByUsernameQuery(queryUsuario)
                .authoritiesByUsernameQuery(queryRol)
                .passwordEncoder(passwordEncoder)
                .dataSource(dataSource);*/

        //Servicio.
        System.out.println("Autentificación en JPA");
        auth.userDetailsService(seguridadServices)
                .passwordEncoder(passwordEncoder);


        return auth.build();
    }

    /**
     * Configurando dos filtros diferentes, en este caso para no almacenar las peticiones en la sesion
     * para el caso de JWT en Spring Boot.
     * @param http
     * @return
     * @throws Exception
     */

    @Bean
    @Order(1) // indicates the initialization order
    public SecurityFilterChain securityFilterApi(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/mock/jwt/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/mock/jwt/**")).authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAutorizacionFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Metodo para el registro de autorización, ver la diferencia entre antmatcher y mvcmatcher:
     * https://stackoverflow.com/questions/50536292/difference-between-antmatcher-and-mvcmatcher
     * MVCMatcher controla más patrones asociados, mientras que el antmatcher es exacto.
     *
     * @param http
     * @param mvc
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/")).hasAnyRole("ADMIN", "USER")
                        .requestMatchers(mvc.pattern("/h2-console/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/admin/**")).hasRole("ADMIN")
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/user/**")).hasRole("USER")
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/logout")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/mock/**")).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureUrl("/login?error")
                        .defaultSuccessUrl("/")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .logoutRequestMatcher(AntPathRequestMatcher.antMatcher("/logout"))
                        .permitAll()
                );

        return http.build();
    }

}

