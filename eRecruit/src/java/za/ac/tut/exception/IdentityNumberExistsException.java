/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package za.ac.tut.exception;

/**
 *
 * @author Chocolate
 */
public class IdentityNumberExistsException extends RuntimeException{

    /**
     * Creates a new instance of <code>IdentityNumberExistsException</code>
     * without detail message.
     */
    public IdentityNumberExistsException() {
   
    }

    /**
     * Constructs an instance of <code>IdentityNumberExistsException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public IdentityNumberExistsException(String msg) {
        super(msg);
    }
}
