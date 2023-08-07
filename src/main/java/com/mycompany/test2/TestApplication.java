package com.mycompany.test2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
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
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.sl.palabras.services.LanguageService;

import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import lombok.extern.log4j.Log4j2;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;

import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@ImportResource("file:application-config.xml")
// @EnableWebMvc
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableAsync
@EnableScheduling
@ComponentScan({ "com.mycompany.test2", "com.sl.palabras.services.impl" })
@EntityScan({ "com.mycompany.test2.db", "com.sl.palabras.entities" })
@EnableJpaRepositories(basePackages = { "com.mycompany.test2.db", "com.sl.palabras.repositories" })
@EnableJpaAuditing
@EnableCaching
@SecuritySchemes({
        // @SecurityScheme(name = "session", type = SecuritySchemeType.DEFAULT),
        @SecurityScheme(name = "basic", type = SecuritySchemeType.HTTP, scheme = "basic"),
        @SecurityScheme(name = "token", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER, paramName = "X-Token", scheme = "token"),
})
@Log4j2
// @EnableAdminServer
public class TestApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(TestApplication.class, args);
        log.info("inject data");
        var languageService = context.getBean(LanguageService.class);
        languageService.addDefaultLanguages();
    }

    // @Bean
    // public CommonsRequestLoggingFilter logFilter() {
    // var filter = new CommonsRequestLoggingFilter();
    // filter.setIncludeClientInfo(true);
    // filter.setIncludeQueryString(true);
    // filter.setIncludeHeaders(true);
    // filter.setIncludePayload(true);
    // filter.setMaxPayloadLength(10000);
    // return filter;
    // }

    // @Bean
    // public Executor getAsyncExecutor() {
    // return new
    // DelegatingSecurityContextExecutorService(Executors.newFixedThreadPool(5));
    // }

    @Bean
    @Profile("!jpa-test")
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
            throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector).servletPath("/");
        http.authorizeHttpRequests(requests -> requests
                // .requestMatchers("/swagger-ui.html", "/swagger-ui/**",
                // "/v3/api-docs/**").permitAll()
                // .requestMatchers("/api/v1/management/status").permitAll()
                // .requestMatchers("/api/v1/applications").permitAll()
                // .requestMatchers("/api/v1/subscriptions").hasRole("ADMIN")
                // .requestMatchers("/api").authenticated()
                // .requestMatchers("/api").permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/admin/**")).hasRole("ADMIN")
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