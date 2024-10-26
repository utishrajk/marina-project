package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        log.debug("Returning index.html");
        return "index"; // This will render src/main/resources/templates/index.html
    }

    @GetMapping("/denoise")
    public String denoise() {
        log.debug("Returning denoise.html");
        return "denoise"; // This will render src/main/resources/templates/denoise.html
    }

    @GetMapping("/predict")
    public String predict() {
        log.debug("Returning predict.html");
        return "predict"; // This will render src/main/resources/templates/predict.html
    }

    @GetMapping("/model")
    public String model() {
        log.debug("Returning model.html");
        return "model"; // This will render src/main/resources/templates/model.html
    }
}