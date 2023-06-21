package com.app.studentManagerment.dto;

import com.app.studentManagerment.dto.StudentDto;
import io.opencensus.trace.Link;

import java.util.List;

public class Semester_StudentDto {
    private int current_Semester;
    private List<StudentDto> studentDtos;

    public Semester_StudentDto() {
    }

    public Semester_StudentDto(int current_Semester, List<StudentDto> studentDtos) {
        this.current_Semester = current_Semester;
        this.studentDtos = studentDtos;
    }

    public int getCurrent_Semester() {
        return current_Semester;
    }

    public void setCurrent_Semester(int current_Semester) {
        this.current_Semester = current_Semester;
    }

    public List<StudentDto> getStudentDtos() {
        return studentDtos;
    }

    public void setStudentDtos(List<StudentDto> studentDtos) {
        this.studentDtos = studentDtos;
    }

    @Override
    public String toString() {
        return "Semester_StudentDto{" +
                "current_Semester=" + current_Semester +
                ", studentDtos=" + studentDtos +
                '}';
    }
}
