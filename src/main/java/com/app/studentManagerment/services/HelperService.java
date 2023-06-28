package com.app.studentManagerment.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface HelperService {
    public MultipartFile FileWithNewName(MultipartFile multipartFile,String newName) throws IOException;


}
