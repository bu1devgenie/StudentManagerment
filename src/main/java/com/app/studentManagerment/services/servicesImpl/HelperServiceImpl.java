package com.app.studentManagerment.services.servicesImpl;

import com.app.studentManagerment.services.HelperService;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class HelperServiceImpl implements HelperService {

    @Override
    public MultipartFile FileWithNewName(MultipartFile multipartFile, String newName) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename(); // Lấy tên file gốc
        String newFilename = newName; // Tên mới cho file

    // Lưu file với tên mới
        File destFile = new File(newFilename);
        multipartFile.transferTo(destFile);

        // Tạo đối tượng MultipartFile mới với tên mới
        MultipartFile newMultipartFile = new MockMultipartFile(
                newFilename,
                originalFilename,
                multipartFile.getContentType(),
                new FileInputStream(destFile)
        );
        return newMultipartFile;
    }
}
