package com.maykonoliveira.token.security.util;

import com.maykonoliveira.core.entities.ApplicationUser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

/** @author maykon-oliveira */
public class SecurityContextUtil {
  private SecurityContextUtil() {}

  public static void setSecurityContext(SignedJWT signedJWT) {
    try {
      JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
      String username = claimsSet.getSubject();
      if (username == null) throw new JOSEException("");
      List<String> authorities = claimsSet.getStringListClaim("authorities");
      Long userId = claimsSet.getLongClaim("userId");

      ApplicationUser applicationUser =
          ApplicationUser.builder()
              .username(username)
              .id(userId)
              .role(String.join(",", authorities))
              .build();

      final UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(
              applicationUser, null, createGrantedAuthorities(authorities));

      authenticationToken.setDetails(signedJWT.serialize());
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    } catch (Exception e) {
      e.printStackTrace();
      SecurityContextHolder.clearContext();
    }
  }

  private static List<SimpleGrantedAuthority> createGrantedAuthorities(List<String> authorities) {
    return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }
}
