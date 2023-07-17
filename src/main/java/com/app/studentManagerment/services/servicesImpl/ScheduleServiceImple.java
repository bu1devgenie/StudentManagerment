package com.app.studentManagerment.services.servicesImpl;

import com.app.studentManagerment.dao.*;
import com.app.studentManagerment.dto.RoomAndSlotCanTakeClass;
import com.app.studentManagerment.entity.*;
import com.app.studentManagerment.entity.user.Teacher;
import com.app.studentManagerment.enumPack.enumRole;
import com.app.studentManagerment.enumPack.enumStatus;
import com.app.studentManagerment.services.ClassRoomService;
import com.app.studentManagerment.services.RequestService;
import com.app.studentManagerment.services.ScheduleService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class ScheduleServiceImple implements ScheduleService {
    private RequestService requestService;
    private SemesterRepository semesterRepository;
    private CourseRepository courseRepository;
    private StudentRepository studentRepository;
    private ClassroomRepository classroomRepository;
    private ClassRoomService classRoomService;
    private SessionRepository sessionRepository;
    private RoomRepository roomRepository;
    private TeacherRepository teacherRepository;
    private EmailServiceImpl emailService;
    private MailProRepository mailProRepository;

    public ScheduleServiceImple(RequestService requestService, SemesterRepository semesterRepository, CourseRepository courseRepository, StudentRepository studentRepository, ClassroomRepository classroomRepository, ClassRoomService classRoomService, SessionRepository sessionRepository, RoomRepository roomRepository, TeacherRepository teacherRepository, EmailServiceImpl emailService, MailProRepository mailProRepository) {
        this.requestService = requestService;
        this.semesterRepository = semesterRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.classroomRepository = classroomRepository;
        this.classRoomService = classRoomService;
        this.sessionRepository = sessionRepository;
        this.roomRepository = roomRepository;
        this.teacherRepository = teacherRepository;
        this.emailService = emailService;
        this.mailProRepository = mailProRepository;
    }

    int[][] dayClass = {{2, 4, 6}, {2, 4}, {4, 6}, {3, 5}};

    @Transactional
    @Async
    @Override
    public void autoGenerateSchedule(String semesterName, Requests requests) {
        requestService.changeStatus(requests, enumStatus.PROCESSING);
        Semester semester = semesterRepository.findByName(semesterName);
        // kiểm tra semester có tồn tại không
        if (semester == null) {
            requestService.changeStatus(requests, enumStatus.UNPROCESSED);
        }
        for (int i = 0; i <= 10; i++) {
            List<Course> courses = courseRepository.findAllByCourseSemester(i);
            int totalStu = studentRepository.countStudentByCurrentSemester(i);
            int totalClass = totalStu % 35 == 0 ? totalStu / 35 : totalStu / 35 + 1;
            // duyệt qua các course theo kì học của sinh viên
            for (Course c : courses) {
                int start_slot = 1;
                for (int j = 1; j <= totalClass; j++) {
                    // tạo lớp
                    ClassRoom classRoom = classRoomService.newClassRoom(semester, null, c, 0, false, null);
                    classRoom = classroomRepository.save(classRoom);
                    //tạo lớp xong
                    // lấy phòng cho lớp
                    if (j % dayClass.length == 0) {
                        int[] sub_dayClass = dayClass[0];
                        RoomAndSlotCanTakeClass room = roomRepository.getRoomCanTakeClassFor246(start_slot, semester.getStart_date(), semester.getEnd_date()).get(0);
                        int secondSlotAt = room.isSecondSlotAt2() ? 2 : (room.isSecondSlotAt4() ? 4 : (room.isSecondSlotAt6() ? 6 : 0));
                        // tạo session cho lớp với tổng số buổi học
                        int checkTotal = 0;
                        for (LocalDate start = semester.getStart_date(); start.isBefore(semester.getEnd_date()); start = start.plusDays(1)) {
                            int day_of_week = start.getDayOfWeek().getValue();
                            if (Arrays.stream(sub_dayClass).anyMatch(day -> day == day_of_week) && checkTotal <= c.getTotalSlot()) {
                                //bắt đầu tạo session
                                Session s1 = new Session();
                                s1.setClassRoom(classRoom);
                                s1.setRoom(roomRepository.findById(room.getRoom_id()).get());
                                s1.setSesionDate(start);
                                s1.setSlotOfDay(start_slot);
                                sessionRepository.save(s1);
                                checkTotal++;
                                // nếu ngày đấy có 2 slot thì tạo thêm slot thứ 2 ngay sau slot đầu tiên ngay sau slot đấy
                                if (checkTotal < c.getTotalSlot() && secondSlotAt == day_of_week && checkTotal <= c.getTotalSlot()) {
                                    Session s2 = new Session();
                                    s2.setClassRoom(classRoom);
                                    s2.setRoom(roomRepository.findById(room.getRoom_id()).get());
                                    s2.setSesionDate(start);
                                    s2.setSlotOfDay(start_slot + 1);
                                    sessionRepository.save(s2);
                                    checkTotal++;
                                }
                            }
                        }
                    } else {
                        int[] sub_dayClass = dayClass[j % dayClass.length];
                        // lấy phòng có thể chứa lớp đó
                        Room r = roomRepository.getRoomCanTakeClassOtherCase(start_slot, semester.getStart_date(), semester.getEnd_date(), dayClass[j % dayClass.length]);
                        int checkTotal = 0;
                        for (LocalDate start = semester.getStart_date(); start.isBefore(semester.getEnd_date()); start = start.plusDays(1)) {
                            int day_of_week = start.getDayOfWeek().getValue();
                            if (Arrays.stream(sub_dayClass).anyMatch(day -> day == day_of_week) && checkTotal <= c.getTotalSlot()) {
                                //bắt đầu tạo session
                                Session s1 = new Session();
                                s1.setClassRoom(classRoom);
                                s1.setRoom(r);
                                s1.setSesionDate(start);
                                s1.setSlotOfDay(start_slot);
                                sessionRepository.save(s1);
                                checkTotal++;
                                // nếu ngày đấy có 2 slot thì tạo thêm slot thứ 2 ngay sau slot đầu tiên ngay sau slot đấy
                                if (checkTotal < c.getTotalSlot() && checkTotal <= c.getTotalSlot()) {
                                    Session s2 = new Session();
                                    s2.setClassRoom(classRoom);
                                    s2.setRoom(r);
                                    s2.setSesionDate(start);
                                    s2.setSlotOfDay(start_slot + 1);
                                    sessionRepository.save(s2);
                                    checkTotal++;
                                }
                            }
                        }
                    }
                    //tăng start slot để có thể tạo các lớp có mốc thời gian học khác nhau cho dù cùng 1 môn
                    if (start_slot < 6) {
                        start_slot++;
                    } else {
                        start_slot = 1;
                    }
                    // lấy teacher có thể đứng lớp
                    Teacher teacher = teacherRepository.getTeacherTakeClass(semester.getId(), classRoom.getId(), c.getId());
                    if (teacher != null) {
                        classRoom.setTeacher(teacher);
                        classroomRepository.save(classRoom);
                    }
                }
            }
        }
        // gọi đến async void để gửi mail với list giáo viên được xếp lịch ở trên
        List<String> emails = teacherRepository.getAllEmailTeacherHaveClass(semester.getId());
        MailPro mailPro = mailProRepository.getMailProWithRole(enumRole.mail_PQLDT.toString());
        JavaMailSender sender = emailService.getJavaMailSender(mailPro);
        if (!emails.isEmpty() && mailPro != null && sender != null) {
            emailService.sendMessageWithHtmlTemplate(emails, mailPro, sender, "Lịch dạy học mới!!", "/templates/NewScheduleReport.html");
        }
        //
        requestService.changeStatus(requests, enumStatus.DONE);
    }


}
