package com.monkeypenthouse.core.connect;

import com.amazonaws.Protocol;
import com.amazonaws.auth.internal.AWS4SignerUtils;
import com.amazonaws.util.DateUtils;
import lombok.RequiredArgsConstructor;

import org.jets3t.service.CloudFrontService;
import org.jets3t.service.CloudFrontServiceException;
import org.jets3t.service.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class CloudFrontManager {

    @Value("${cloud.aws.cloud-front.distribution-domain}")
    private String distributionDomain;

    @Value("${cloud.aws.cloud-front.key-file-path}")
    private String filePath;

    @Value("${cloud.aws.cloud-front.key-pair-id}")
    private String keyPairId;

    private final static long EXPIRED_TIME = 1000 * 60 * 2;

    public String getSignedUrlWithCannedPolicy(String S3ObjectKey) throws CloudFrontServiceException, IOException {
        String url = distributionDomain + "/" + S3ObjectKey;
        byte[] derPrivateKey = ServiceUtils.readInputStreamToBytes(new FileInputStream(filePath));

        long now = (new Date()).getTime();
        Date dateLessThan = new Date(now + EXPIRED_TIME) ;
        try {
            return CloudFrontService.signUrlCanned(url, keyPairId, derPrivateKey, dateLessThan);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
