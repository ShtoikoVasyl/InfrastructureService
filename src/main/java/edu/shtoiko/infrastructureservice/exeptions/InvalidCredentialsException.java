package edu.shtoiko.infrastructureservice.exeptions;

public class InvalidCredentialsException extends Exception{
    public InvalidCredentialsException(String message) {
        super(message);
    }
}