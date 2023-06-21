package com.app.studentManagerment.services.servicesImpl;

import com.app.studentManagerment.dao.ScheduleRepository;
import com.app.studentManagerment.dao.SemesterRepository;
import com.app.studentManagerment.dao.SlotRepository;
import com.app.studentManagerment.entity.*;
import com.app.studentManagerment.entity.user.Teacher;
import com.app.studentManagerment.services.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private CourseService courseService;
    private TeacherService teacherService;
    private StudentService studentService;
    private RoomServices roomServices;
    private ClassRoomService classRoomService;
    private SemesterRepository semesterRepository;
    private ScheduleRepository scheduleRepository;
    private SlotRepository slotRepository;

    public ScheduleServiceImpl(CourseService courseService, TeacherService teacherService, StudentService studentService, RoomServices roomServices, ClassRoomService classRoomService, SemesterRepository semesterRepository, ScheduleRepository scheduleRepository, SlotRepository slotRepository) {
        this.courseService = courseService;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.roomServices = roomServices;
        this.classRoomService = classRoomService;
        this.semesterRepository = semesterRepository;
        this.scheduleRepository = scheduleRepository;
        this.slotRepository = slotRepository;
    }

    @Async
    @Override
    public String autoGenerateSchedule(String semesterName) {
        Semester semester = semesterRepository.findByName(semesterName);
        int[][] tempDayOfWeak = {{2, 4, 6}, {2, 4}, {4, 6}, {3, 5}};
        if (semester != null) {
            for (int i = 1; i <= 10; i++) {
                List<Course> courses = courseService.findCourseWithCurrentSemester(i);
                // duyệt các môn học theo từng kì học

                for (Course course : courses) {
                    int totalStudents = studentService.totalStudentWithCurrentSemester(i);
                    int totalClassSize = totalStudents / 35;
                    if (totalStudents % 35 != 0) {
                        totalClassSize += 1;
                    }
                    int defaultSlot = 1;
                    // tạo ra các lớp theo số lượng sinh viên
                    for (int j = 0; j <= totalClassSize; j++) {
                        // tạo lớp
                        ClassRoom classRoom = classRoomService.newClassRoom(semester, null, course, 0, true, null);
                        // tạo lớp xong
                        // lấy slot trong tuần cho lịch
                        Pageable pageable = PageRequest.of(0, 3);
                        int indexTempDay = (j % tempDayOfWeak.length + tempDayOfWeak.length) % tempDayOfWeak.length;
                        Page<Slot> slots = null;
                        if (indexTempDay == 0) {
                            slots = slotRepository.findSlotCase246(defaultSlot, pageable);
                        } else {
                            int[] day_of_week = tempDayOfWeak[indexTempDay];
                            slots = slotRepository.findSlotOtherCase(day_of_week, new int[]{defaultSlot, defaultSlot + 1}, pageable);
                        }
                        // lấy slot xong
                        // tạo mới lịch cho class
                        Schedule schedule = new Schedule();
                        schedule.setClassRoom(classRoom);
                        schedule.setSemester(semester);
                        if (!slots.getContent().isEmpty()) {
                            for (Slot s : slots.getContent().stream().toList()) {
                                s.setStatus(Slot.SlotStatus.RESERVED);
                            }
                            schedule.setSlot(slots.getContent().stream().toList());
                        }
                        Teacher teacher = teacherService.getAllTeacherCanTakeClasses(course, semester, schedule.getSlot());
                        schedule.getClassRoom().setTeacher(teacher);
                        scheduleRepository.save(schedule);
                        ////
                        if (defaultSlot + 1 <= 5) {
                            defaultSlot++;
                        } else {
                            defaultSlot = 1;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Page<Schedule> allSchedule() {
        Pageable pageable = PageRequest.of(0, 10);
        return scheduleRepository.findAll(pageable);
    }
}
