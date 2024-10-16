package gr.aueb.cf.teacherapp.mapper;

import gr.aueb.cf.teacherapp.core.enums.Role;
import gr.aueb.cf.teacherapp.dto.TeacherInsertDTO;
import gr.aueb.cf.teacherapp.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.teacherapp.dto.UserInsertDTO;
import gr.aueb.cf.teacherapp.model.Teacher;
import gr.aueb.cf.teacherapp.model.User;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public Teacher mapToTeacherEntity(TeacherInsertDTO insertDTO) {
        Teacher teacher = new Teacher();
        teacher.setFirstname(insertDTO.getFirstname());
        teacher.setLastname(insertDTO.getLastname());
        teacher.setVat(insertDTO.getVat());
        return teacher;
    }

    public TeacherReadOnlyDTO mapToTeacherReadOnlyDTO(Teacher teacher) {
        return new TeacherReadOnlyDTO(teacher.getId(), teacher.getCreatedAt(), teacher.getUpdatedAt(),
                teacher.getUuid(), teacher.getFirstname(), teacher.getLastname(),
                teacher.getVat(), teacher.getRegion().getName());
    }

    public User mapToUserEntity(UserInsertDTO insertDTO) {
        return new User(null, insertDTO.getUsername(), insertDTO.getPassword(),
                Role.valueOf(insertDTO.getRole().toUpperCase()));
    }
}
