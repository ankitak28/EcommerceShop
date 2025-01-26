package com.ecommerce.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{

    @Override
    public String uplaodImage(String path, MultipartFile image) throws IOException {
        //file names of current/ original files
        String originalFilename = image.getOriginalFilename();

        //Generate a unique filename with the help of randomUUID
        String randomUUID = UUID.randomUUID().toString(); //generates a random UUID
        //mat.jpg --> 1234 --> 124.jpg
        String fileName = randomUUID.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));
        String filePath = path + File.separator +fileName; //pathSeparator is better to use as  hardcoded "/" might not run on linux or other OS
        //Check if path exists and  create
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }
        //upload to server
        Files.copy(image.getInputStream(), Paths.get(filePath));
        //return filename

        return fileName;
    }
}
