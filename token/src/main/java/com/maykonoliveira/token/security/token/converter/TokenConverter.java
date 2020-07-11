package com.maykonoliveira.token.security.token.converter;

import com.maykonoliveira.core.properties.JwtConfiguration;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

/** @author maykon-oliveira */
@Slf4j
@Service
@AllArgsConstructor
public class TokenConverter {
  private final JwtConfiguration jwtConfiguration;

  @SneakyThrows
  public String decryptToken(String encrypterToken) {
    final JWEObject jweObject = JWEObject.parse(encrypterToken);

    final DirectDecrypter decrypter =
        new DirectDecrypter(jwtConfiguration.getPrivateKey().getBytes());

    jweObject.decrypt(decrypter);

    return jweObject.getPayload().toSignedJWT().serialize();
  }

  @SneakyThrows
  public void validateTokenSignature(String signedToken) {
    final SignedJWT signedJWT = SignedJWT.parse(signedToken);

    final RSAKey rsaKey = RSAKey.parse(signedJWT.getHeader().getJWK().toJSONString());

    if (!signedJWT.verify(new RSASSAVerifier(rsaKey))) {
      throw new AccessDeniedException("");
    }
  }
}
