package com.example.presignedurl.controller;

import com.amazonaws.HttpMethod;
import com.example.presignedurl.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class FileController {

    @Autowired
    private FileService fileService;


    @PostMapping("/geturl")
    public ResponseEntity<String> generateUrl(@RequestParam String extension) {
        return ResponseEntity.ok(fileService.generatePreSignedUrl(
                UUID.randomUUID()+"."+extension, HttpMethod.PUT));
    }

    @GetMapping("/getpdfurl")
    public ResponseEntity<String> getUrl(@RequestParam String filename) {
        return ResponseEntity.ok(fileService.generatePreSignedUrl(
                filename, HttpMethod.GET));
    }

}
