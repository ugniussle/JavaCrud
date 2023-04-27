package lt.ku.javacrud.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {
    public void store(MultipartFile file, String outputFilename) {
        try {
            Path path = Paths.get("uploads").resolve(outputFilename).normalize().toAbsolutePath();
            System.out.println(path);

            if(!Paths.get("uploads").toFile().exists()) {
                Files.createDirectory(Paths.get("uploads"));
            }

            Files.copy(file.getInputStream(), path);
        } catch (java.io.IOException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public Resource load(String filename) {
        Resource resource = null;
        try {
            Path path = Paths.get("uploads").resolve(filename).normalize().toAbsolutePath();
            System.out.println(path);

            resource = new UrlResource(path.toUri());
        } catch (java.io.IOException e) {
            System.err.println(e);
            e.printStackTrace();
        }

        return resource;
    }
}
