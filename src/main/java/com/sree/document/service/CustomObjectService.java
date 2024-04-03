package com.sree.document.service;

import com.sree.document.models.CustomObject;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CustomObjectService {

    final public Map<Integer, CustomObject> customObjectMap = new ConcurrentHashMap<>();

    public void saveDocumentMetadata(CustomObject customObject) {
        // Get the next available key by incrementing the maximum key by 1
        int nextKey = customObjectMap.isEmpty() ? 1 :
                customObjectMap.keySet().stream().max(Comparator.naturalOrder()).orElse(0) + 1;
        customObjectMap.put(nextKey, customObject);
    }

    public Map<Integer, CustomObject> getAllCustomObjects() {
        return customObjectMap;
    }

    public CustomObject getCustomObjectById(int id) {
        return customObjectMap.get(id);
    }

    public void deleteCustomObject(UUID customObjectUUID) {
        // Remove the entry with matching UUID
        customObjectMap.entrySet().removeIf(
                entry -> entry.getValue().getCustomObjectUUID().equals(customObjectUUID)
        );
    }

    public Object getCustomObjectByLatestVersion(String customObjectName) {
        int customObjectVersion = getLatestVersionCustomObject(customObjectName);
        return getObjectFromMap(customObjectName, customObjectVersion);
    }

    public int getLatestVersionCustomObject(String customObjectName) {
        // Find the maximum version for the given custom object name
        AtomicInteger maxVersion = new AtomicInteger();
        customObjectMap.values().stream()
                .filter(customObject -> customObject.getCustomObjectName().equals(customObjectName))
                .forEach(customObject -> maxVersion.set(
                        Math.max(maxVersion.get(), customObject.getCustomObjectVersion())));
        return maxVersion.get();
    }

    public Object getSpecificCustomObject(String customObjectName, int customObjectVersion) {
        return getObjectFromMap(customObjectName, customObjectVersion);
    }

    private Object getObjectFromMap(String customObjectName, int customObjectVersion) {
        // Find the custom object by name and version
        return customObjectMap.values().stream()
                .filter(customObject -> customObject.getCustomObjectName().equals(customObjectName)
                        && customObject.getCustomObjectVersion() == customObjectVersion)
                .findFirst().orElse(null);
    }
}
