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
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Map;
import java.util.UUID;

/**
 * This class is used for custom object CRUD operations.
 */
@RestController
@RequestMapping("/customobject")
public class CustomObjectController {

    CustomObjectService customObjectService;

    // constructor injection
    public CustomObjectController(CustomObjectService customObjectService) {
        this.customObjectService = customObjectService;
    }

    /**
     * This method is accepting custom object and store inside the map.
     * @param customObject
     */
    @PostMapping
    public void addCustomObject(@RequestBody CustomObject customObject) {
        customObjectService.saveCustomObjectMetadata(customObject);
    }

    /**
     * This method is accepting custom object with existing id to update inside the map.
     * @param customObject
     * @param id
     * @return updated custom object
     */
    @PutMapping
    public CustomObject updateCustomObject(@RequestBody CustomObject customObject, @RequestParam int id) {
        return customObjectService.updateCustomObjectMetadata(customObject, id);
    }

    /**
     * This method return all custom objects.
     * @return all custom objects map
     */
    @GetMapping
    public Map<Integer, CustomObject> getAllCustomObjects() {
        return customObjectService.getAllCustomObjects();
    }

    /**
     * This method is accepting input as parameter and gives a result.
     * @param id
     * @return matched custom object.
     */
    @GetMapping("/{id}")
    public CustomObject getCustomObjectByID(@PathVariable int id) {
        return customObjectService.getCustomObjectById(id);
    }

    /**
     * This method is accepting the custom object UUID to remove object from map.
     * @param customobjectuuid
     */
    @DeleteMapping("/{customobjectuuid}")
    public void deleteCustomObject(@PathVariable UUID customobjectuuid) {
        customObjectService.deleteCustomObject(customobjectuuid);
    }

    /**
     * This method is accepting name and returned latest version custom object with the name.
     * @param customObjectName
     * @return map entry along with custom object.
     */
    @GetMapping("/name")
    public Object getLatestVersionCustomObjectByName(@RequestParam String customObjectName) {
        return customObjectService.getCustomObjectByLatestVersion(customObjectName);
    }

    /**
     * This method is accepting name and version of custom object and gives result.
     * @param customObjectName
     * @param version
     * @return map entry along with custom object.
     */
    @GetMapping("/specificcustomobject")
    public Object getSpecificVersionCustomObject(@RequestParam String customObjectName, @RequestParam int version) {
        return customObjectService.getSpecificCustomObject(customObjectName, version);
    }
}
