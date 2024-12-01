package br.edu.toycenter.service;

import br.edu.toycenter.service.exceptions.InternalErrorException;
import br.edu.toycenter.service.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UploadService {

    public String uploadImage(MultipartFile multipartFile, String entity) throws InvalidFormatException, InternalError {
        if (multipartFile.isEmpty())
            throw new InvalidFormatException("The image can not be null.");

        try {

            byte[] bytes = multipartFile.getBytes();
            Path path = Paths.get( new File(".").getCanonicalPath()
                    + "/src/main/resources/static/images/"+ entity + "/" + multipartFile.getOriginalFilename());
            Files.write(path, bytes);

        } catch (IOException e) {
            throw new InternalErrorException("Unable to save image");
        }

        return "/images/"+ entity + "/" + multipartFile.getOriginalFilename();
    }
}
