package com.app.studentManagerment.services.servicesImpl;

import com.app.studentManagerment.dao.RoomRepository;
import com.app.studentManagerment.entity.Room;
import com.app.studentManagerment.services.RoomServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class RoomServicesImpl implements RoomServices {
    private RoomRepository roomRepository;

    public RoomServicesImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }


    @Override
    public Page<Room> getFullRoom() {
        Pageable pageable = PageRequest.of(0, 10);
        return roomRepository.findAll(pageable);
    }

    @Async
    @Override
    public Room addRoom(String address, String name) {
        Room room = new Room();
        room.setAddress(address);
        room.setName(name);
        room = roomRepository.save(room);
        return room;
    }
}
