package com.app.studentManagerment.services;

import com.app.studentManagerment.entity.Room;
import org.springframework.data.domain.Page;

public interface RoomServices {
    Page<Room> getFullRoom();

    Room addRoom(String address, String name);

    boolean deleteRoom(String name);

    Room updateRoom(String oldName, String address, String newName);
}
