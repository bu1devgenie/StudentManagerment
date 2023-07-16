package com.app.studentManagerment.restController;

import com.app.studentManagerment.dto.TeacherDto;
import com.app.studentManagerment.enumPack.enumGender;
import com.app.studentManagerment.services.TeacherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherRestController {
    private TeacherService teacherService;

    public TeacherRestController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/getMSGV")
    public String getMSGV() {
        return teacherService.getMSGV();
    }

    @PostMapping("/addTeacher")
    public TeacherDto addTeacher(@RequestParam(name = "selectedCourse", required = false) List<String> courses,
                                 @RequestParam(name = "name") String name,
                                 @RequestParam(name = "dob") LocalDate dob,
                                 @RequestParam(name = "address") String address,
                                 @RequestParam(name = "avatarFile", required = false) MultipartFile avatar,
                                 @RequestParam(name = "enumGender") enumGender enumGender) {

        return teacherService.addTeacher(courses, name, dob, address, avatar, enumGender);
    }

    @PostMapping("/searchTeacher")
    public Page<TeacherDto> searchTeacher(@RequestParam(name = "type") String type,
                                          @RequestParam(name = "searchText") String searchText, @RequestParam(name = "targetPageNumber") Integer targetPageNumber) {
        if (targetPageNumber < 0) {
            return null;
        }
        Pageable pageable = PageRequest.of(targetPageNumber, 10);
        return teacherService.search(searchText, type, pageable);
    }

    @GetMapping("/getAllTeacher")
    public Page<TeacherDto> getAllTeacher() {
        return searchTeacher("", "", 0);
    }

    @PutMapping("/updateTeacher")
    public synchronized TeacherDto updateTeacher(@RequestParam(name = "msgvUpdate") String msgvUpdate,
                                                 @RequestParam(name = "name", required = false) String name,
                                                 @RequestParam(name = "address", required = false) String address,
                                                 @RequestParam(name = "dob", required = false) LocalDate dob,
                                                 @RequestParam(name = "avatar", required = false) MultipartFile avatar,
                                                 @RequestParam(name = "course", required = false) List<String> courses,
                                                 @RequestParam(name = "email", required = false) String email,
                                                 @RequestParam(name = "enumGender") enumGender enumGender) throws Exception {
        return teacherService.updateTeacher(msgvUpdate, name, address, dob, avatar, courses, email, enumGender);
    }

    @DeleteMapping("/deleteTeacher")
    public synchronized boolean deleteTeacher(@RequestParam(name = "msgv") String msgv) {

        return teacherService.deleteTeacher(msgv);
    }
}
