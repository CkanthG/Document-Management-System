package com.sree.document.controller;

import com.sree.document.models.CustomObject;
import com.sree.document.service.CustomObjectService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/customobject")
public class CustomObjectController {

    CustomObjectService customObjectService;

    public CustomObjectController(CustomObjectService customObjectService) {
        this.customObjectService = customObjectService;
    }

    @PostMapping
    public void addCustomObject(@RequestBody CustomObject customObject) {
        customObjectService.saveDocumentMetadata(customObject);
    }

    @GetMapping
    public Map<Integer, CustomObject> getAllCustomObjects() {
        return customObjectService.getAllCustomObjects();
    }

    @GetMapping("/{id}")
    public CustomObject getCustomObjectByID(@PathVariable int id) {
        return customObjectService.getCustomObjectById(id);
    }

    @DeleteMapping("/{customobjectuuid}")
    public void deleteCustomObject(@PathVariable UUID customobjectuuid) {
        customObjectService.deleteCustomObject(customobjectuuid);
    }

    @GetMapping("/name")
    public Object getLatestVersionCustomObjectByName(@RequestParam String customObjectName) {
        return customObjectService.getCustomObjectByLatestVersion(customObjectName);
    }

    @GetMapping("/specificcustomobject")
    public Object getSpecificVersionCustomObject(@RequestParam String customObjectName, @RequestParam int version) {
        return customObjectService.getSpecificCustomObject(customObjectName, version);
    }
}
