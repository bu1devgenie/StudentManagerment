package com.app.studentManagerment.entity;

import com.app.studentManagerment.enumPack.enumStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "")
public class Requests {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String description;
	@Column
	private LocalDateTime time;

	@Enumerated(EnumType.STRING)
	private enumStatus status;

	public Requests() {
	}

	public Requests(Long id, String description, LocalDateTime time, enumStatus status) {
		this.id = id;
		this.description = description;
		this.time = time;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public enumStatus getStatus() {
		return status;
	}

	public void setStatus(enumStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Requests{" +
		       "id=" + id +
		       ", description='" + description + '\'' +
		       ", time=" + time +
		       ", status=" + status +
		       '}';
	}
}
