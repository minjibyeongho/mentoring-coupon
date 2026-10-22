package com.min.coupon.couponissuefile.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path storageDirectory;

    public FileStorageService(@Value("${coupon.file-storage.path}") String storagePath) {
        this.storageDirectory = Path.of(storagePath);
        try {
            Files.createDirectories(storageDirectory);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String save(String originalFileName, InputStream inputStream) {
        String storedFileName = UUID.randomUUID() + "_" + originalFileName;
        Path targetPath = storageDirectory.resolve(storedFileName);
        try {
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return storedFileName;
    }

    public Path load(String storedFileName) {
        return storageDirectory.resolve(storedFileName);
    }
}
