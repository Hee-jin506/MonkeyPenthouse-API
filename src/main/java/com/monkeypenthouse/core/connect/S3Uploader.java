package com.monkeypenthouse.core.connect;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // MultipartFile을 전달받아 File로 전환한 후 S3에 업로드
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("파일 전환 실패"));
        return upload(uploadFile, dirName);
    }

    // File을 정해진 디렉토리 이름 밑에 S3에 저장 요청
    private String upload(File uploadFile, String dirName) {
        String newFileName = UUID.randomUUID().toString();
        // 파일 이름은 [dirname]/[filename]
        String dirFileName = dirName + "/" + newFileName;
        String uploadImageUrl = putS3(uploadFile, dirFileName);

        // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환하며 로컬에 파일 생성됨)
        removeNewFile(uploadFile);

        return newFileName;
    }

    // 파일을 S3에 업로
    private String putS3(File uploadFile, String fileName) {
        // bucket에 정해진 filename으로 파일 업로드
        // public read가 가능하도록 S3 업로드
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 생성된 File 삭제
    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) {
            System.out.println("파일이 삭제되었습니다.");
        }else {
            System.out.println("파일이 삭제되지 못했습니다.");
        }
    }

    // MultipartFile을 File 형태로 변환
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

}
