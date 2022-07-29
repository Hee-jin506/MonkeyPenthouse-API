package com.monkeypenthouse.core.component;

import com.monkeypenthouse.core.connect.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ImageManager {

    private final S3Uploader s3Uploader;
    private static final int THUMBNAIL_WIDTH_PX = 1125;
    private static final int THUMBNAIL_HEIGHT_PX = 708;
    private static final String THUMBNAIL_NAME = "_thumbnail";

    public String uploadImageOnS3(final MultipartFile multipartFile, final String dirName) throws IOException {
        File file = multiPartToFile(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("파일 전환 실패"));
        String fileName = s3Uploader.upload(file, dirName);
        removeNewFile(file);
        return fileName;
    }

    // 1125 * 708 크기의 이미지 새로 생성하여 저장
    public String uploadThumbnailOnS3(final MultipartFile multipartFile) throws IOException {

        File file = multiPartToFile(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("파일 전환 실패"));

        String originName = file.getName().substring(0, file.getName().lastIndexOf("."));
        String ext = file.getName()
                .substring(file.getName().lastIndexOf(".") + 1);

        File resizedFile = createResizedImageFile(
                file,
                originName + THUMBNAIL_NAME + "." + ext,
                THUMBNAIL_WIDTH_PX,
                THUMBNAIL_HEIGHT_PX);

        String fileName = s3Uploader.upload(resizedFile, "thumbnail");

        removeNewFile(file);
        removeNewFile(resizedFile);
        return fileName;
    }

    // 원본 파일에 대해
    private File createResizedImageFile(final File originFile,
                                        final String newFileName,
                                        final int newWidth,
                                        final int newHeight) throws IOException {
        Image image = ImageIO.read(originFile);
        File resizedFile = new File(newFileName);

        // 이미지 리사이즈
        // Image.SCALE_DEFAULT : 기본 이미지 스케일링 알고리즘 사용
        // Image.SCALE_FAST    : 이미지 부드러움보다 속도 우선
        // Image.SCALE_REPLICATE : ReplicateScaleFilter 클래스로 구체화 된 이미지 크기 조절 알고리즘
        // Image.SCALE_SMOOTH  : 속도보다 이미지 부드러움을 우선
        // Image.SCALE_AREA_AVERAGING  : 평균 알고리즘 사용
        Image resizedImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);

        // 새 이미지  저장하기
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics g = newImage.getGraphics();
        g.drawImage(resizedImage, 0, 0, null);
        g.dispose();

        String ext = originFile.getName()
                .substring(originFile.getName().lastIndexOf(".") + 1);
        ImageIO.write(newImage, ext, resizedFile);
        return resizedFile;
    }

    // 로컬에 생성된 File 삭제
    private void removeNewFile(final File targetFile) {
        targetFile.delete();
    }

    // MultipartFile을 File 형태로 변환
    private Optional<File> multiPartToFile(final MultipartFile file) throws IOException {
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
