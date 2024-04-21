package fr.anthonyquere.talkwithme.core.adapters.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
      .addMapping("/**")
      .allowedMethods("GET", "POST", "OPTIONS");
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    registry.addViewController("/admin")
      .setViewName("forward:/index.html");

    registry.addViewController("/admin/")
      .setViewName("forward:/index.html");


    // Single directory level
    registry.addViewController("/admin/{x:[\\w\\-]+}")
      .setViewName("forward:/index.html");
    // Multi-level directory path
    registry.addViewController("/admin/[\\w\\-]+/[\\w\\-]+")
      .setViewName("forward:/index.html");
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.setOrder(Ordered.LOWEST_PRECEDENCE);

    registry
      .addResourceHandler(
        "/admin/**"
      ).addResourceLocations("classpath:/static/");


    registry
      .addResourceHandler(
        "/neighbors/**"
      ).addResourceLocations("classpath:/neighbors/");
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //  Decrypt jwt bearer tokens from the Authorization request header
    http
      .authorizeHttpRequests(c -> c.anyRequest().permitAll())
      .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
    return http.build();
  }

  @Bean
  public JwtDecoder jwtDecoder() {

    return NimbusJwtDecoder.withIssuerLocation("https://talk-with-me.eu.auth0.com/").build();
  }
}
