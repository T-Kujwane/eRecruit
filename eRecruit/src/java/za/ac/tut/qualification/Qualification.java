/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.tut.qualification;

import java.util.Objects;

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.type);
        hash = 41 * hash + Objects.hashCode(this.course);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Qualification other = (Qualification) obj;
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        return Objects.equals(this.course, other.course);
    }
    
    
}
