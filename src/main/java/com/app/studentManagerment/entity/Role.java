package com.app.studentManagerment.entity;

import jakarta.persistence.*;
import com.app.studentManagerment.enumPack.enumRole;

@Entity
@Table(name = "role")

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private enumRole name;

    public Role() {
    }

    public Role(long id, enumRole name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public enumRole getName() {
        return name;
    }

    public void setName(enumRole name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role{" +
               "id=" + id +
               ", name=" + name +
               '}';
    }
}
