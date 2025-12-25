package com.ethical.aims.resources;

import com.google.gson.JsonObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AIMSResource {

    @GetMapping("/ping")
    public String ping() {
        return "OK";
    }

    @GetMapping("/sectionData")
    public String getAllSections() {
        JsonObject obj = new JsonObject();
        obj.addProperty("result", "success-new");
        return obj.toString();
    }
}
