package com.app.studentManagerment.restController;


import com.app.studentManagerment.dto.StudentDto;
import com.app.studentManagerment.entity.user.Student;
import com.app.studentManagerment.services.HelperService;
import com.app.studentManagerment.services.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;

@RestController
@RequestMapping("/student")
public class StudentRestController {

    private final StudentService studentServices;


    public StudentRestController(StudentService studentServices, HelperService helperService) {
        this.studentServices = studentServices;
    }

    @GetMapping("/getMSSV")
    public synchronized String getMSSV() {
        return studentServices.getMSSV();
    }

    @GetMapping("/findAll")
    public Page<StudentDto> findAll() {
        Pageable pageable = PageRequest.of(0, 10);
        return studentServices.search("", "", pageable);
    }

    @PostMapping("/addStudent")
    public synchronized Student addStudent(
            @RequestParam(name = "current_semester") int current_semester,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam LocalDate dob,
            @RequestParam String address,
            @RequestParam(name = "avatarFile", required = false) MultipartFile avatarFile) throws IOException, GeneralSecurityException {
//      =========================
        return studentServices.addStudent(current_semester, firstName, lastName, dob, address, avatarFile);
    }

    @PostMapping("/searchStudent")
    public Page<StudentDto> searchStudent(@RequestParam(name = "type") String type,
                                          @RequestParam(name = "searchText") String searchText, @RequestParam(name = "targetPageNumber") Integer targetPageNumber) {
        if (targetPageNumber < 0) {
            return null;
        }
        Pageable pageable = PageRequest.of(targetPageNumber, 10);
        return studentServices.search(searchText, type, pageable);

    }

    @DeleteMapping("/deleteStudent")
    public synchronized boolean deleteStudent(@RequestParam(name = "mssv") String mssv) {
        return studentServices.deleteStudent(mssv);
    }

    @PutMapping("/updateStudent")
    public synchronized StudentDto updateStudent(@RequestParam(name = "mssvUpdate", required = true) String mssv,
                                                 @RequestParam(name = "current_semester", required = false) int current_semester,
                                                 @RequestParam(name = "account_mail", required = false) String mail,
                                                 @RequestParam(name = "nameUpdate", required = false) String Name,
                                                 @RequestParam(name = "dobUpdate", required = false) LocalDate dob,
                                                 @RequestParam(name = "addressUpdate", required = false) String address,
                                                 @RequestParam(name = "avatar", required = false) MultipartFile avatarFile) throws Exception {
        System.out.println(mssv + " " + current_semester + " " + mail + " " + Name + " " + dob + " " + address + " " + avatarFile);
        return studentServices.updateStudent(mssv, current_semester, mail, Name, dob, address, avatarFile);
    }
}
