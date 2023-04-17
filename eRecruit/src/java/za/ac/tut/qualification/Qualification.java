/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.tut.qualification;

/**
 * This object models a qualification
 * @author T Kujwane
 */
public class Qualification {
    private final String type, course;

    public Qualification(String type, String course) {
        this.type = type;
        this.course = course;
    }

    public String getType() {
        return type;
    }

    public String getCourse() {
        return course;
    }

    @Override
    public String toString() {
        return this.type + " in " + this.course;
    }
}
