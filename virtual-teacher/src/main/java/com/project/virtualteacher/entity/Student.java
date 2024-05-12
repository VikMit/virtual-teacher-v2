package com.project.virtualteacher.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("1")
@Data
public class Student extends User{

    @ManyToMany(mappedBy = "enrolledStudents")
    private Set<Course> enrolledCourses;
}
