package com.app.studentManagerment.services;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Permission;
import jakarta.servlet.ServletException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.List;

public interface GoogleService {
    // Get all file
    public List listEverything() throws IOException, GeneralSecurityException, ServletException;

    // Get all folder
    public List listFolderContent(String parentId) throws IOException, GeneralSecurityException, ServletException;

    // Download file by id
    public void downloadFile(String id, OutputStream outputStream) throws IOException, GeneralSecurityException, ServletException;

    // Delete file by id
    public void deleteFileOrFolder(String fileId) throws Exception;

    // Set permission drive file
    public Permission setPermission(String type, String role);

    // Upload file
    public String uploadFile(MultipartFile file, String folderName, String type, String role,String FileName);

    // getFolderId
    public String getFolderId(String folderName) throws Exception;

    //
    public String findOrCreateFolder(String parentId, String folderName, Drive driveInstance) throws Exception;

    //
    public String searchFolderId(String parentId, String folderName, Drive service) throws Exception;

    //
    public String getLiveLink(String fileId) throws IOException, GeneralSecurityException, ServletException;

}
