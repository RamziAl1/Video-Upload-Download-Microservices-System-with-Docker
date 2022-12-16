package com.containrorization.videostorageservice.service;

import com.containrorization.videostorageservice.model.Video;
import com.containrorization.videostorageservice.repo.VideoRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@AllArgsConstructor
public class ConcreteVideoService implements VideoService {
    @Autowired
    private VideoRepo vRepo;

    @Override
    public Video getVideo(String name) {
        if (!vRepo.existsByName(name))
            return null;
        return vRepo.findByName(name);
    }

    @Override
    public void storeVideo(MultipartFile videoFile, String name) throws IOException {
        if (!vRepo.existsByName(name)) {
            Video vid = new Video(name, videoFile.getBytes());
            vRepo.save(vid);
        }
    }
}
