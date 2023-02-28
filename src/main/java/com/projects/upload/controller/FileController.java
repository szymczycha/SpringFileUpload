package com.projects.upload.controller;

import com.projects.upload.message.ResponseMessage;
import com.projects.upload.model.FileData;
import com.projects.upload.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FileController {
    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
        fileService.init();

    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("name") String name){
        try{
            fileService.save(file);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseMessage(file.getOriginalFilename()));
        }catch (Exception e){
            System.out.println(file);
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseMessage(e.getMessage()));
        }
    }

    @GetMapping("/files")
    private ResponseEntity<List<FileData>> listFiles() {

        System.out.println("/files");
        // ZADANIE:
        // z użyciem funkcji getAll() z serwisu
        // pobierz listę plików i przetwórz ją na List<FileData>
        // tak aby uzyskać informacje o nazwie i urlu pliku
        // wykorzystaj Streams
        // na koniec dodaj Response jak podczas post-a
        List<FileData> list = new ArrayList<>();
        try{
            List<Path> pathList = fileService.getAll();
//            list = pathList.stream()
//                    .map(path -> new FileData(path.getFileName().toString(), path.toFile().getPath()))
//                    .toList();
            pathList.stream().forEach(path -> list.add(new FileData(path.getFileName().toString(), path.toFile().getPath())));

        }catch (Exception e){
            System.out.println(e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(list);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(list);
    }

    @GetMapping("/files/{fileName}")
    private ResponseEntity<Resource> getFile(@PathVariable String fileName){
        System.out.println("/files/"+fileName);

        Resource file = fileService.download(fileName);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_JPEG)
                .body(file);
    }
}
