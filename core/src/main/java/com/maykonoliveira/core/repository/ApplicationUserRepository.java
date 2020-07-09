package com.maykonoliveira.core.repository;

import com.maykonoliveira.core.entities.ApplicationUser;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/** @author maykon-oliveira */
public interface ApplicationUserRepository
    extends PagingAndSortingRepository<ApplicationUser, Long> {
  public Optional<ApplicationUser> findByUsername(String username);
}
