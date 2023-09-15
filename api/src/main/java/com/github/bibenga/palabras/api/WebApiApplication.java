package com.github.bibenga.palabras.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;

@SpringBootApplication
// @EnableAsync
// @EnableScheduling
// @EnableCaching
@EntityScan({ "com.github.bibenga.palabras.entities" })
@EnableJpaRepositories(basePackages = { "com.github.bibenga.palabras.repositories" })
@EnableJpaAuditing
@ComponentScan({ "com.github.bibenga.palabras.api.controllers" })
@EnableWebMvc
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@SecuritySchemes({
        // @SecurityScheme(name = "session", type = SecuritySchemeType.DEFAULT),
        @SecurityScheme(name = "basic", type = SecuritySchemeType.HTTP, scheme = "basic"),
        @SecurityScheme(name = "token", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER, paramName = "X-Token", scheme = "token"),
})
public class WebApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApiApplication.class, args);
    }

    // @Bean
    // public Executor getAsyncExecutor() {
    // return new
    // DelegatingSecurityContextExecutorService(Executors.newFixedThreadPool(5));
    // }

    @Bean
    @Profile("!jpa-test")
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
            throws Exception {
        // MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector).servletPath("/");
        http.authorizeHttpRequests(requests -> requests
                // .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // .requestMatchers("/api/v1/management/status").permitAll()
                // .requestMatchers("/api/v1/applications").permitAll()
                // .requestMatchers("/api/v1/subscriptions").hasRole("ADMIN")
                // .requestMatchers("/api").authenticated()
                // .requestMatchers("/api").permitAll()
                // .requestMatchers(mvcMatcherBuilder.pattern("/admin/**")).hasRole("ADMIN")
                .anyRequest().permitAll());
        http.formLogin(form -> form.permitAll());
        http.logout(logout -> logout.permitAll());
        http.httpBasic(t -> {
        });
        http.csrf(t -> {
            t.disable();
        });
        // http.csrf(csrf -> csrf
        // .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        // .csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler()));
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        var admin = User.withUsername("a")
                .password(encoder.encode("a"))
                .roles("ADMIN")
                .build();

        var user = User.withUsername("u")
                .password(encoder.encode("u"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}