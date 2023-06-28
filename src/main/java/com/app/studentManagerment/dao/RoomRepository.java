package com.app.studentManagerment.dao;

import com.app.studentManagerment.dto.RoomAndSlotCanTakeClass;
import com.app.studentManagerment.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByName(String name);

    Optional<Room> findById(Long id);

        @Query(value = """
                SELECT new com.app.studentManagerment.dto.RoomAndSlotCanTakeClass( r.id, (CASE WHEN
                        (SELECT COUNT(*) FROM Session s WHERE s.slotOfDay = (:startSlot +1) AND DAYOFWEEK(s.sesionDate) = 2
                        AND s.sesionDate BETWEEN :startDate AND :endDate AND s.room.id = r.id) > 0 THEN false ELSE true END),
                        CASE WHEN (SELECT COUNT(*) FROM Session s WHERE s.slotOfDay = (:startSlot +1) AND DAYOFWEEK(s.sesionDate) = 4
                        AND s.sesionDate BETWEEN :startDate AND :endDate AND s.room.id = r.id) > 0 THEN false ELSE true END ,
                        CASE WHEN (SELECT COUNT(*) FROM Session s WHERE s.slotOfDay = (:startSlot +1) AND DAYOFWEEK(s.sesionDate) = 6 
                        AND s.sesionDate BETWEEN :startDate AND :endDate AND s.room.id = r.id) > 0 THEN false ELSE true END )
                        FROM Room r WHERE (SELECT COUNT(*) FROM Session s WHERE s.slotOfDay = :startSlot AND DAYOFWEEK(s.sesionDate) IN (2, 4, 6) 
                        AND s.sesionDate BETWEEN :startDate AND :endDate AND s.room.id = r.id) = 0 AND (SELECT NOT ((SELECT COUNT(*) FROM Session s1 WHERE s1.slotOfDay = (:startSlot +1) 
                        AND DAYOFWEEK(s1.sesionDate) = 2 AND s1.sesionDate BETWEEN :startDate AND :endDate AND s1.room.id = r.id) > 0 AND (SELECT COUNT(*) FROM Session s2 WHERE s2.slotOfDay = (:startSlot +1) 
                        AND DAYOFWEEK(s2.sesionDate) = 4 AND s2.sesionDate BETWEEN :startDate AND :endDate AND s2.room.id = r.id) > 0 AND (SELECT COUNT(*) FROM Session s3 WHERE s3.slotOfDay = (:startSlot +1) 
                        AND DAYOFWEEK(s3.sesionDate) = 6 AND s3.sesionDate BETWEEN :startDate AND :endDate AND s3.room.id = r.id) > 0))=1
                 """)
        List<RoomAndSlotCanTakeClass> getRoomCanTakeClassFor246(
                @Param("startSlot") int startSlot,
                @Param("startDate") LocalDate startDate,
                @Param("endDate") LocalDate endDate);


    @Query(value = """
            select * from room r
            where r.id not in
            (select distinct s.room_id
            from session s
            where   slot_of_day in (:startSlot,(:startSlot + 1))
                    AND DAYOFWEEK(s.sesion_date) IN (:dayOfWeak)
                    AND s.sesion_date BETWEEN :startDate AND :endDate
            )
            limit 1;
            """, nativeQuery = true
    )
    Room getRoomCanTakeClassOtherCase(
            @Param("startSlot") int startSlot,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("dayOfWeak") int[] dayOfWeak);

}
