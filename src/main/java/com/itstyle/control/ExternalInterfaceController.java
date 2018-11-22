package com.itstyle.control;

import com.google.gson.Gson;
import com.itstyle.common.YstCommon;
import com.itstyle.dao.RedisDao;
import com.itstyle.domain.version.VersionInfo;
import com.itstyle.service.FileResourceService;
import com.itstyle.vo.incrementmonly.response.IncrementMonly;
import com.itstyle.vo.login.request.LoginRequest;
import com.itstyle.vo.login.reponse.LoginResponse;
import com.itstyle.exception.BusinessException;
import com.itstyle.service.AccountService;
import com.itstyle.service.ExternalInterfaceService;
import com.itstyle.utils.enums.Status;
import com.itstyle.vo.inition.response.Inition;
import com.itstyle.vo.syncarinfo.response.SynCarInfo;
import com.itstyle.vo.version.request.VersionRequest;
import com.itstyle.vo.version.response.VersionResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/external")
public class ExternalInterfaceController {

    private AccountService accountService;
    private RedisDao redisDao;
    private Gson gson;
    private FileResourceService fileResourceService;

    private ExternalInterfaceService externalInterfaceService;


    @Autowired
    public ExternalInterfaceController(AccountService accountService, RedisDao redisDao, Gson gson,
                                       FileResourceService fileResourceServic, ExternalInterfaceService externalInterfaceService) {
        this.accountService = accountService;
        this.redisDao = redisDao;
        this.gson = gson;
        this.fileResourceService = fileResourceServic;
        this.externalInterfaceService = externalInterfaceService;
    }


    /**
     * 外部登陆接口
     * */
    @PostMapping("/login")
    @ResponseBody
    public LoginResponse login(LoginRequest loginRequestVo) {
        log.info("[ExternalInterfaceController] login request param is {}", loginRequestVo);
        LoginResponse responseVo = new LoginResponse();
        responseVo.setServiceCode(loginRequestVo.getServiceCode());
        try {
            accountService.login(loginRequestVo.getUserName(), loginRequestVo.getPassword());
        } catch (BusinessException e) {
            String message = e.getMessage();
            responseVo.setErrorCode(Status.ERROR);
            responseVo.setErrorDesc(message);
        }
        responseVo.setErrorCode(Status.NORMAL);
        return responseVo;
    }

    /**
     * 同步车辆信息配置
     */
    @GetMapping("/synCarInfo")
    @ResponseBody
    public SynCarInfo synCarInfo() {
        return externalInterfaceService.synCarInfo();
    }

     /**
     * 获取全部配置
     */
    @GetMapping("/inition")
    @ResponseBody
    public Inition inition() {
        return externalInterfaceService.inition();
    }

    @PostMapping("/version/update")
    @ResponseBody
    public VersionResponse versionUpdate(VersionRequest versionRequest) {
        log.info("[ExternalInterfaceController] update request param is {}", versionRequest);
        String json = redisDao.get(YstCommon.VERSION_INFO);
        VersionResponse versionResponse = new VersionResponse();
        versionResponse.setServiceCode(versionRequest.getServiceCode());
        if (json == null) {
            versionResponse.setErrorCode(Status.ERROR);
            versionResponse.setErrorDesc("未上传apk到服务器，请上传之后下载");
            return versionResponse;
        }
        VersionInfo versionInfo = gson.fromJson(json, VersionInfo.class);
        if (StringUtils.isEmpty(versionInfo.getOldVersionCode())) { // 老版本号为空，代表是第一次请求
            versionResponse.setErrorCode(Status.NORMAL);
            versionResponse.setDownloadUrl("/external/version/download/" + versionInfo.getNewVersionCode());
            versionResponse.setUpdateContent(versionInfo.getUpdateContent());
            versionResponse.setVersionCode(versionInfo.getNewVersionCode());
            versionInfo.setOldVersionCode(versionInfo.getNewVersionCode());
            versionInfo.setDownload(true);
            redisDao.set(YstCommon.VERSION_INFO, gson.toJson(versionInfo));
            return versionResponse;
        } else if (StringUtils.isEmpty(versionRequest.getVersionCode())) {
            // 如果不是第一次请求该接口，那么app版本号不能为空，否则报错
            if (!StringUtils.isEmpty(versionInfo.getOldVersionCode())) {
                versionResponse.setErrorCode(Status.ERROR);
                versionResponse.setErrorDesc("版本号不能为空");
                return versionResponse;
            }
        }

        // 如果版本号不为空，需要验证该版本号是否正确，不能随便一个版本号都能下载，安全着想
        if (!versionRequest.getVersionCode().equals(versionInfo.getOldVersionCode())) {
            versionResponse.setErrorCode(Status.ERROR);
            versionResponse.setErrorDesc("版本号不正确");
            return versionResponse;
        }
        // 版本号不为空，并且版本号也正确，且新老版本号不一致，给予最新下载地址
        if (!versionRequest.getVersionCode().equals(versionInfo.getNewVersionCode())) {
            versionResponse.setErrorCode(Status.NORMAL);
            versionResponse.setDownloadUrl("/external/version/download/" + versionInfo.getNewVersionCode());
            versionResponse.setUpdateContent(versionInfo.getUpdateContent());
            versionResponse.setVersionCode(versionInfo.getNewVersionCode());
            versionInfo.setDownload(true);
            redisDao.set(YstCommon.VERSION_INFO, gson.toJson(versionInfo));
        } else {
            versionResponse.setErrorCode(Status.ERROR);
            versionResponse.setErrorDesc("无新版本需要更新");
        }
        return versionResponse;
    }

    @GetMapping("/version/download/{versionCode}")
    public ResponseEntity<byte[]> download(@PathVariable("versionCode") String versionCode) {
        return fileResourceService.downloadByUuid(versionCode);
    }

    /**
     * 同步车辆信息配置
     */
    @GetMapping("/incrementMonly")
    @ResponseBody
    public IncrementMonly incrementMonly(Long startTime, Long endTime) {
        return externalInterfaceService.incrementMonly(startTime, endTime);
    }

}
