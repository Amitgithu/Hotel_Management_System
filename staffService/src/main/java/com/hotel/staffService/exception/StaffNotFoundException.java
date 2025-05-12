package com.hotel.staffService.exception;

public class StaffNotFoundException extends RuntimeException{
    public StaffNotFoundException(String message){
        super(message);
    }
}
