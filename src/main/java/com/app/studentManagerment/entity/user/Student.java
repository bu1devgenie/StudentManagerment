package com.app.studentManagerment.entity.user;

import com.app.studentManagerment.entity.Account;
import com.app.studentManagerment.entity.ClassRoom;
import com.app.studentManagerment.enumPack.enumGender;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "student")


public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "mssv", nullable = false, unique = true)
	private String mssv;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "dob", nullable = false)
	private LocalDate dob;
	@Enumerated(EnumType.STRING)
	private enumGender enumGender;


	@Column(name = "avatar", nullable = true)
	private String avatar;

	@Column(name = "currentSemester", nullable = false)
	private int currentSemester;

	@OneToOne
	@JoinColumn(name = "email")
	private Account account;

	@Cascade(org.hibernate.annotations.CascadeType.MERGE)
	@ManyToMany(mappedBy = "students")
	private List<ClassRoom> classRooms;

	public Student() {
	}

	public Student(long id, String name, String mssv, String address, LocalDate dob, enumGender enumGender, String avatar, int currentSemester, Account account, List<ClassRoom> classRooms) {
		this.id = id;
		this.name = name;
		this.mssv = mssv;
		this.address = address;
		this.dob = dob;
		this.enumGender = enumGender;
		this.avatar = avatar;
		this.currentSemester = currentSemester;
		this.account = account;
		this.classRooms = classRooms;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMssv() {
		return mssv;
	}

	public void setMssv(String mssv) {
		this.mssv = mssv;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public enumGender getGender() {
		return enumGender;
	}

	public void setGender(enumGender enumGender) {
		this.enumGender = enumGender;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getCurrentSemester() {
		return currentSemester;
	}

	public void setCurrentSemester(int currentSemester) {
		this.currentSemester = currentSemester;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public List<ClassRoom> getClassRooms() {
		return classRooms;
	}

	public void setClassRooms(List<ClassRoom> classRooms) {
		this.classRooms = classRooms;
	}

	@Override
	public String toString() {
		return "Student{" +
		       "id=" + id +
		       ", name='" + name + '\'' +
		       ", mssv='" + mssv + '\'' +
		       ", address='" + address + '\'' +
		       ", dob=" + dob +
		       ", gender=" + enumGender +
		       ", avatar='" + avatar + '\'' +
		       ", currentSemester=" + currentSemester +
		       ", account=" + account +
		       ", classRooms=" + classRooms +
		       '}';
	}
}
