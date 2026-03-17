package com.example.batiss.music.GestionDesSons.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
public class ApiError extends Exception {

    public final int code;
    public final String msg;

    public ResponseEntity getResponse(){
        return ResponseEntity.status(code).body(msg);
    }
}
