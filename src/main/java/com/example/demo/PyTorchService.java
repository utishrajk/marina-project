package com.example.demo;

import ai.djl.Application;
import ai.djl.MalformedModelException;
import ai.djl.Model;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.Pipeline;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PyTorchService {
    private ZooModel<Image, Classifications> model;

    /**
     * Loads the model after the service is constructed.
     * This method is annotated with @PostConstruct to ensure it runs after the service is initialized.
     * It sets up the criteria for loading the model, including the model path and translator.
     * Then, it loads the model based on the criteria.
     */
    @PostConstruct
    public void loadModel() throws IOException, MalformedModelException, ModelNotFoundException {
        Criteria<Image, Classifications> criteria = Criteria.builder()
                .setTypes(Image.class, Classifications.class)
                .optApplication(Application.CV.IMAGE_CLASSIFICATION)
                //.optModelUrls("file:///c:/data/resnet50_scripted.pt")  // path to the PyTorch model file
                .optModelUrls("file:///home/suniti/PycharmProjects/pythonProject1/fine_tuned_resnet18_scripted.pt")  // path to the PyTorch model file
                .optTranslator(new SimpleTranslator())
                .build();

        // sysout that criteria is created
        System.out.println("Criteria created");

        ZooModel<Image, Classifications> ssd = criteria.loadModel();
        // sysout that model is loaded
        System.out.println("Model loaded");

        model = criteria.loadModel();

        System.out.println("Model loaded successfully");
    }

    /**
     * Classifies an image.
     * This method takes an image as input, uses the loaded model to predict the classification,
     * and returns the best classification class name.
     *
     * @param image The input image to classify.
     * @return The best classification class name.
     * @throws TranslateException If an error occurs during prediction.
     */
    public String classify(Image image) throws TranslateException {
        try (Predictor<Image, Classifications> predictor = model.newPredictor()) {
            Classifications classifications = predictor.predict(image);
            if (classifications.items().isEmpty()) {
                return "No classification found";
            }
            return classifications.best().getClassName();
        }
    }

    /**
     * Inner class to handle image preprocessing and postprocessing.
     * This translator converts the input image to a tensor and processes the output to get the classification probabilities.
     */
    private static class SimpleTranslator implements Translator<Image, Classifications> {

        @Override
        public NDList processInput(TranslatorContext ctx, Image input) {
            // Convert Image to NDArray with 3 channels (RGB)
            NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);

            // Apply transformations: Resize, CenterCrop, ToTensor, Normalize
            array = NDImageUtils.resize(array, 256, 256);
            array = NDImageUtils.centerCrop(array, 224, 224);
            array = NDImageUtils.toTensor(array);
            array = NDImageUtils.normalize(array, new float[]{0.485f, 0.456f, 0.406f}, new float[]{0.229f, 0.224f, 0.225f});

            return new NDList(array);
        }

        @Override
        public Classifications processOutput(TranslatorContext ctx, NDList list) {
            // Create a Classifications with the output probabilities
            NDArray probabilities = list.singletonOrThrow().softmax(0);
            List<String> classNames = IntStream.range(0, (int) probabilities.size())
                    .mapToObj(String::valueOf)
                    .collect(Collectors.toList());
            return new Classifications(classNames, probabilities);
        }
    }
}
