package com.aws.service.s3bucket.controller;

import com.aws.service.s3bucket.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class StorageController {

    @Autowired
    private StorageService storageService;

    @PostMapping(value = "/upload",consumes = "multipart/form-data")
    public String uploadFile(@RequestParam MultipartFile file){
        return storageService.uploadFile(file);
    }

    @PostMapping(value = "/url")
    public String generateObjectUrl(@RequestParam String key){
        return storageService.generateObjectUrl(key);
    }

    @GetMapping(value = "/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam String key){
        byte[] data = storageService.downloadFile(key);

        if(data != null){
            return ResponseEntity.ok()
                    .header("Content-type", "application/octet-stream")
                    .header("Content-Disposition", "attachment; filename=\"" + key + "\"")
                    .body(new ByteArrayResource(data));
        }
        return ResponseEntity.notFound().build();
    }
}
