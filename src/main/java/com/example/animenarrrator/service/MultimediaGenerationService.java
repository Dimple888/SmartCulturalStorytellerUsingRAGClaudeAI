package com.example.animenarrrator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class MultimediaGenerationService {

    private static final Logger logger = LoggerFactory.getLogger(MultimediaGenerationService.class);

    @Value("${app.multimedia.output-dir:./anime-videos}")
    private String outputDirectory;

    @Value("${app.multimedia.ffmpeg-path:ffmpeg}")
    private String ffmpegPath;

    /**
     * Generates a video from image and audio files
     * @param imagePath Path to the image file
     * @param audioPath Path to the audio file
     * @param outputPath Path where the video will be saved
     * @return true if successful, false otherwise
     */
    public boolean generateVideoFromImageAndAudio(String imagePath, String audioPath, String outputPath) {
        try {
            // Validate inputs
            if (!Files.exists(Paths.get(imagePath))) {
                logger.error("Image file not found: {}", imagePath);
                return false;
            }
            if (!Files.exists(Paths.get(audioPath))) {
                logger.error("Audio file not found: {}", audioPath);
                return false;
            }

            // Create output directory if it doesn't exist
            createOutputDirectory(outputPath);

            // Build FFmpeg command
            List<String> command = buildFFmpegCommand(imagePath, audioPath, outputPath);

            // Execute FFmpeg
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            
            Process process = processBuilder.start();
            logFFmpegOutput(process);
            
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                logger.info("Video generated successfully: {}", outputPath);
                return true;
            } else {
                logger.error("FFmpeg failed with exit code: {}", exitCode);
                return false;
            }

        } catch (Exception e) {
            logger.error("Error generating video", e);
            return false;
        }
    }

    /**
     * Generates a complete anime video from multiple scenes
     * @param sceneData List of scene images and audios
     * @param outputPath Final video output path
     * @return true if successful, false otherwise
     */
    public boolean generateCompleteAnimeVideo(List<SceneMediaData> sceneData, String outputPath) {
        try {
            createOutputDirectory(outputPath);

            // Step 1: Convert each scene to individual video
            List<String> videoFiles = new ArrayList<>();
            for (int i = 0; i < sceneData.size(); i++) {
                SceneMediaData scene = sceneData.get(i);
                String tempVideoPath = outputDirectory + "/temp-scene-" + i + ".mp4";
                
                if (generateVideoFromImageAndAudio(scene.getImagePath(), scene.getAudioPath(), tempVideoPath)) {
                    videoFiles.add(tempVideoPath);
                    logger.info("Scene {} converted to video", i + 1);
                } else {
                    logger.warn("Failed to convert scene {}", i + 1);
                }
            }

            if (videoFiles.isEmpty()) {
                logger.error("No scene videos generated");
                return false;
            }

            // Step 2: Concatenate all videos
            return concatenateVideos(videoFiles, outputPath);

        } catch (Exception e) {
            logger.error("Error generating complete anime video", e);
            return false;
        }
    }

    /**
     * Concatenates multiple video files into one
     * @param videoFiles List of video file paths
     * @param outputPath Output video path
     * @return true if successful, false otherwise
     */
    public boolean concatenateVideos(List<String> videoFiles, String outputPath) {
        try {
            if (videoFiles.isEmpty()) {
                logger.error("No video files to concatenate");
                return false;
            }

            createOutputDirectory(outputPath);

            // Create concat file
            String concatFilePath = outputDirectory + "/concat-list.txt";
            createConcatFile(videoFiles, concatFilePath);

            // Build FFmpeg command for concatenation
            List<String> command = new ArrayList<>();
            command.add(ffmpegPath);
            command.add("-f");
            command.add("concat");
            command.add("-safe");
            command.add("0");
            command.add("-i");
            command.add(concatFilePath);
            command.add("-c");
            command.add("copy");
            command.add("-y");
            command.add(outputPath);

            // Execute FFmpeg
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            
            Process process = processBuilder.start();
            logFFmpegOutput(process);
            
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                logger.info("Videos concatenated successfully: {}", outputPath);
                // Clean up concat file
                Files.deleteIfExists(Paths.get(concatFilePath));
                return true;
            } else {
                logger.error("FFmpeg concatenation failed with exit code: {}", exitCode);
                return false;
            }

        } catch (Exception e) {
            logger.error("Error concatenating videos", e);
            return false;
        }
    }

    /**
     * Builds FFmpeg command for image + audio to video conversion
     */
    private List<String> buildFFmpegCommand(String imagePath, String audioPath, String outputPath) {
        List<String> command = new ArrayList<>();
        command.add(ffmpegPath);
        command.add("-loop");
        command.add("1");
        command.add("-i");
        command.add(imagePath);
        command.add("-i");
        command.add(audioPath);
        command.add("-c:v");
        command.add("libx264");
        command.add("-c:a");
        command.add("aac");
        command.add("-b:a");
        command.add("192k");
        command.add("-shortest");
        command.add("-y");
        command.add(outputPath);
        return command;
    }

    /**
     * Creates a concat file for FFmpeg video concatenation
     */
    private void createConcatFile(List<String> videoFiles, String concatFilePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String videoFile : videoFiles) {
            sb.append("file '").append(videoFile).append("'\n");
        }
        Files.write(Paths.get(concatFilePath), sb.toString().getBytes());
    }

    /**
     * Logs FFmpeg output for debugging
     */
    private void logFFmpegOutput(Process process) {
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.debug("FFmpeg: {}", line);
                }
            } catch (IOException e) {
                logger.error("Error reading FFmpeg output", e);
            }
        }).start();
    }

    /**
     * Creates output directory if it doesn't exist
     */
    private void createOutputDirectory(String filePath) throws IOException {
        Path path = Paths.get(filePath).getParent();
        if (path != null && !Files.exists(path)) {
            Files.createDirectories(path);
            logger.info("Created output directory: {}", path);
        }
    }

    /**
     * Checks if FFmpeg is installed and accessible
     */
    public boolean isFFmpegAvailable() {
        try {
            ProcessBuilder pb = new ProcessBuilder(ffmpegPath, "-version");
            Process process = pb.start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            logger.error("FFmpeg not available", e);
            return false;
        }
    }

    /**
     * Gets FFmpeg version information
     */
    public String getFFmpegVersion() {
        try {
            ProcessBuilder pb = new ProcessBuilder(ffmpegPath, "-version");
            pb.redirectErrorStream(true);
            Process process = pb.start();
            
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                if ((line = reader.readLine()) != null) {
                    output.append(line);
                }
            }
            
            process.waitFor();
            return output.toString();
        } catch (Exception e) {
            logger.error("Error getting FFmpeg version", e);
            return "FFmpeg not available";
        }
    }

    /**
     * Inner class to hold scene media data
     */
    public static class SceneMediaData {
        private String imagePath;
        private String audioPath;
        private int sceneNumber;

        public SceneMediaData(int sceneNumber, String imagePath, String audioPath) {
            this.sceneNumber = sceneNumber;
            this.imagePath = imagePath;
            this.audioPath = audioPath;
        }

        public String getImagePath() {
            return imagePath;
        }

        public String getAudioPath() {
            return audioPath;
        }

        public int getSceneNumber() {
            return sceneNumber;
        }
    }
}
