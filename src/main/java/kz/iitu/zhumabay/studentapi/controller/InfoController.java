package kz.iitu.zhumabay.studentapi.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    private final String author;
    private final String group;
    private final String version;

    public InfoController(@Value("${student.author}") String author,
                          @Value("${student.group}") String group,
                          ObjectProvider<BuildProperties> buildProperties) {
        this.author = author;
        this.group = group;
        BuildProperties props = buildProperties.getIfAvailable();
        this.version = props != null ? props.getVersion() : "dev";
    }

    @GetMapping({"/", "/api/info"})
    public Map<String, Object> info() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("application", "zhumabay-zhuldyz-student-api");
        body.put("author", author);
        body.put("group", group);
        body.put("version", version);
        body.put("message", "Student CRUD API by " + author + " is running");
        return body;
    }
}
