package com.app.studentManagerment.services.servicesImpl;

import com.app.studentManagerment.dao.SlotRepository;
import com.app.studentManagerment.entity.Room;
import com.app.studentManagerment.entity.Slot;
import com.app.studentManagerment.services.SlotService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class SlotServiceImpl implements SlotService {
    private SlotRepository slotRepository;

    public SlotServiceImpl(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }


    @Override
    public void addSlot(Room room) {
        for (int i = 2; i <= 8; i++) {
            LocalTime start_time = LocalTime.parse("07:20");
            LocalTime end_day = LocalTime.parse("22:00");
            int slotNumber = 1;
            while ((start_time.plusHours(2).plusMinutes(10)).isBefore(end_day)) {
                Slot slot = new Slot();
                slot.setSlotOfDay(slotNumber);
                slot.setDayOfWeak(i);
                slot.setRoom(room);
                slot.setStatus(Slot.SlotStatus.AVAILABLE);
                start_time = start_time.plusMinutes(10);
                slot.setStart_time(start_time);
                start_time = start_time.plusHours(2);
                slot.setEnd_time(start_time);
                slotRepository.save(slot);
                slotNumber++;
            }
        }
    }
}
