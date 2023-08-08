package com.app.studentManagerment.dao;

import com.app.studentManagerment.dto.ClassRoomDto;
import com.app.studentManagerment.dto.TimeClassDto;
import com.app.studentManagerment.dto.TimeTableSlotDto;
import com.app.studentManagerment.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
	@Query(value = """
			select new com.app.studentManagerment.dto.TimeTableSlotDto(se.room.name,se.classRoom.name,se.sesionDate,se.slotOfDay,se.classRoom.teacher.name,a.atten)
			 from Session se
			                    join ClassRoom cl on cl.id=se.classRoom.id
			                    join Student s 
			                    left join Attendence a on se.id = a.session.id AND a.student.id = s.id
			where s.mssv like :mssv and (se.sesionDate between :startWeek and :endWeek) and s in (select cls from cl.students cls)
								"""
	)
	List<TimeTableSlotDto> getScheduleOfWeekForStudent(@Param("startWeek") LocalDate startWeek,
	                                                   @Param("endWeek") LocalDate endWeek,
	                                                   @Param("mssv") String mssv);

	@Query(value = """
			select DISTINCT DAYNAME(se.sesion_date),se.slot_of_day,t.name,c.name,r.name from session se
						join classroom c on c.id = se.classroom_id
			            join teacher t on t.id = c.teacher_id
			            join room r on se.room_id = r.id
						where c.name like :className
			""", nativeQuery = true)
	List<TimeClassDto> getTimeClass(@Param("className") String className);


}
