package ru.home_work.t1_school.exception;

public class MessageSendException extends RuntimeException{
    public MessageSendException(String message, Throwable cause){super(message, cause);}
    public MessageSendException() {
    }
}
