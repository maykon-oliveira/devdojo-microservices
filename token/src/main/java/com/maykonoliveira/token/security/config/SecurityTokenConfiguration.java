package com.maykonoliveira.token.security.config;

import com.maykonoliveira.core.properties.JwtConfiguration;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/** @author maykon-oliveira */
@AllArgsConstructor
public class SecurityTokenConfiguration extends WebSecurityConfigurerAdapter {
  protected final JwtConfiguration jwtConfiguration;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .cors()
        .configurationSource(r -> new CorsConfiguration().applyPermitDefaultValues())
        .and()
        .sessionManagement()
        .sessionCreationPolicy(STATELESS)
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(
            (req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
        .and()
        .authorizeRequests()
        .antMatchers(jwtConfiguration.getLoginUrl(), "/**/swagger-ui.html")
        .permitAll()
        .antMatchers(
            HttpMethod.GET,
            "/**/swagger-resources/**",
            "/**/webjars/springfox-swagger-ui/**",
            "/**/v2/api-docs/**")
        .permitAll()
        .antMatchers("/course/v1/admin/**")
        .hasRole("ADMIN")
        .antMatchers("/auth/v1/users/me")
        .hasAnyRole("ADMIN", "USER")
        .anyRequest()
        .authenticated();
  }
}
