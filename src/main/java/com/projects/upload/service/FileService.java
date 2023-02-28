package com.projects.upload.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

public interface FileService {
    public void init();
    public void save(MultipartFile file);
    public Resource download(String filename);
    public void deleteAll();
    public List<Path> getAll();
}
