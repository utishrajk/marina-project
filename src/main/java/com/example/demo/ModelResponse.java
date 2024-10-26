package com.example.demo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ModelResponse implements java.io.Serializable {

    String name;
    Map<String, String> properties = Map.of();


}
