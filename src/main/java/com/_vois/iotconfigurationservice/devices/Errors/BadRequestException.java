package com._vois.iotconfigurationservice.devices.Errors;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message){
        super(message);
    }
}
