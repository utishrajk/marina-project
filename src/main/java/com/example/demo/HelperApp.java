package com.example.demo;

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class HelperApp {

    private static final String SOURCE_DIR = "/home/suniti/Downloads/flower_images/flower_images";
    private static final Map<String, String> FILE_LABELS = new HashMap<>();

    static {
        String[] labelData = {
                "0001.png 0", "0002.png 0", "0003.png 2", "0004.png 0", "0005.png 0",
                "0006.png 1", "0007.png 6", "0008.png 0", "0009.png 0", "0010.png 0",
                "0011.png 0", "0012.png 0", "0013.png 0", "0014.png 7", "0015.png 7",
                "0016.png 1", "0017.png 0", "0018.png 0", "0019.png 6", "0020.png 0",
                "0021.png 2", "0022.png 4", "0023.png 7", "0024.png 4", "0025.png 5",
                "0026.png 6", "0027.png 2", "0028.png 5", "0029.png 6", "0030.png 6",
                "0031.png 3", "0032.png 6", "0033.png 5", "0034.png 0", "0035.png 3",
                "0036.png 8", "0037.png 5", "0038.png 9", "0039.png 2", "0040.png 8",
                "0041.png 9", "0042.png 1", "0043.png 7", "0044.png 3", "0045.png 1",
                "0046.png 4", "0047.png 7", "0048.png 3", "0049.png 8", "0050.png 1",
                "0051.png 3", "0052.png 4", "0053.png 7", "0054.png 9", "0055.png 3",
                "0056.png 6", "0057.png 5", "0058.png 8", "0059.png 6", "0060.png 8",
                "0061.png 2", "0062.png 1", "0063.png 7", "0064.png 8", "0065.png 0",
                "0066.png 5", "0067.png 6", "0068.png 3", "0069.png 6", "0070.png 4",
                "0071.png 9", "0072.png 7", "0073.png 9", "0074.png 1", "0075.png 5",
                "0076.png 3", "0077.png 6", "0078.png 6", "0079.png 8", "0080.png 3",
                "0081.png 1", "0082.png 4", "0083.png 3", "0084.png 9", "0085.png 8",
                "0086.png 5", "0087.png 2", "0088.png 4", "0089.png 6", "0090.png 4",
                "0091.png 7", "0092.png 1", "0093.png 5", "0094.png 2", "0095.png 1",
                "0096.png 5", "0097.png 8", "0098.png 5", "0099.png 8", "0100.png 3",
                "0101.png 1", "0102.png 2", "0103.png 4", "0104.png 5", "0105.png 1",
                "0106.png 2", "0107.png 8", "0108.png 3", "0109.png 8", "0110.png 3",
                "0111.png 5", "0112.png 4", "0113.png 2", "0114.png 9", "0115.png 5",
                "0116.png 0", "0117.png 8", "0118.png 6", "0119.png 0", "0120.png 8",
                "0121.png 5", "0122.png 2", "0123.png 4", "0124.png 5", "0125.png 8",
                "0126.png 3", "0127.png 2", "0128.png 0", "0129.png 8", "0130.png 6",
                "0131.png 9", "0132.png 2", "0133.png 8", "0134.png 4", "0135.png 5",
                "0136.png 8", "0137.png 0", "0138.png 6", "0139.png 2", "0140.png 4",
                "0141.png 9", "0142.png 4", "0143.png 5", "0144.png 5", "0145.png 2",
                "0146.png 7", "0147.png 8", "0148.png 4", "0149.png 9", "0150.png 3",
                "0151.png 2", "0152.png 4", "0153.png 7", "0154.png 5", "0155.png 9",
                "0156.png 3", "0157.png 1", "0158.png 8", "0159.png 1", "0160.png 3",
                "0161.png 6", "0162.png 9", "0163.png 1", "0164.png 2", "0165.png 8",
                "0166.png 2", "0167.png 7", "0168.png 9", "0169.png 9", "0170.png 5",
                "0171.png 9", "0172.png 8", "0173.png 3", "0174.png 9", "0175.png 8",
                "0176.png 5", "0177.png 1", "0178.png 4", "0179.png 2", "0180.png 7",
                "0181.png 0", "0182.png 5", "0183.png 8", "0184.png 6", "0185.png 3",
                "0186.png 9", "0187.png 6", "0188.png 1", "0189.png 3", "0190.png 7",
                "0191.png 4", "0192.png 7", "0193.png 1", "0194.png 9", "0195.png 8",
                "0196.png 3", "0197.png 6", "0198.png 5", "0199.png 6", "0200.png 4",
                "0201.png 1", "0202.png 3", "0203.png 8", "0204.png 5", "0205.png 4",
                "0206.png 6", "0207.png 0", "0208.png 4", "0209.png 6", "0210.png 1"
        };

        for (String pair : labelData) {
            String[] parts = pair.split(" ");
            FILE_LABELS.put(parts[0], parts[1]);
        }
    }

    public static void main(String[] args) {
        File sourceDir = new File(SOURCE_DIR);
        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            System.err.println("Source directory does not exist or is not a directory.");
            return;
        }

        for (File file : sourceDir.listFiles()) {
            if (file.isFile() && FILE_LABELS.containsKey(file.getName())) {
                String label = FILE_LABELS.get(file.getName());
                File destDir = new File(sourceDir, label);

                if (!destDir.exists()) {
                    if (!destDir.mkdir()) {
                        System.err.println("Failed to create directory: " + destDir.getPath());
                        continue;
                    }
                }

                try {
                    Path source = file.toPath();
                    Path destination = Paths.get(destDir.getPath(), file.getName());
                    Files.move(source, destination);
                    System.out.println("Moved " + file.getName() + " to folder " + label);
                } catch (IOException e) {
                    System.err.println("Error moving file " + file.getName() + ": " + e.getMessage());
                }
            }
        }

        System.out.println("File organization complete.");

    }
}
