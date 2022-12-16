package com.containrorization.videostorageservice.controller;

import com.containrorization.videostorageservice.service.VideoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("videoDB")
@AllArgsConstructor
public class VideoDBController {

    @Autowired
    private VideoService vService;

    @CrossOrigin
    @PostMapping(value="", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> storeVideo(@RequestParam("file") MultipartFile file, @RequestParam("name") String name) throws IOException {
        vService.storeVideo(file, name);
        return ResponseEntity.ok("successful store");
    }

    @CrossOrigin
    @GetMapping("{name}")
    public ResponseEntity<Resource> getVideoByName(@PathVariable("name") String name){
        if (vService.getVideo(name) == null)
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new ByteArrayResource("".getBytes()));
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(vService.getVideo(name).getData()));
        //ByteArrayResource returns multiple streams of data which is good for video files
    }
}
