package com.yadev.spring.config;

import com.yadev.spring.http.handler.CustomAccessDeniedHandler;
import com.yadev.spring.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.Set;

import static com.yadev.spring.database.entity.Role.ADMIN;

@Configuration
//@EnableMethodSecurity
@EnableWebSecurity
//@EnableGlobalMethodSecurity(
//        prePostEnabled = true,
//        securedEnabled = true,
//        jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
//                .csrf().disable()
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(
                                "/login",
                                "/users/registration",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/login/oauth2/code/**",
                                "/login/oauth2/code/*"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/users",
                                "/login",
                                "/login/oauth2/code/*"
                        ).permitAll()
//                        .requestMatchers("/users/{\\d+}/delete").hasAuthority(ADMIN.getAuthority())
                        .requestMatchers("/admin/**").hasAuthority(ADMIN.getAuthority())
                        .anyRequest().authenticated()
                )
                //                .httpBasic(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID"))
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/users")
                        .permitAll())
                .oauth2Login(config -> config
                        .loginPage("/login")
                        .defaultSuccessUrl("/users")
                        .failureUrl("/failureUrl")
                        .failureHandler(new AuthenticationFailureHandler() {
                            @Override
                            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                                var authentication = SecurityContextHolder.getContext().getAuthentication();
                                if (authentication != null){
                                    System.out.println("User '" + authentication.getName() +
                                            "' attempted to access the URL: " +
                                            request.getRequestURI());
                                }
                                response.sendRedirect(request.getContextPath() + "/access-denied");
                            }
                        })
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(userRequest -> {
                                    String email = userRequest.getIdToken().getClaim("email");
                                    //todo create new user -> userService.create
                                    UserDetails userDetails = userService.loadUserByUsername(email);

                                    var oidcUser = new DefaultOidcUser(userDetails.getAuthorities(), userRequest.getIdToken());
                                    var userDetailsMethods = Set.of(UserDetails.class.getMethods());

                                    return (OidcUser) Proxy.newProxyInstance(SecurityConfiguration.class.getClassLoader(),
                                            new Class[]{UserDetails.class, OidcUser.class},
                                            (proxy, method, args) -> userDetailsMethods.contains(method)
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