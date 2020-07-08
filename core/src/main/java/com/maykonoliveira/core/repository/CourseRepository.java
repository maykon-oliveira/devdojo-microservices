package com.maykonoliveira.core.repository;

import com.maykonoliveira.core.entities.Course;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author maykon-oliveira
 */

public interface CourseRepository extends PagingAndSortingRepository<Course, Long> {
}
