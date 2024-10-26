package com.example.demo;

import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(MyController.class);

    @Autowired
    PyTorchService pyTorchService;

    @PostMapping("/predict")
    public ResponseEntity<?> tryModel(@RequestParam("file") MultipartFile file) throws IOException, TranslateException {

        System.out.println("Received file: {}" +  file);

        Image image = ImageFactory.getInstance().fromInputStream(file.getInputStream());
        String output = pyTorchService.classify(image);

        System.out.println("Output: {}" + output);
        return ResponseEntity.ok(output);
    }

    @GetMapping("/model-details")
    public ResponseEntity<?> getModel() {
        return ResponseEntity.ok(pyTorchService.getModel());
    }

}
