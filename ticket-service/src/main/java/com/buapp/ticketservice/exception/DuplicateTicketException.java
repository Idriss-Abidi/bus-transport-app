package com.buapp.ticketservice.exception;

public class DuplicateTicketException extends RuntimeException {
    public DuplicateTicketException(String message) {
        super(message);
    }
}
