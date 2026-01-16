package com.example.animenarrrator.model;

public class VideoGenerationResponse {
    private boolean success;
    private String videoPath;
    private String message;
    private long generationTime;
    private String videoName;

    public VideoGenerationResponse() {}

    public VideoGenerationResponse(boolean success, String videoPath, String message, long generationTime, String videoName) {
        this.success = success;
        this.videoPath = videoPath;
        this.message = message;
        this.generationTime = generationTime;
        this.videoName = videoName;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getGenerationTime() {
        return generationTime;
    }

    public void setGenerationTime(long generationTime) {
        this.generationTime = generationTime;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    @Override
    public String toString() {
        return "VideoGenerationResponse{" +
                "success=" + success +
                ", videoPath='" + videoPath + '\'' +
                ", message='" + message + '\'' +
                ", generationTime=" + generationTime +
                ", videoName='" + videoName + '\'' +
                '}';
    }
}
