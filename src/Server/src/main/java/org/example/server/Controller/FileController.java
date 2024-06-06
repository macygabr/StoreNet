package org.example.server.Controller;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;

@Controller
public class FileController {
    @GetMapping("/files")
    public String files(Map<String, Object> model) {
        File folder = new File("src/main/resources/static/download");
        File[] files = folder.listFiles();
        String[] name = new String[files.length];

        for (int i = 0; i < files.length; i++) {
            name[i] = files[i].getName();
        }

        model.put("files", name);
        return "pages/files";
    }
}