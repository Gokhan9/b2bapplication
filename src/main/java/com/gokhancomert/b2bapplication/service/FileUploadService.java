package com.gokhancomert.b2bapplication.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);

    // application.properties'tan dosya yükleme dizinini okur
    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * Yüklenen dosyayı sunucuya kaydeder, dosyanın erişilebilir URL'ini döner.
     * @param - file yüklenecek dosya(MultipartFile)
     * @return Kaydedilen dosyanın URL'i
     * @throws "IOException" Dosya kaydetme sırasında bir hata oluşursa..
     */
    private String uploadFile(MultipartFile file) throws IOException {
        //Dosya adını temizle ve benzersiz hale getir
        String originalFileName = Objects.requireNonNull(file.getOriginalFilename());
        String fileName = UUID.randomUUID().toString() + "_" + originalFileName.replaceAll("\\s+", "_");
        logger.info("Uploading file: {} as {}" , originalFileName, fileName);

        //Yükleme dizinini oluştur(yoksa)
        Path copyLocation = Paths.get(uploadDir + "/" + fileName);
        Files.createDirectories(copyLocation.getParent());

        //Dosyayı hedef konuma kopyalamak
        Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        logger.info("File saved to: {}", copyLocation.toAbsolutePath());

        //Dosyanın erişilebilir URL'ini oluştur ve dön
        //Bu URL, uygulamamızın çalıştığı adrese göre dinamik olarak oluşturulur..
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")// app.properties'de yer alan upload-dir ile eşleşmeli..
                .path(fileName)
                .toUriString();
    }
}
