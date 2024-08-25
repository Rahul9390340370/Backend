package com.example.courses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instances")
public class CourseInstanceController {

    @Autowired
    private CourseInstanceRepository courseInstanceRepository;

    @PostMapping
    public ResponseEntity<CourseInstance> createInstance(@RequestBody CourseInstance instance) {
        CourseInstance createdInstance = courseInstanceRepository.save(instance);
        return ResponseEntity.ok(createdInstance);
    }

    @GetMapping
    public List<CourseInstance> getAllInstances() {
        return courseInstanceRepository.findAll();
    }

    @GetMapping("/{year}/{semester}")
    public List<CourseInstance> getInstancesByYearAndSemester(@PathVariable int year, @PathVariable int semester) {
        return courseInstanceRepository.findByYearAndSemester(year, semester);
    }

    @GetMapping("/{year}/{semester}/{courseId}")
    public ResponseEntity<CourseInstance> getInstance(@PathVariable int year, @PathVariable int semester, @PathVariable Long courseId) {
        List<CourseInstance> instances = courseInstanceRepository.findByCourseId(courseId);
        return instances.stream()
                .filter(instance -> instance.getYear() == year && instance.getSemester() == semester)
                .findFirst()
                .map(instance -> ResponseEntity.ok().body(instance))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{year}/{semester}/{courseId}")
    public ResponseEntity<Object> deleteInstance(@PathVariable int year, @PathVariable int semester, @PathVariable Long courseId) {
        List<CourseInstance> instances = courseInstanceRepository.findByCourseId(courseId);
        return instances.stream()
                .filter(instance -> instance.getYear() == year && instance.getSemester() == semester)
                .findFirst()
                .map(instance -> {
                    courseInstanceRepository.deleteById(instance.getId());
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
