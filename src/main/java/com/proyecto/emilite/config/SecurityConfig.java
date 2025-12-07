package com.proyecto.emilite.config;

import com.proyecto.emilite.model.Usuario;
import com.proyecto.emilite.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // 1. Rutas PÚBLICAS (sin autenticación)
                .requestMatchers(
                    "/",                       // Landing page
                    "/login",                  // Página de login
                    "/usuarios/registro",      // Página de registro 
                    "/usuarios/registro-publico", // Página de registro público
                    "/css/**",                 // Archivos CSS
                    "/js/**",                  // Archivos JavaScript
                    "/images/**",              // Imágenes
                    "/error"                   // Página de error
                ).permitAll()

                // 2. Rutas solo para ADMIN
                .requestMatchers(
                    "/api/usuarios/**",        // API de usuarios
                    "/usuarios",               // Lista de usuarios
                    "/usuarios/**",            // Gestión de usuarios
                    "/servicios/nuevo",        // Crear servicio
                    "/servicios/*/editar",     // Editar servicio
                    "/servicios/*/eliminar",   // Eliminar servicio
                    "/promociones/nueva",      // Crear promoción
                    "/promociones/*/editar",   // Editar promoción
                    "/promociones/*/eliminar", // Eliminar promoción
                    "/pagos/nuevo",            // Crear pago
                    "/pagos/*/editar",         // Editar pago
                    "/pagos/*/eliminar",       // Eliminar pago
                    "/reportes",               // Reportes
                    "/reportes/**"             // Reportes específicos
                ).hasRole("ADMIN")

                // 3. Rutas solo para ENTRENADOR
                .requestMatchers(
                    "/entrenador/**"           // Todas las rutas de entrenador
                ).hasRole("ENTRENADOR")

                // 4. Rutas solo para CLIENTE
                .requestMatchers(
                    "/cliente/pagos",          // Pagos del cliente
                    "/cliente/rutinas",        // Rutinas del cliente
                    "/cliente/perfil/**",      // Perfil del cliente
                    "/cliente/servicios",      // Servicios del cliente
                    "/cliente/pagos/pdf"       // Reporte PDF de pagos
                ).hasRole("CLIENTE")

                // 5. Rutas para CLIENTE Y ENTRENADOR (si aplica)
                .requestMatchers(
                    "/dashboard",              // Dashboard principal
                    "/servicios",              // Ver servicios (solo lectura)
                    "/promociones",            // Ver promociones (solo lectura)
                    "/pagos"                   // Ver pagos (solo lectura)
                ).hasAnyRole("CLIENTE", "ENTRENADOR", "ADMIN")

                // 6. Cualquier otra ruta requiere autenticación (rol mínimo)
                .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")  // URL en caso de error
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/error/403")  // Página de acceso denegado
            );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Usuario usuario = usuarioRepository.findByUserName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

            // Asegúrate de que el rol tenga el prefijo "ROLE_"
            String role = usuario.getRol().getNombre();
            if (!role.startsWith("ROLE_")) {
                role = "ROLE_" + role;
            }

            var authorities = Collections.singletonList(
                new SimpleGrantedAuthority(role)
            );

            return new User(
                usuario.getUserName(),
                usuario.getPassword(),
                authorities
            );
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}