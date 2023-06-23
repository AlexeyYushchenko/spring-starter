package com.yadev.spring.config;

import com.yadev.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import java.lang.reflect.Proxy;
import java.util.Set;

import static com.yadev.spring.database.entity.Role.ADMIN;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(
                                "/login",
                                "/users/registration",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/users"
                        ).permitAll()
                        .requestMatchers(
                                RegexRequestMatcher.regexMatcher(
                                        HttpMethod.POST,
                                        "/users/\\d+/delete")).hasAuthority(ADMIN.getAuthority())
                        .requestMatchers(
                                "/admin/**",
                                "/swagger-ui/**").hasAuthority(ADMIN.getAuthority())
                        .anyRequest().authenticated()
                )
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