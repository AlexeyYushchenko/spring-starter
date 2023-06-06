package com.yadev.spring.config;

import com.yadev.spring.service.UserService;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Set;

import static com.yadev.spring.database.entity.Role.ADMIN;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/login", "/users/registration", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/\\d+/delete").hasAuthority(ADMIN.getAuthority())
                        .requestMatchers("/admin/**").hasAuthority(ADMIN.getAuthority())
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID"))
//                .httpBasic(Customizer.withDefaults());
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/users")
                        .permitAll())
                .oauth2Login(config -> config
                        .loginPage("/login")
                        .defaultSuccessUrl("/users")
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(userRequest -> {
                                    String email = userRequest.getIdToken().getClaim("email");
                                    //todo create new user -> userService.create
                                    UserDetails userDetails = userService.loadUserByUsername(email);
                                    var oidcUser = new DefaultOidcUser(userDetails.getAuthorities(), userRequest.getIdToken());
                                    var methods = Set.of(userDetails.getClass().getMethods());

                                    return (OidcUser) Proxy.newProxyInstance(
                                            SecurityConfiguration.class.getClassLoader(),
                                            new Class[]{UserDetails.class, OidcUser.class},
                                            (proxy, method, args) -> methods.contains(method)
                                                    ? method.invoke(userDetails, args)
                                                    : method.invoke(oidcUser, args));

//                            return new OidcUserService().loadUser(userRequest);

                                }
                        )));
        return http.build();
    }

//    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService(){
//        return userRequest -> {
//            return new DefaultOidcUser(null, null);
//        }
//    }

}