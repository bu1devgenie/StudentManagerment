package com.app.studentManagerment.services;

import com.app.studentManagerment.dao.RoomRepository;
import com.app.studentManagerment.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoomServices {
    Page<Room> getFullRoom();

    Room addRoom(String address,String name);
}
