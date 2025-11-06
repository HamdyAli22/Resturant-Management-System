package com.restaurant.spring.service.impl;

import com.restaurant.spring.service.FileUploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Override
    public String uploadFile(MultipartFile file, String folder) throws IOException {
      //  String uploadDir = "../front-end/src/assets/img/" + folder;
        String uploadDir = "../uploads/" + folder;
        System.out.println("uploadDir: " + uploadDir);
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
            Path path = Paths.get(uploadDir, file.getOriginalFilename());
            System.out.println("path: " + path);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return folder + "/" + file.getOriginalFilename();
    }
}
