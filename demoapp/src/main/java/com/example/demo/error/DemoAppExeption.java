package com.example.demo.error;

public class DemoAppExeption extends RuntimeException {
    public DemoAppExeption(String message) {
        super(message);
    }
}
