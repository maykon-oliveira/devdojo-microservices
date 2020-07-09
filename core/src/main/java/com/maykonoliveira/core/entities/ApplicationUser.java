package com.maykonoliveira.core.entities;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/** @author maykon-oliveira */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ApplicationUser implements AbstractEntity {
  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull private String username;
  private String password;
  private String role = "USER";

  public ApplicationUser(ApplicationUser applicationUser) {
    this.password = applicationUser.getPassword();
    this.username = applicationUser.getUsername();
    this.role = applicationUser.getRole();
  }
}
