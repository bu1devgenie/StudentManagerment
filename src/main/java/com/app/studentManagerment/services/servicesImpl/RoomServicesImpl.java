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

    @Override
    public boolean deleteRoom(String name) {
        Room room = roomRepository.findByName(name);
        if (room != null) {
            roomRepository.delete(room);
            return true;
        }
        return false;
    }

    @Override
    public Room updateRoom(String oldName, String address, String newName) {
        Room room = roomRepository.findByName(oldName);
        if (room != null) {
            room.setName(newName);
            room.setAddress(address);
            return roomRepository.save(room);
        }
        return null;
    }


}
