package com.hotel.roomService.exception;

public class RoomAlreadyExistsException extends RuntimeException{
    public RoomAlreadyExistsException(String message){
        super(message);
    }
}
