package com.sree.document.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * This bean class is to accept input and pass to kafka.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomObject {

    private String customObjectName;
    private UUID customObjectUUID;
    private int customObjectVersion;
    private boolean isCustomObjectPresent;
    private Instant customObjectCreatedDateAndTime = Instant.now();
    private String customObjectCreatedBy;
    private Instant customObjectUpdatedDateAndTime = Instant.now();
    private String customObjectUpdatedBy;

}
