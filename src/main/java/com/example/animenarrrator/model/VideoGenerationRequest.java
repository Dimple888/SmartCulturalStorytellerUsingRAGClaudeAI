package com.example.animenarrrator.model;

import java.util.List;

public class VideoGenerationRequest {
    private String imagePath;
    private String audioPath;
    private String outputPath;
    private String videoName;

    public VideoGenerationRequest() {}

    public VideoGenerationRequest(String imagePath, String audioPath, String outputPath, String videoName) {
        this.imagePath = imagePath;
        this.audioPath = audioPath;
        this.outputPath = outputPath;
        this.videoName = videoName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    @Override
    public String toString() {
        return "VideoGenerationRequest{" +
                "imagePath='" + imagePath + '\'' +
                ", audioPath='" + audioPath + '\'' +
                ", outputPath='" + outputPath + '\'' +
                ", videoName='" + videoName + '\'' +
                '}';
    }
}
