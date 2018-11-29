package com.itstyle.control;

import com.google.gson.Gson;
import com.itstyle.common.YstCommon;
import com.itstyle.dao.RedisDao;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.domain.version.VersionInfo;
import com.itstyle.exception.AssertUtil;
import com.itstyle.exception.BusinessException;
import com.itstyle.service.FileResourceService;
import com.itstyle.utils.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Controller
@RequestMapping("/version")
public class VersionController {

    private static final String POINT = ".";

    private FileResourceService fileResourceService;
    private RedisDao redisDao;
    private Gson gson;

    @Autowired
    public VersionController(FileResourceService fileResourceService, RedisDao redisDao, Gson gson) {
        this.fileResourceService = fileResourceService;
        this.redisDao = redisDao;
        this.gson = gson;
    }

    @PostMapping("/upload")
    @ResponseBody
    public Response upload(@RequestParam("file") MultipartFile file, String updateContent, Integer versionCode) {
        AssertUtil.assertNotNull(file, () -> new BusinessException("文件不能为空"));
        AssertUtil.assertNotEmpty(updateContent, () -> new BusinessException("版本更新信息不能为空"));
        AssertUtil.assertNotNull(versionCode, () -> new BusinessException("版本号不能为空"));
        String filenamePrefix = getFilenamePrefix(file.getOriginalFilename());
        String uuid = UUID.randomUUID().toString();
        fileResourceService.upload(file, uuid);
        VersionInfo versionInfo = new VersionInfo();
        versionInfo.setFilename(filenamePrefix);
        versionInfo.setUpdateContent(updateContent);
        versionInfo.setVersionCode(versionCode);
        versionInfo.setUuid(uuid);
        redisDao.set(YstCommon.VERSION_INFO, gson.toJson(versionInfo));
        return Response.build(Status.NORMAL, null, null);
    }

    @GetMapping("/page")
    public String getVersionInfoPage(Model model) {
        String json = redisDao.get(YstCommon.VERSION_INFO);
        VersionInfo versionInfo = gson.fromJson(json, VersionInfo.class);
        model.addAttribute("version_info", versionInfo);
        return "/backend/version-info";
    }

    private String getFilenamePrefix(String filename) {
        AssertUtil.assertTrue(filename.contains(POINT), () -> new BusinessException("文件格式不正确"));
        return filename.substring(0, filename.lastIndexOf(POINT));
    }
}
