/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author romulo
 */
public class ItemAlreadyExistException extends Exception {

    public ItemAlreadyExistException() {
    }

    public ItemAlreadyExistException(String message) {
        super(message);
    }

}
