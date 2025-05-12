package com.example.apigatewayservice.exception;

public class UnauthorizedUserException extends RuntimeException{

    public UnauthorizedUserException(String message){
        super(message);
    }
}
