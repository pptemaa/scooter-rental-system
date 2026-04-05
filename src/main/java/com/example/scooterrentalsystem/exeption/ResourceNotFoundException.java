package com.example.scooterrentalsystem.exeption;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String massage){
        super(massage);
    }
}
