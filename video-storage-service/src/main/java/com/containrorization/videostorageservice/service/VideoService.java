package com.containrorization.videostorageservice.service;

import com.containrorization.videostorageservice.model.Video;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface VideoService {
    Video getVideo(String name);

    void storeVideo(MultipartFile videoFile, String name) throws IOException;

}
