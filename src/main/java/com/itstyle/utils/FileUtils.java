package com.itstyle.utils;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.itstyle.domain.car.manager.CarInfoExcelModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author lijun
 */
@Slf4j
public class FileUtils {

    /**
     * 文件上传
     *
     * @param files
     */
    public static List<String> upload(String savePath, MultipartFile... files) throws IOException {
        List<String> fileNames = new ArrayList<>(files.length);
        //判断路径是否存在
        File dir = new File(savePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        for (MultipartFile file : files) {
            if (file != null) {
                String fileStr = file.getOriginalFilename();
                String fileName = fileStr.substring(fileStr.lastIndexOf('\\') + 1);
                File saveFile = new File(savePath + fileName);
                file.transferTo(saveFile);
                fileNames.add(fileName);
            }
        }
        return fileNames;
    }

    public static List<String> uploadList(String savePath, List<MultipartFile> fileList) throws IOException {
        MultipartFile[] multipartFiles = new MultipartFile[fileList.size()];
        fileList.toArray(multipartFiles);
        return upload3(savePath, multipartFiles);
    }

    /**
     * 文件上传
     * 文件名會變化
     *
     * @param files
     */
    public static List<String> renameUpload(String savePath, MultipartFile... files) throws IOException {
        List<String> fileNames = new ArrayList<>(files.length);
        //判断路径是否存在
        File dir = new File(savePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        for (MultipartFile file : files) {
            if (file != null) {
                String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                String fileName = UUID.randomUUID().toString() + suffix;
                File saveFile = new File(savePath + fileName);
                file.transferTo(saveFile);
                fileNames.add(fileName);
            }
        }
        return fileNames;
    }

    /**
     * 文件上传
     * 文件名會變化
     * 本地文件名1 | 返回原文件名1
     * 本地文件名2 | 返回原文件名2
     *
     * @param files
     */
    public static Map<String, String> upload2(String savePath, MultipartFile... files) throws IOException {
        Map<String, String> resultMap = new HashMap<>(files.length);
        //判断路径是否存在
        File dir = new File(savePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        for (MultipartFile file : files) {
            if (file != null && file.getSize() > 0) {
                String fileStr = file.getOriginalFilename();
                String oldFileName = fileStr.substring(fileStr.lastIndexOf('/') + 1);
                String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                String fileName = UUID.randomUUID().toString() + suffix;
                File saveFile = new File(savePath + fileName);
                file.transferTo(saveFile);
                resultMap.put(fileName, oldFileName);
            }
        }
        return resultMap;
    }

    public static List<String> upload3(String savePath, MultipartFile... files) throws IOException {
        List<String> fileNames = new ArrayList<>(files.length);
        //判断路径是否存在
        File dir = new File(savePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        for (MultipartFile file : files) {
            if (file != null) {
                String fileStr = file.getOriginalFilename();
                String fileSuffix = fileStr.substring(fileStr.lastIndexOf('.'));
                String fileName = UUID.randomUUID() + fileSuffix;
                File saveFile = new File(savePath + "/" + fileName);
                file.transferTo(saveFile);
                fileNames.add(fileName);
            }
        }
        return fileNames;
    }

    /**
     * 上传单个文件，不改变原文件名
     *
     * @param savePath
     * @param file
     * @return
     * @throws IOException
     */
    public static void uploadSingleFile(String savePath, MultipartFile file) throws IOException {
        File dir = new File(savePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (file != null) {
            File saveFile = new File(savePath + "/" + file.getOriginalFilename());
            file.transferTo(saveFile);
        }
    }


    /**
     * 生成excel响应体
     * @param data
     * @return
     */
    public static ResponseEntity<byte[]> buildExcelResponseEntity(List<? extends BaseRowModel> data, Class<? extends BaseRowModel> clazz, String fileName) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ResponseEntity<byte[]> entity = null;
        try {
            ExcelWriter excelWriter = new ExcelWriter(out, ExcelTypeEnum.XLSX);
            Sheet sheet = new Sheet(1, 0, clazz);
            excelWriter.write(data, sheet);
            excelWriter.finish();

            HttpHeaders headers = HttpUtils.getDownloadHttpHeaders(fileName);
            entity = new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("exportExcel error", e);
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                log.error("exportExcel error2", e);
            }
        }
        return entity;
    }
}
