package com.wayfare.backend.controller;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.sas.BlobContainerSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.azure.storage.blob.models.BlobStorageException;
import com.wayfare.backend.model.TourListing;
import com.wayfare.backend.response.ResponseObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class AzureController {
    @Value("${AZURE_ACCOUNT_NAME}")
    private String AZURE_ACCOUNT_NAME;

    @Value("${AZURE_ENDPOINT_URL}")
    private String AZURE_ENDPOINT_URL;

    @Value("${AZURE_ACCOUNT_KEY}")
    private String AZURE_ACCOUNT_KEY;
    @GetMapping("/api/v1/azure/sas")
    public ResponseObject getSasKey() {
        ResponseObject result;
        String containerName = "test";
        StorageSharedKeyCredential credential = new StorageSharedKeyCredential(AZURE_ACCOUNT_NAME, AZURE_ACCOUNT_KEY);

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(AZURE_ENDPOINT_URL)
                .credential(credential)
                .buildClient();
        // Create a SAS token
        BlobContainerClient containerClient = blobServiceClient
                .getBlobContainerClient(containerName);
        String sasToken = createServiceSASContainer(containerClient);
        Map<String, String> azureInfo = new HashMap<>();
        azureInfo.put("accountName", AZURE_ACCOUNT_NAME);
        azureInfo.put("endPointUrl", AZURE_ENDPOINT_URL);
        azureInfo.put("sasToken", sasToken);
        if (sasToken != null)
            result = new ResponseObject(true, azureInfo);
        else
            result = new ResponseObject(false, "Key not generated");

        return result;
    }
    private String createServiceSASContainer(BlobContainerClient containerClient) {
        try {
            // Create a SAS token that's valid for 1 day, as an example
            OffsetDateTime expiryTime = OffsetDateTime.now().plusDays(1);

            // Assign read permissions to the SAS token
            BlobContainerSasPermission sasPermission = new BlobContainerSasPermission()
                    .setReadPermission(true)
                    .setWritePermission(true)  // Add write permission
                    .setAddPermission(true);   // Add upload permission

            BlobServiceSasSignatureValues sasSignatureValues = new BlobServiceSasSignatureValues(expiryTime, sasPermission)
                    .setStartTime(OffsetDateTime.now().minusMinutes(5));

            String sasToken = containerClient.generateSas(sasSignatureValues);
            return sasToken;
    }catch(BlobStorageException ex) {
        // Handle exception (log, throw, etc.)
        ex.printStackTrace(); // Example of logging the exception
        return null; // Or some default value or error message indicating failure
    }
    }
}

