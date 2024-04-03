package com.sree.document.service;

import com.sree.document.models.CustomObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
public class CustomObjectServiceIT {

    @Autowired
    CustomObjectService customObjectService;

    @BeforeEach
    public void cleanUp() {
        customObjectService.customObjectMap.clear();
    }

    @Test
    public void saveCustomObject() {
        CustomObject customObject = createCustomObjectBean();
        customObjectService.saveDocumentMetadata(customObject);
        Object obj = customObjectService.getCustomObjectById(1);
        Assertions.assertNotNull(obj);
    }

    @Test
    public void getAllCustomObjects() {
        CustomObject customObject = createCustomObjectBean();
        customObjectService.saveDocumentMetadata(customObject);
        Map<Integer, CustomObject> allCustomObjects = customObjectService.getAllCustomObjects();
        Assertions.assertEquals(allCustomObjects.size(), 1);
    }

    @Test
    public void getCustomObjectById() {
        CustomObject customObject = createCustomObjectBean();
        customObjectService.saveDocumentMetadata(customObject);
        Object co = customObjectService.getCustomObjectById(1);
        Assertions.assertNotNull(co);
    }

    @Test
    public void deleteCustomObject() {
        CustomObject customObject = createCustomObjectBean();
        customObjectService.saveDocumentMetadata(customObject);
        customObjectService.deleteCustomObject(customObject.getCustomObjectUUID());
        int size = customObjectService.customObjectMap.size();
        Assertions.assertEquals(size, 0);
    }

    @Test
    public void getCustomObjectByLatestVersion() {
        CustomObject customObject = createCustomObjectBean();
        customObjectService.saveDocumentMetadata(customObject);
        Object co = customObjectService.getLatestVersionCustomObject(customObject.getCustomObjectName());
        Assertions.assertNotNull(co);
    }

    @Test
    public void getSpecificCustomObject() {
        CustomObject customObject = createCustomObjectBean();
        customObjectService.saveDocumentMetadata(customObject);
        Object co = customObjectService.getSpecificCustomObject(customObject.getCustomObjectName(), customObject.getCustomObjectVersion());
        Assertions.assertNotNull(co);
    }

    private CustomObject createCustomObjectBean() {
        CustomObject customObject = new CustomObject();
        customObject.setCustomObjectUUID(UUID.randomUUID());
        customObject.setCustomObjectName("integration test");
        customObject.setCustomObjectCreatedBy("integration test user");
        customObject.setCustomObjectUpdatedBy("integration test user");
        customObject.setCustomObjectPresent(true);
        customObject.setCustomObjectVersion(1);
        return customObject;
    }
}
