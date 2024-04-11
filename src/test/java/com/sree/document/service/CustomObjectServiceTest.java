package com.sree.document.service;

import com.sree.document.kafka.KafkaMessageListener;
import com.sree.document.models.CustomObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CustomObjectServiceTest {

    @InjectMocks
    private CustomObjectService customObjectService;
    @Mock
    private KafkaMessageListener kafkaMessageListener;

    @BeforeEach
    public void cleanUpMock() {
        MockitoAnnotations.initMocks(this);
        customObjectService.customObjectMap.clear();
    }

    @Test
    public void saveCustomObject() {
        CustomObject customObject = createCustomObjectBean();
        customObjectService.customObjectMap.put(1, customObject);
        Mockito.doNothing().when(kafkaMessageListener).sendMessageToKafka(any(), any());
        customObjectService.customObjectMap.clear();
        customObjectService.saveCustomObjectMetadata(customObject);
        Object obj = customObjectService.getCustomObjectById(1);
        Assertions.assertNotNull(obj);
    }

    @Test
    public void updateCustomObject() {
        CustomObject customObject = createCustomObjectBean();
        customObjectService.customObjectMap.put(1, customObject);
        Mockito.doNothing().when(kafkaMessageListener).sendMessageToKafka(any(), any());
        customObjectService.customObjectMap.clear();
        customObjectService.saveCustomObjectMetadata(customObject);
        customObject.setCustomObjectVersion(2);
        customObjectService.updateCustomObjectMetadata(customObject, 1);
        Object obj = customObjectService.getCustomObjectById(1);
        Assertions.assertNotNull(obj);
    }

    @Test
    public void getAllCustomObjects() {
        CustomObject customObject = createCustomObjectBean();
        customObjectService.customObjectMap.put(1, customObject);
        customObjectService.customObjectMap.put(1, customObject);
        Mockito.doNothing().when(kafkaMessageListener).sendMessageToKafka(any(), any());
        customObjectService.customObjectMap.clear();
        customObjectService.customObjectMap.clear();customObjectService.saveCustomObjectMetadata(customObject);
        Map<Integer, CustomObject> allCustomObjects = customObjectService.getAllCustomObjects();
        Assertions.assertEquals(allCustomObjects.size(), 1);
    }

    @Test
    public void getCustomObjectById() {
        CustomObject customObject = createCustomObjectBean();
        customObjectService.customObjectMap.put(1, customObject);
        customObjectService.customObjectMap.put(1, customObject);
        Mockito.doNothing().when(kafkaMessageListener).sendMessageToKafka(any(), any());
        customObjectService.customObjectMap.clear();
        customObjectService.customObjectMap.clear();customObjectService.saveCustomObjectMetadata(customObject);
        Object co = customObjectService.getCustomObjectById(1);
        Assertions.assertNotNull(co);
    }

    @Test
    public void deleteCustomObject() {
        CustomObject customObject = createCustomObjectBean();
        customObjectService.customObjectMap.put(1, customObject);
        Mockito.doNothing().when(kafkaMessageListener).sendMessageToKafka(any(), any());
        customObjectService.customObjectMap.clear();
        customObjectService.saveCustomObjectMetadata(customObject);
        customObjectService.deleteCustomObject(customObject.getCustomObjectUUID());
        int size = customObjectService.customObjectMap.size();
        Assertions.assertEquals(size, 0);
    }

    @Test
    public void getCustomObjectByLatestVersion() {
        CustomObject customObject = createCustomObjectBean();
        customObjectService.customObjectMap.put(1, customObject);
        Mockito.doNothing().when(kafkaMessageListener).sendMessageToKafka(any(), any());
        customObjectService.customObjectMap.clear();
        customObjectService.saveCustomObjectMetadata(customObject);
        Object co = customObjectService.getLatestVersionCustomObject(customObject.getCustomObjectName());
        Assertions.assertNotNull(co);
    }

    @Test
    public void getSpecificCustomObject() {
        CustomObject customObject = createCustomObjectBean();
        customObjectService.customObjectMap.put(1, customObject);
        Mockito.doNothing().when(kafkaMessageListener).sendMessageToKafka(any(), any());
        customObjectService.customObjectMap.clear();
        customObjectService.saveCustomObjectMetadata(customObject);
        Object co = customObjectService.getSpecificCustomObject(customObject.getCustomObjectName(), customObject.getCustomObjectVersion());
        Assertions.assertNotNull(co);
    }

    private CustomObject createCustomObjectBean() {
        CustomObject customObject = new CustomObject();
        customObject.setCustomObjectUUID(UUID.randomUUID());
        customObject.setCustomObjectName("mockito test");
        customObject.setCustomObjectCreatedBy("mockito test user");
        customObject.setCustomObjectUpdatedBy("mockito test user");
        customObject.setCustomObjectPresent(true);
        customObject.setCustomObjectVersion(1);
        return customObject;
    }
}
