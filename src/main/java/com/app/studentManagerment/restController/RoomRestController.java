package com.app.studentManagerment.restController;

import com.app.studentManagerment.entity.Room;
import com.app.studentManagerment.services.RoomServices;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomRestController {
    private RoomServices roomServices;

    public RoomRestController(RoomServices roomServices) {
        this.roomServices = roomServices;
    }

    @GetMapping("/getAllRoom")
    public Page<Room> getAllRoom() {
        return roomServices.getFullRoom();
    }

    @PostMapping("/addRoom")
    public Room addRoom(@RequestParam(name = "address") String address, @RequestParam(name = "name") String name) {
        for (int i = 1; i < 50; i++) {
            roomServices.addRoom("Address" + i, "Room" + i);
        }
//        return roomServices.addRoom(address, name);
        return null;
    }
}
