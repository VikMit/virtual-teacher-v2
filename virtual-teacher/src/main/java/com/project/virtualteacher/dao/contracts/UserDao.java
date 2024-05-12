package com.project.virtualteacher.dao.contracts;


import com.project.virtualteacher.entity.Student;
import com.project.virtualteacher.entity.Teacher;
import com.project.virtualteacher.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao {
    Optional<User> findByUsename(String username);

    void create(User user);

    Optional<User> findById(int userId);

    void delete(User user);

    void update(User user);

    boolean isEmailExist(String email);

    boolean isUsernameExist(String username);

    void block(int userId);

    void unblock(int id);

    void verifyEmail(String code);

    Optional<Student> findStudentById(int studentId);


     Optional<Teacher> findTeacherById(int  teacherId);
}
