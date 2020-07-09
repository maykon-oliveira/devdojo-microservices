package com.maykonoliveira.auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class AuthApplicationTests {

  @Test
  void contextLoads() {
    System.out.println(new BCryptPasswordEncoder().encode("12345"));
    //    $2a$10$794xuCqWtvCI3OjZDVcI1.YVnLU4nmBBTp9omm9P2EYyM2x/s9IHK
  }
}
