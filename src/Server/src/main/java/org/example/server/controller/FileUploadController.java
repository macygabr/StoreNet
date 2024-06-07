package org.example.server.controller;

import org.example.server.backend.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.sql.DataSource;

@Controller
public class FileUploadController {
    @Autowired
    private Database database;

    @RequestMapping("/upload")
    public String uploadForm() {
        return "pages/upload";
    }
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String path = new File("").getAbsolutePath();
            file.transferTo(new File(path +"/src/main/resources/static/download/" + file.getOriginalFilename()));
            database.update();
            return "pages/upload";
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}