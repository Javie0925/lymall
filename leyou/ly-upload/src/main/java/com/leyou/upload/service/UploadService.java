package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.common.enmus.ExceptionEnmu;
import com.leyou.common.exception.LyException;
import com.leyou.upload.config.UploadProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author javie
 * @date 2019/5/20 1:39
 */
@Service
@Slf4j
@EnableConfigurationProperties(UploadProperties.class)
public class UploadService {

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private UploadProperties prop;


    public String uploadFile(MultipartFile multipartFile) {

        try {
            //检验文件类型
            String contentType = multipartFile.getContentType();
            if (!prop.getAllowTypes().contains(contentType)) {
                throw new LyException(ExceptionEnmu.INVALIDE_FILE_TYPE);
            }
            //校验文件内容
            BufferedImage image = ImageIO.read(multipartFile.getInputStream());
            if (image == null) {
                throw new LyException(ExceptionEnmu.INVALIDE_FILE_TYPE);
            }
            //保存文件到文件服务器
            String extension = StringUtils.substringAfterLast(multipartFile.getOriginalFilename(), ".");
            StorePath storePath = storageClient.uploadFile(multipartFile.getInputStream(), multipartFile.getSize(), extension, null);
            // 返回路径
            return prop.getBaseUrl()+storePath.getFullPath();
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new LyException(ExceptionEnmu.FILE_UPLOAD_FAIL);
        }
    }
}
