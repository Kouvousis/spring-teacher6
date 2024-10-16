package gr.aueb.cf.teacherapp.service;

import gr.aueb.cf.teacherapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.teacherapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.teacherapp.dto.TeacherInsertDTO;
import gr.aueb.cf.teacherapp.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.teacherapp.mapper.Mapper;
import gr.aueb.cf.teacherapp.model.Teacher;
import gr.aueb.cf.teacherapp.model.static_data.Region;
import gr.aueb.cf.teacherapp.repository.RegionRepository;
import gr.aueb.cf.teacherapp.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService implements ITeacherService {

    private final TeacherRepository teacherRepository;
    private final RegionRepository regionRepository;
    private final Mapper mapper;

    @Override
    @Transactional
    public Page<TeacherReadOnlyDTO> getPaginatedTeachers(int page, int size) {
        // Create a Pageable object with the requested page number and size
        Pageable pageable = PageRequest.of(page, size);

        // Fetch paginated list of teachers
        Page<Teacher> teacherPage = teacherRepository.findAll(pageable);

        // Map each Teacher entity to TeacherReadOnlyDTO
        return teacherPage.map(mapper::mapToTeacherReadOnlyDTO);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Teacher saveTeacher(TeacherInsertDTO insertDTO)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException {

        if (teacherRepository.findByVat(insertDTO.getVat()).isPresent()) {
            throw new EntityAlreadyExistsException("Teacher", "Teacher with vat: " + insertDTO.getVat() + " already exists");
        }
        Teacher teacher = mapper.mapToTeacherEntity(insertDTO);

        Region region = regionRepository.findById(insertDTO.getRegionId())
                .orElseThrow(() -> new EntityInvalidArgumentException("Region", "Invalid region id."));
        teacher.setRegion(region);

        return teacherRepository.save(teacher);
    }
}
