package com.projects.upload.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileServiceImpl implements FileService{
    private final Path root = Paths.get("uploads");
    @Override
    public void init() {
        try{
            Files.createDirectory(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resource download(String filename) {
        Path file = root.resolve(filename);
        Resource resource = null;
        try {
            resource = new UrlResource(file.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        if (resource.exists()) {
            return resource;
        } else {
            throw new RuntimeException("cant read file");
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public List<Path> getAll() {
        try {
            return (List<Path>) Files.list(this.root);
        } catch (IOException e) {
            throw new RuntimeException("cant get files");
        }
    }
}
