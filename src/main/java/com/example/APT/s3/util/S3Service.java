package com.example.APT.s3.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    // Config에서 s3Client라는 이름으로 등록된 bean을 사용한다.
//    @Autowired
    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        // (1) MultipartFile을 File로 변환
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환에 실패했습니다."));

        // (2) 파일 이름 중복을 방지하기 위해 UUID 값으로 설정(현재 DB의 길이 제한으로 UUID값만 저장하도록 해두었다. 필요에 따라 수정 예정)
        String randomName = UUID.randomUUID().toString();
        String fileName = dirName + "/" + randomName;

        // (3)S3에 파일을 업로드. 업로드 완료 여부와 관계 없이 (1)에서 임시 경로에 생성된 파일을 삭제
        try {
            return putS3(uploadFile, fileName);
        } finally {
            removeNewFile(uploadFile);
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        // 임시 경로에 file을 생성한다.
        File convertFile = new File(System.getProperty("java.io.tmpdir"), file.getOriginalFilename());

        // MultipartFile의 내용을 convertFile에 작성한다.
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    private String putS3(File uploadFile, String fileName) {
        // S3에 파일을 업로드한다.
        s3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        // 업로드된 파일의 경로를 가져온다.
        return s3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }
}
