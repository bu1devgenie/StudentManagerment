package com.app.studentManagerment.dao;

import com.app.studentManagerment.entity.Slot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.lang.reflect.Array;
import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    @Query("""
            SELECT s FROM Slot s
                        where s.dayOfWeak IN (2,4,6)
                        AND s.slotOfDay = (:slot_of_day)
                        AND s.status = 'AVAILABLE'
                        and s.room.id in (SELECT r.id  FROM Slot s1
                                        JOIN Room r ON r.id = s1.room.id
                                        WHERE s1.dayOfWeak IN (2,4,6)
                                        AND s1.slotOfDay = :slot_of_day
                                        AND s1.status = 'AVAILABLE'
                                        GROUP BY r.id
                                        having count(s1.room.id)= 3
                        )
                        order by s.room.id
            """)
    Page<Slot> findSlotCase246(@Param("slot_of_day") int slot_of_day, Pageable pageable);


    @Query("""
            SELECT s FROM Slot s
                        where s.dayOfWeak IN :day_of_week
                        AND s.slotOfDay in :slot_of_day
                        AND s.status = 'AVAILABLE'
                        and s.room.id in (SELECT r.id  FROM Slot s1
                                        JOIN Room r ON r.id = s1.room.id
                                        WHERE s1.dayOfWeak IN :day_of_week
                                        AND s1.slotOfDay in :slot_of_day
                                        AND s1.status = 'AVAILABLE'
                                        GROUP BY r.id
                                        having count(s1.room.id)>= 3
                        )
                        order by s.room.id
            """)
    Page<Slot> findSlotOtherCase(@Param("day_of_week") int[] day_of_week, @Param("slot_of_day") int[] slot_of_day, Pageable pageable);
}
