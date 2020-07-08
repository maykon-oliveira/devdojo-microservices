package com.maykonoliveira.course.endpoint.service;

import com.maykonoliveira.core.entities.Course;
import com.maykonoliveira.core.repository.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * @author maykon-oliveira
 */

@Service
@AllArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    public Iterable<Course> list(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }
}
