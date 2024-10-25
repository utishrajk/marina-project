package com.example.demo;

import jakarta.annotation.PostConstruct;
import org.opencv.core.*;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

@RestController
public class ImageController {

    @PostConstruct
    public void init() {
        // Load the OpenCV native library
        nu.pattern.OpenCV.loadLocally();
    }

    @PostMapping("/addNoise")
    public ResponseEntity<byte[]> addNoise(@RequestParam("image") MultipartFile imageFile) throws IOException {
        // Convert MultipartFile to Mat
        Mat image = Imgcodecs.imdecode(new MatOfByte(imageFile.getBytes()), Imgcodecs.IMREAD_COLOR);

        // Add noise to the image
        Mat noisyImage = addNoiseToImage(image);

        // Convert Mat back to byte array
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", noisyImage, matOfByte);
        byte[] imageBytes = matOfByte.toArray();

        // Return the noisy image
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "image/png");
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    @PostMapping("/denoise")
    public ResponseEntity<byte[]> denoise(@RequestParam("image") MultipartFile imageFile) throws IOException {
        // Convert MultipartFile to Mat
        Mat image = Imgcodecs.imdecode(new MatOfByte(imageFile.getBytes()), Imgcodecs.IMREAD_COLOR);

        // Denoise the image with improved parameters
        Mat denoisedImage = new Mat();
        Photo.fastNlMeansDenoisingColored(image, denoisedImage, 10, 10, 7, 21);

        // Convert Mat back to byte array
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", denoisedImage, matOfByte);
        byte[] imageBytes = matOfByte.toArray();

        // Return the denoised image
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "image/png");
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    private Mat addNoiseToImage(Mat image) {
        Mat noise = new Mat(image.size(), image.type());
        Core.randn(noise, 0, 25);
        Core.add(image, noise, image);
        return image;
    }
}