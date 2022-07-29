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

@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(final File file, final String dirName) throws IOException {
        // 랜덤으로 파일 이름 짓기
        String fileName = createRandomFileName(file, dirName);
        // S3에 저장
        putS3(file, dirName + "/" + fileName);
        return fileName;
    }

    // 파일 이름 랜덤 생성
    private String createRandomFileName(final File uploadFile, final String dirName) {
        return UUID.randomUUID().toString();
    }

    // 파일을 S3에 업로드
    private String putS3(final File uploadFile, final String fileName) {
        // bucket에 정해진 filename으로 파일 업로드
        // public read가 가능하도록 S3 업로드
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }


}
