package com.app.studentManagerment.restController;

import com.app.studentManagerment.dto.ClassRoomDto;
import com.app.studentManagerment.entity.ClassRoom;
import com.app.studentManagerment.services.ClassRoomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/classroom")
public class ClassRoomRestController {
    private ClassRoomService classRoomService;

    public ClassRoomRestController(ClassRoomService classRoomService) {
        this.classRoomService = classRoomService;
    }


    @GetMapping("/findAll")
    public Page<ClassRoomDto> findAll() {
        return classRoomService.getAll();
    }


    @PostMapping("/searchClassRoom")
    public Page<ClassRoomDto> searchStudent(@RequestParam(name = "semester_name", required = false) String semester_name,
                                            @RequestParam(name = "msgv", required = false) String msgv,
                                            @RequestParam(name = "courseName", required = false) String courseName,
                                            @RequestParam(name = "currentSlot", required = false) String currentSlot,
                                            @RequestParam(name = "learning", required = false) boolean learning
                                            ) {


        return classRoomService.searchClassRoom(semester_name, msgv, courseName, currentSlot, learning);
    }

    @PostMapping("/newClassRoom")
    public synchronized ClassRoom newClassRoom() {
        return null;
    }

    @PutMapping("/updateClassRoom")
    public synchronized ClassRoom updateClassRoom() {
        return null;
    }

    @DeleteMapping("/shutdownClassRoom")
    public synchronized boolean shutdownClassRoom() {
        return false;
    }


}
