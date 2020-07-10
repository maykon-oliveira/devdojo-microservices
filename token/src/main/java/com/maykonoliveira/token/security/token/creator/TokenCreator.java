package com.maykonoliveira.token.security.token.creator;

import com.maykonoliveira.core.entities.ApplicationUser;
import com.maykonoliveira.core.properties.JwtConfiguration;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

/** @author maykon-oliveira */
@Slf4j
@Service
@AllArgsConstructor
public class TokenCreator {
  private final JwtConfiguration jwtConfiguration;

  @SneakyThrows
  public SignedJWT createSignedJWT(Authentication authentication) {
    log.info("createSignedJWT");

    final ApplicationUser principal = (ApplicationUser) authentication.getPrincipal();

    final JWTClaimsSet jwtClaimsSet = createJWTClaimsSet(authentication, principal);

    final KeyPair rsaKeys = generateKeyPair();

    log.info("JWK from RSA");

    final JWK jwk =
        new RSAKey.Builder((RSAPublicKey) rsaKeys.getPublic())
            .keyID(UUID.randomUUID().toString())
            .build();

    final SignedJWT signedJWT =
        new SignedJWT(
            new JWSHeader.Builder(JWSAlgorithm.RS256).jwk(jwk).type(JOSEObjectType.JWT).build(),
            jwtClaimsSet);

    log.info("Assigning token");

    final RSASSASigner rsassaSigner = new RSASSASigner(rsaKeys.getPrivate());
    signedJWT.sign(rsassaSigner);

    log.info("JWT '{}'", signedJWT.serialize());

    return signedJWT;
  }

  private JWTClaimsSet createJWTClaimsSet(
      Authentication authentication, ApplicationUser applicationUser) {
    log.info("createJWTClaimsSet to '{}'", applicationUser.getUsername());

    return new JWTClaimsSet.Builder()
        .subject(applicationUser.getUsername())
        .claim(
            "authorities",
            authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()))
        .issuer("http://localhost:8080")
        .issueTime(new Date())
        .expirationTime(
            new Date(System.currentTimeMillis() + (jwtConfiguration.getExpiration() * 1000)))
        .build();
  }

  @SneakyThrows
  private KeyPair generateKeyPair() {
    final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(2048);

    return generator.genKeyPair();
  }

  @SneakyThrows
  public String encryptToken(SignedJWT signedJWT) {
    log.info("Encrypting");
    final DirectEncrypter encrypter =
        new DirectEncrypter(jwtConfiguration.getPrivateKey().getBytes());
    final JWEObject jwt =
        new JWEObject(
            new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256)
                .contentType("JWT")
                .build(),
            new Payload(signedJWT));

    jwt.encrypt(encrypter);
    return jwt.serialize();
  }
}
