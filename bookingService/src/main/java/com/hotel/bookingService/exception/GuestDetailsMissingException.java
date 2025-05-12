package com.hotel.bookingService.exception;

public class GuestDetailsMissingException extends RuntimeException {
    public GuestDetailsMissingException(String message) {
        super(message);
    }
}
