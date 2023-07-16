package com.app.studentManagerment.services.servicesImpl;

import com.app.studentManagerment.configuration.GoogleDriveConfig;
import com.app.studentManagerment.services.GoogleService;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleServiceImpl implements GoogleService {
    @Autowired
    private GoogleDriveConfig googleDriveConfig;

    @Override
    public List listEverything() throws IOException, GeneralSecurityException, ServletException {
        FileList result = googleDriveConfig.getInstance().files().list()
                .setPageSize(1000)
                .setFields("nextPageToken, files(id, name, size, thumbnailLink, shared)") // get field of google drive file
                .execute();
        return result.getFiles();

    }

    @Override
    public List listFolderContent(String parentId) throws IOException, GeneralSecurityException, ServletException {
        if (parentId == null) {
            parentId = "root";
        }
        String query = "'" + parentId + "' in parents";
        FileList result = googleDriveConfig.getInstance().files().list()
                .setQ(query)
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)") // get field of google drive folder
                .execute();
        return result.getFiles();

    }

    @Override
    public void downloadFile(String id, OutputStream outputStream) throws IOException, GeneralSecurityException, ServletException {
        if (id != null) {
            googleDriveConfig.getInstance().files()
                    .get(id).executeMediaAndDownloadTo(outputStream);
        }
    }

    @Override
    public void deleteFileOrFolder(String fileId) throws Exception {
        googleDriveConfig.getInstance().files().delete(fileId).execute();
    }

    @Override
    public Permission setPermission(String type, String role) {
        Permission permission = new Permission();
        switch (type) {
            case "anyone":
                permission.setType("anyone");
                break;
            case "domain":
                permission.setType("domain");
                permission.setDomain("example.com"); // Replace with your domain name
                break;
            case "user":
                permission.setType("user");
                permission.setEmailAddress("johndoe@example.com"); // Replace with the email address of the user you want to give access to
                break;
            default:
                throw new IllegalArgumentException("Invalid permission type: " + type);
        }
        permission.setRole(role);
        return permission;
    }

    @Override
    public String uploadFile(MultipartFile file, String folderName, String type, String role,String FileName) {
        try {
            String folderId = getFolderId(folderName);
            if (null != file) {

                File fileMetadata = new File();
                fileMetadata.setParents(Collections.singletonList(folderId));
                fileMetadata.setName(FileName);
                File uploadFile = googleDriveConfig.getInstance()
                        .files()
                        .create(fileMetadata, new InputStreamContent(
                                file.getContentType(),
                                new ByteArrayInputStream(file.getBytes()))
                        )
                        .setFields("id").execute();

                if (!type.equals("private") && !role.equals("private")) {
                    // Call Set Permission drive
                    googleDriveConfig.getInstance().permissions().create(uploadFile.getId(), setPermission(type, role)).execute();
                }

                return uploadFile.getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getFolderId(String folderName) throws Exception {
        String parentId = null;
        String[] folderNames = folderName.split("/");

        Drive driveInstance = googleDriveConfig.getInstance();
        for (String name : folderNames) {
            parentId = findOrCreateFolder(parentId, name, driveInstance);
        }
        return parentId;
    }

    @Override
    public String findOrCreateFolder(String parentId, String folderName, Drive driveInstance) throws Exception {
        String folderId = searchFolderId(parentId, folderName, driveInstance);
        // Folder already exists, so return id
        if (folderId != null) {
            return folderId;
        }
        //Folder dont exists, create it and return folderId
        File fileMetadata = new File();
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        fileMetadata.setName(folderName);

        if (parentId != null) {
            fileMetadata.setParents(Collections.singletonList(parentId));
        }
        return driveInstance.files().create(fileMetadata)
                .setFields("id")
                .execute()
                .getId();
    }

    @Override
    public String searchFolderId(String parentId, String folderName, Drive service) throws Exception {
        String folderId = null;
        String pageToken = null;
        FileList result = null;

        File fileMetadata = new File();
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        fileMetadata.setName(folderName);

        do {
            String query = " mimeType = 'application/vnd.google-apps.folder' ";
            if (parentId == null) {
                query = query + " and 'root' in parents";
            } else {
                query = query + " and '" + parentId + "' in parents";
            }
            result = service.files().list().setQ(query)
                    .setSpaces("drive")
                    .setFields("nextPageToken, files(id, name)")
                    .setPageToken(pageToken)
                    .execute();

            for (File file : result.getFiles()) {
                if (file.getName().equalsIgnoreCase(folderName)) {
                    folderId = file.getId();
                }
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null && folderId == null);

        return folderId;
    }

    @Override
    public String getLiveLink(String fileId) throws IOException, GeneralSecurityException, ServletException {
        Permission permission = new Permission()
                .setType("anyone")
                .setRole("reader");
        permission = googleDriveConfig.getInstance().permissions().create(fileId, permission).execute();
        return "https://drive.google.com/uc?id=" + fileId;
    }
}
