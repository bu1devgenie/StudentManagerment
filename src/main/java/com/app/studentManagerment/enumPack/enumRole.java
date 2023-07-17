package com.app.studentManagerment.enumPack;

public enum enumRole {
	Principal("Principal"),
	Admin("Admin"),
	Hr("Hr"),
	Student("Student"),
	Teacher("Teacher"),
	mail_DVMAIL("mail_DVMAIL"),
	mail_PQLDT("mail_PQLDT"),
	mail_PQLSV("mail_PQLSV");

	private String role;

	private enumRole(String role) {
		this.role = role;
	}

	public String toString() {
		return role;
	}

	public static enumRole getEnumFromString(String value) {
		for (enumRole role : enumRole.values()) {
			if (role.toString().equalsIgnoreCase(value)) {
				return role;
			}
		}
		throw new IllegalArgumentException("Invalid string value: " + value);
	}
}
