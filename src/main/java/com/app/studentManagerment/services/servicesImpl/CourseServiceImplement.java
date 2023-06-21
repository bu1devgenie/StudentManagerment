package com.app.studentManagerment.services.servicesImpl;

import com.app.studentManagerment.dao.ClassroomRepository;
import com.app.studentManagerment.dao.CourseRepository;
import com.app.studentManagerment.entity.ClassRoom;
import com.app.studentManagerment.entity.Course;
import com.app.studentManagerment.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImplement implements CourseService {

    private CourseRepository courseRepository;
    private ClassroomRepository classroomRepository;

    @Autowired
    public CourseServiceImplement(CourseRepository courseRepository, ClassroomRepository classroomRepository) {
        this.courseRepository = courseRepository;
        this.classroomRepository = classroomRepository;
    }

    @Override
    public List<String> getAllNameOfCourse() {
        return courseRepository.getAllNameOfCourse();
    }

    @Override
    public List<Course> getAllCourse() {
        return courseRepository.findAll();
    }

    @Override
    public Course updateCourse(long id, String name, int totalSlot, int courseSemester) {
        Course course = courseRepository.findById(id);
        List<ClassRoom> classRooms = classroomRepository.findByCourse(course);
        for (ClassRoom classRoom : classRooms) {
            if (classRoom.isLearning()) {
                return course;
            }
        }
        if (course == null) {
            return null;
        }
        if (!name.isEmpty()) {
            course.setName(name);
        }
        if (totalSlot > 0) {
            course.setTotalSlot(totalSlot);
        }
        if (courseSemester > 0 && courseSemester <= 10) {
            course.setCourseSemester(courseSemester);
        }
        return courseRepository.save(course);
    }

    @Override
    public Boolean shutdownCourse(long id) {
        Course course = courseRepository.findById(id);
        if (course != null && course.isActivity()) {
            List<ClassRoom> classRooms = classroomRepository.findByCourse(course);
            for (ClassRoom classRoom : classRooms) {
                if (classRoom.isLearning()) {
                    return false;
                }
            }
            course.setActivity(false);
            return courseRepository.save(course).isActivity();
        }
        return false;
    }

    @Override
    public Course addCourse(String name, int totalSlot, int courseSemester, boolean activity) {
        Course course = courseRepository.findByName(name);
        if (course != null) {
            return null;
        }
        course.setName(name);
        course.setTotalSlot(totalSlot);
        course.setActivity(activity);
        course.setCourseSemester(courseSemester);
        return courseRepository.save(course);
    }

    @Override
    public List<Course> findCourseWithCurrentSemester(int currentSemester) {
        return courseRepository.findAllByCourseSemester(currentSemester);
    }


}
