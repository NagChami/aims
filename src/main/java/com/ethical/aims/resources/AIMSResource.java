package com.ethical.aims.resources;

import com.ethical.aims.service.AimsBasicService;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AIMSResource {

    @Autowired
    private AimsBasicService aimsBasicService;
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


    @GetMapping("/contextRegister")
    public String getContextRegisterDetails(@RequestParam("contextRegisterId") Long contextRegisterId) {
        System.out.println("Inside AIMSResource....getContextRegisterDetails():::contextRegisterId:::"+contextRegisterId);
        JsonObject obj = new JsonObject();
        return aimsBasicService.getContextRegisterDetails(1L);
    }


}
