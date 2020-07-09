package com.maykonoliveira.auth.security.config;

import com.maykonoliveira.auth.security.filter.JwtAuthenticationFilter;
import com.maykonoliveira.core.properties.JwtConfiguration;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/** @author maykon-oliveira */
@EnableWebSecurity
@AllArgsConstructor
public class SecurityCredentialsSecurity extends WebSecurityConfigurerAdapter {
  private final JwtConfiguration jwtConfiguration;
  private final UserDetailsService userDetailsService;

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
        .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtConfiguration))
        .authorizeRequests()
        .antMatchers(jwtConfiguration.getLoginUrl())
        .permitAll()
        .antMatchers("/admin/courses/**")
        .hasRole("ADMIN")
        .anyRequest()
        .authenticated();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
