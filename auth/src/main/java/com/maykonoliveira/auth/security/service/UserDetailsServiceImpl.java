package com.maykonoliveira.auth.security.service;

import com.maykonoliveira.core.entities.ApplicationUser;
import com.maykonoliveira.core.repository.ApplicationUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/** @author maykon-oliveira */
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private final ApplicationUserRepository applicationUserRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    final Optional<ApplicationUser> applicationUserOptional =
        applicationUserRepository.findByUsername(username);

    final ApplicationUser applicationUser =
        applicationUserOptional.orElseThrow(
            () -> {
              throw new UsernameNotFoundException(String.format("User %s not found", username));
            });

    return new CustomUserDetail(applicationUser);
  }

  private static final class CustomUserDetail extends ApplicationUser implements UserDetails {
    public CustomUserDetail(ApplicationUser applicationUser) {
      super(applicationUser);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_" + getRole());
    }

    @Override
    public boolean isAccountNonExpired() {
      return true;
    }

    @Override
    public boolean isAccountNonLocked() {
      return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
      return true;
    }

    @Override
    public boolean isEnabled() {
      return true;
    }
  }
}
