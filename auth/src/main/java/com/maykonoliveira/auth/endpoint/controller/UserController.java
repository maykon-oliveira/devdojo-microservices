package com.maykonoliveira.auth.endpoint.controller;

import com.maykonoliveira.core.entities.ApplicationUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** @author maykon-oliveira */
@RestController
@RequestMapping("v1/users")
@AllArgsConstructor
public class UserController {
  @GetMapping("me")
  public ResponseEntity<ApplicationUser> me(Authentication authentication) {
    final ApplicationUser applicationUser = (ApplicationUser) authentication.getPrincipal();

    return ResponseEntity.ok(applicationUser);
  }
}
