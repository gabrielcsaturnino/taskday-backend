package com.example.taskday.domain.exceptions;

public class OperationException extends Exception {
    public OperationException(){}
    public OperationException(String message){
        super(message);
    }
}
