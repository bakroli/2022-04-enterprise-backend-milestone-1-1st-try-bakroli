package com.codecool.exams.supercraftsman.testmodels;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class Expertise extends BaseEntity {
    private Long id;

    private String name;

    public Expertise() {
    }

    public Expertise(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Experties{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expertise expertise = (Expertise) o;
        return Objects.equals(id, expertise.id) && Objects.equals(name, expertise.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
