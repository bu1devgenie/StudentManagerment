package com.app.studentManagerment.entity.user;

import com.app.studentManagerment.entity.Account;
import com.app.studentManagerment.enumPack.enumGender;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "user")

public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "ms", nullable = false, unique = true)
	private String ms;
	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "dob", nullable = false)
	private LocalDate dob;

	@Column(name = "avatar", nullable = false)
	private String avatar;


	@ManyToOne
	@JoinColumn(name = "email")
	private Account account;

	@Enumerated(EnumType.STRING)
	private enumGender enumGender;

	public User() {
	}

	public User(long id, String name, String ms, String address, LocalDate dob, String avatar, Account account, enumGender enumGender) {
		this.id = id;
		this.name = name;
		this.ms = ms;
		this.address = address;
		this.dob = dob;
		this.avatar = avatar;
		this.account = account;
		this.enumGender = enumGender;
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

	public String getMs() {
		return ms;
	}

	public void setMs(String ms) {
		this.ms = ms;
	}

	public com.app.studentManagerment.enumPack.enumGender getEnumGender() {
		return enumGender;
	}

	public void setEnumGender(com.app.studentManagerment.enumPack.enumGender enumGender) {
		this.enumGender = enumGender;
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public enumGender getGender() {
		return enumGender;
	}

	public void setGender(enumGender enumGender) {
		this.enumGender = enumGender;
	}

	@Override
	public String toString() {
		return "User{" +
		       "id=" + id +
		       ", name='" + name + '\'' +
		       ", ms='" + ms + '\'' +
		       ", address='" + address + '\'' +
		       ", dob=" + dob +
		       ", avatar='" + avatar + '\'' +
		       ", account=" + account +
		       ", enumGender=" + enumGender +
		       '}';
	}
}
