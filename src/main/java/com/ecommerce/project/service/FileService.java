package com.ecommerce.project.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    String uplaodImage(String path, MultipartFile image) throws IOException;
}
