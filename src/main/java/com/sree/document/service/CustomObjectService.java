package com.sree.document.service;

import com.sree.document.kafka.KafkaProducerAndConsumer;
import com.sree.document.models.CustomObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CustomObjectService {

    final public Map<Integer, CustomObject> customObjectMap = new ConcurrentHashMap<>();
    @Autowired
    KafkaProducerAndConsumer kafkaProducerAndConsumer;

    /**
     * This method is accepting custom object and save information in map.
     * @param customObject
     */
    public void saveCustomObjectMetadata(CustomObject customObject) {
        // Get the next available key by incrementing the maximum key by 1
        int nextKey = customObjectMap.isEmpty() ? 1 :
                customObjectMap.keySet().stream().max(Comparator.naturalOrder()).orElse(0) + 1;
        customObjectMap.put(nextKey, customObject);
        kafkaProducerAndConsumer.kafkaProducer(customObject.getCustomObjectName(), customObjectMap.toString());
    }

    /**
     * This method is accepting custom object and id then updating the map accordingly.
     * @param customObject
     * @param id
     * @return updated custom object.
     */
    public CustomObject updateCustomObjectMetadata(CustomObject customObject, int id) {
        return customObjectMap.put(id, customObject);
    }

    /**
     * This method is used to return all custom objects.
     * @return all custom objects.
     */
    public Map<Integer, CustomObject> getAllCustomObjects() {
        return customObjectMap;
    }

    /**
     * This method is accepting map id and return corresponding value in map.
     * @param id
     * @return matched custom object.
     */
    public CustomObject getCustomObjectById(int id) {
        kafkaProducerAndConsumer.kafkaConsumer();
        return customObjectMap.get(id);
    }

    /**
     * This method accepting custom object UUID to remove the entry from map.
     * @param customObjectUUID
     */
    public void deleteCustomObject(UUID customObjectUUID) {
        // Remove the entry with matching UUID
        customObjectMap.entrySet().removeIf(
                entry -> entry.getValue().getCustomObjectUUID().equals(customObjectUUID)
        );
    }

    /**
     * This method is accepting the custom object name to return latest version custom object.
     * @param customObjectName
     * @return latest version custom object matched with custom object name.
     */
    public Object getCustomObjectByLatestVersion(String customObjectName) {
        int customObjectVersion = getLatestVersionCustomObject(customObjectName);
        return getObjectFromMap(customObjectName, customObjectVersion);
    }

    /**
     * This method is accepting custom object name and find the latest version custom object in map.
     * @param customObjectName
     * @return latest version number
     */
    public int getLatestVersionCustomObject(String customObjectName) {
        // Find the maximum version for the given custom object name
        AtomicInteger maxVersion = new AtomicInteger();
        customObjectMap.values().stream()
                .filter(customObject -> customObject.getCustomObjectName().equals(customObjectName))
                .forEach(customObject -> maxVersion.set(
                        Math.max(maxVersion.get(), customObject.getCustomObjectVersion())));
        return maxVersion.get();
    }

    /**
     * This method is accepting custom object and version to retrive specific version object.
     * @param customObjectName
     * @param customObjectVersion
     * @return return specific version object.
     */
    public Object getSpecificCustomObject(String customObjectName, int customObjectVersion) {
        kafkaProducerAndConsumer.kafkaConsumer();
        return getObjectFromMap(customObjectName, customObjectVersion);
    }

    /**
     * This method is accepting custom object name and version to retrive data from map.
     * @param customObjectName
     * @param customObjectVersion
     * @return specific custom object.
     */
    private Object getObjectFromMap(String customObjectName, int customObjectVersion) {
        // Find the custom object by name and version
        return customObjectMap.values().stream()
                .filter(customObject -> customObject.getCustomObjectName().equals(customObjectName)
                        && customObject.getCustomObjectVersion() == customObjectVersion)
                .findFirst().orElse(null);
    }
}
