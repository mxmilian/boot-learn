package pl.msc.demoboot.admin.adminDB;

import org.springframework.web.multipart.MultipartFile;

public class FileUpload {
    private MultipartFile multipartFile;

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }
}
