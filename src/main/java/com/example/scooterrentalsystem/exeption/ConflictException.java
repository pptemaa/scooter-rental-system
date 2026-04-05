package com.example.scooterrentalsystem.exeption;

public class ConflictException extends RuntimeException{
    public ConflictException(String massage){
        super(massage);
    }
}
