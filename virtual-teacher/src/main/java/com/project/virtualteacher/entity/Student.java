/*
package com.project.virtualteacher.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Entity
@DiscriminatorValue("1")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Student extends User {

    @OneToMany()
    @JoinTable(name = "user_course", joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"))
    private Set<Course> enrolledCourses;
}
*/
