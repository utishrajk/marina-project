package com.example.demo;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.translate.TranslateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Slf4j
@RestController
public class MyController {

    @Autowired
    PyTorchService pyTorchService;

    @PostMapping("/predict")
    public ResponseEntity<?> tryModel(@RequestParam("file") MultipartFile file) throws IOException, TranslateException {

        log.debug("Received file: {}", file);

        Image image = ImageFactory.getInstance().fromInputStream(file.getInputStream());
        String output = pyTorchService.classify(image);

        log.debug("Output: {}", output);
        return ResponseEntity.ok(output);
    }

}
