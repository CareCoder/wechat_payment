package com.itstyle.service;

import com.itstyle.domain.FileResource.FileResource;
import com.itstyle.domain.FileResource.FileResourceBo;
import com.itstyle.mapper.FileResourceMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FileResourceService {
    @Value("${filePath}")
    private String filePath;
    @Value("${fileUrlPrefix}")
    private String fileUrlPrefix;

    @Resource
    private FileResourceMapper fileResourceMapper;

    public String upload(@RequestParam("file") MultipartFile file, String uuid) {
        if (file.isEmpty()) {
            return "";
        }
        if (StringUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
        }
        try {
            if (file.getSize() > 0) {
                //获取文件信息和保存
                String fileStr = file.getOriginalFilename();
                //这里获取到的文件名,可能是路径,所以需要把路径删除了
                fileStr = fileStr.substring(fileStr.lastIndexOf("\\") + 1, fileStr.length());
                String suffix = fileStr.substring(fileStr.lastIndexOf(".") + 1);
                String fileName = uuid + "." + suffix;
                File filePath = new File(this.filePath);
                if (! filePath.exists()) {
                    filePath.mkdir();
                }
                File saveFile = new File(filePath.getAbsolutePath() + File.separator + fileName);
                file.transferTo(saveFile);
                //保存文件信息到数据库
                FileResource fileResource = new FileResource();
                fileResource.setFileName(fileName);
                fileResource.setOriginalName(fileStr);
                fileResource.setSuffix(suffix);
                fileResource.setUuid(uuid);
                fileResource.setCreateTime(new Date());
                fileResource.setModifyTime(new Date());
                fileResourceMapper.save(fileResource);
            }
        } catch (Exception e) {
            log.error("upload error ", e);
        }
        return uuid;
    }

    public ResponseEntity<byte[]> downloadByUuid(String uuid) {
        ResponseEntity<byte[]> entity = null;
        try {
            FileResourceBo bo = getByUuid(uuid);
            File file = new File(filePath + bo.getFileName());

            //读取文件字节数组
            byte[] res = FileUtils.readFileToByteArray(file);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/octet-stream");
            headers.add("Connection", "close");
            headers.add("Accept-Ranges", "bytes");
            headers.add("Content-Disposition", "attachment;filename=" + new String(bo.getOriginalName().getBytes("GB2312"), "ISO8859-1"));
            //将文件字节数组，header，状态码封装到ResponseEntity
            entity = new ResponseEntity<>(res, headers, HttpStatus.OK);
        } catch (IOException e) {
            log.error("downloadByUuid error", e);
        }
        return entity;

    }

    public FileResourceBo get(Long id) {
        FileResource fileResource = fileResourceMapper.findOne(id);
        return convertToBo(fileResource);
    }

    public List<FileResourceBo> findAll() {
        List<FileResource> fileResources = fileResourceMapper.findAll();
        return convertToBos(fileResources);
    }

    public FileResourceBo getByUuid(String uuid) {
        FileResource fileResource = fileResourceMapper.getByUuid(uuid);
        return convertToBo(fileResource);
    }

    public void deleteByUuid(String uuid) {
        FileResourceBo bo = getByUuid(uuid);
        String fileName = filePath + bo.getFileName();
        File file = new File(fileName);
        FileUtils.deleteQuietly(file);
        fileResourceMapper.deleteByUuid(uuid);
    }

    /**
     * 根据新图片名删除指定图片
     * @param fileName
     */
    public void deleteByFileName(String fileName){
        File file = new File(fileName);
        FileUtils.deleteQuietly(file);
        FileResource fileResource = fileResourceMapper.getByFileName(fileName.substring(fileName.lastIndexOf("/") + 1,fileName.length()));
        fileResourceMapper.deleteByUuid(fileResource.getUuid());
    }

    private List<FileResourceBo> convertToBos(List<FileResource> fileResources) {
        List<FileResourceBo> bos = new ArrayList<>();
        if (fileResources == null || fileResources.isEmpty()) {
            return bos;
        }
        fileResources.forEach(f ->
                bos.add(convertToBo(f))
        );
        return bos;
    }

    private FileResourceBo convertToBo(FileResource fileResource) {
        FileResourceBo bo = new FileResourceBo();
        BeanUtils.copyProperties(fileResource, bo);
        bo.setUrl(fileUrlPrefix + fileResource.getFileName());
        return bo;
    }
}
