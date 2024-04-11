package com.github.bibenga.palabras.api;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@EnableAsync
@EnableScheduling
// @EnableCaching
@EntityScan({ "com.github.bibenga.palabras.entities" })
@EnableJpaRepositories(basePackages = { "com.github.bibenga.palabras.repositories" })
@EnableJpaAuditing
// @EnableTransactionManagement
@ComponentScan({ "com.github.bibenga.palabras.api.controllers" })
@EnableWebMvc
@EnableWebSocket
@EnableWebSecurity
@EnableWebSocketSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@SecuritySchemes({
        @SecurityScheme(name = "firestore", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer"),
// @SecurityScheme(name = "basic", type = SecuritySchemeType.HTTP, scheme =
// "basic"),
// @SecurityScheme(name = "token", type = SecuritySchemeType.APIKEY, in =
// SecuritySchemeIn.HEADER, paramName = "X-Token", scheme = "token"),
})
@Log4j2
public class WebApiApplication implements WebSocketConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(WebApiApplication.class, args);
    }

    // @Bean
    // public Executor getAsyncExecutor() {
    // return new
    // DelegatingSecurityContextExecutorService(Executors.newFixedThreadPool(5));
    // }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
            throws Exception {
        // MvcRequestMatcher.Builder mvcMatcherBuilder = new
        // MvcRequestMatcher.Builder(introspector).servletPath("/");
        http.authorizeHttpRequests(requests -> requests
                // .requestMatchers("/swagger-ui.html", "/swagger-ui/**",
                // "/v3/api-docs/**").permitAll()
                // .requestMatchers("/api/v1/management/status").permitAll()
                // .requestMatchers("/api/v1/applications").permitAll()
                // .requestMatchers("/api/v1/subscriptions").hasRole("ADMIN")
                // .requestMatchers("/api").authenticated()
                // .requestMatchers("/api").permitAll()
                // .requestMatchers("/ws").permitAll()
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

        // http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        http.oauth2ResourceServer(
                oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    @Bean
    AuthorizationManager<Message<?>> messageAuthorizationManager(
            MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        messages.simpDestMatchers("/ws").permitAll();
        return messages.build();
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }

    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(
                jwt -> Optional.ofNullable(jwt.getClaimAsStringList("role"))
                        .stream()
                        .flatMap(Collection::stream)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()));
        return converter;
    }

    // @Bean
    // public AuthenticationEventPublisher authenticationEventPublisher(
    //         ApplicationEventPublisher applicationEventPublisher) {
    //     return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    // }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        log.info("onSuccess -> {}", success);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        log.info("registerWebSocketHandlers");
        registry.addHandler(wsHandler(), "/ws").setAllowedOrigins("*")
                .addInterceptors(new HttpSessionHandshakeInterceptor());
    }

    @Bean
    public WebSocketHandler wsHandler() {
        return new WSHandler();
    }
}
