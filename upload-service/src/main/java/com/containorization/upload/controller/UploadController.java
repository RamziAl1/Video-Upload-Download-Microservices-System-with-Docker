package com.containorization.upload.controller;

import com.containorization.upload.service.UploadService;
import com.containorization.upload.dao.VideoDataDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

@Controller
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @RequestMapping(value = "/submit-video", method = RequestMethod.GET)
    protected String getVideoSubmissionPage() {
        return "video-submission";
    }

    @RequestMapping(value = "/submit-video", method = RequestMethod.POST)
    protected String submitVideo(@RequestParam("video-name") String videoName, @RequestParam("file") MultipartFile file, ModelMap m) throws SQLException, IOException {
        String endpoint = "http://video-storage-service:8084/videoDB/";

        //send data to mysql database
        VideoDataDAO vDao = new VideoDataDAO();
        vDao.addVideoData(videoName, endpoint + videoName);

        ResponseEntity<String> re = uploadService.uploadFile(endpoint, videoName, file);

        if (re.getStatusCode().value() == 200)
            m.put("status", "upload successful");
        else
            m.put("status", "upload failure");

        return "video-submission";
    }
}
