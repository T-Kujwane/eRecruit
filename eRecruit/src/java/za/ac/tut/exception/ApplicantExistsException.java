/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package za.ac.tut.exception;

/**
 *
 * @author T Kujwane
 */
public class ApplicantExistsException extends Exception {

    /**
     * Creates a new instance of <code>ApplicantExistsException</code> without detail message.
     */
    public ApplicantExistsException() {
    }

    /**
     * Constructs an instance of <code>ApplicantExistsException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ApplicantExistsException(String msg) {
        super(msg);
    }
}
