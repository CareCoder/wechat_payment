package com.itstyle.control;

import com.itstyle.common.PageResponse;
import com.itstyle.common.YstCommon;
import com.itstyle.domain.account.Account;
import com.itstyle.domain.car.manager.CarNumExcelModel;
import com.itstyle.domain.car.manager.CarNumQueryVo;
import com.itstyle.domain.car.manager.CarNumVo;
import com.itstyle.domain.car.manager.FixedCarManager;
import com.itstyle.domain.car.manager.enums.CarNumExtVo;
import com.itstyle.domain.car.manager.enums.CarNumType;
import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.domain.caryard.ResponseAccessType;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.service.AccessTypeService;
import com.itstyle.service.CarNumService;
import com.itstyle.service.FeeTestService;
import com.itstyle.service.GlobalSettingService;
import com.itstyle.utils.FeeUtil;
import com.itstyle.utils.FileUtils;
import com.itstyle.utils.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/carnum")
public class CarNumController {
    @Resource
    private CarNumService carNumService;
    @Resource
    private GlobalSettingService globalSettingService;
    @Resource
    private AccessTypeService accessTypeService;
    @Resource
    private FeeTestService feeTestService;

    @GetMapping("/tempcarinfo.html")
    public String tempcarinfo(CarNumQueryVo queryVo, Model model) {
        if (queryVo.getPage() <= 0) {
            queryVo.setPage(1);
        }
        queryVo.setRecord(false);//已经缴费的不在显示
//        queryVo.setLeave(false);
        queryVo.setCarTypeLimit(CarType.TEMP_CAR_D.getValue());//限制为只查询临时车
        PageResponse pageResponse = carNumService.queryComplex(queryVo);
        List<CarNumVo> carNumVos = pageResponse.getData();
        carNumVos.forEach(e -> {
            List<CarNumExtVo> carNumExtVos = e.getCarNumExtVos();
            carNumExtVos = carNumExtVos.stream()
                    .filter(f -> f.getCarNumType() == CarNumType.ENTER_SMALL || f.getCarNumType() == CarNumType.ENTER_BIG)
                    .collect(Collectors.toList());
            carNumExtVos.sort(Comparator.comparingInt(e1 -> e1.getCarNumType().ordinal()));
            e.setCarNumExtVos(carNumExtVos);
        });
        model.addAttribute("carNumVos", carNumVos);
        model.addAttribute("queryVo", queryVo);
        List<FixedCarManager> fixedCars = globalSettingService.list(YstCommon.FIXEDCARMANAGER_KEY, FixedCarManager.class);
        model.addAttribute("fixedCars", fixedCars);
        List<ResponseAccessType> accessTypes = accessTypeService.listNoPage();
        //业务需求这里只需要入口通道
        accessTypes = accessTypes.stream().filter(e -> e.getChannelTypeId() % 2 == 1).collect(Collectors.toList());
        model.addAttribute("accessTypes", accessTypes);

        model.addAttribute("maxPage", Math.ceil((double)pageResponse.getCount() / 4));
        return "/backend/tempcarinfo";
    }

    public static void main(String[] args) {
        System.out.println(Math.ceil((double)7 / 4));
    }

    @RequestMapping("/inner/car-info.html")
    public String innerCarInfo(Model model) {
        List<FixedCarManager> fixedCars = globalSettingService.list(YstCommon.FIXEDCARMANAGER_KEY, FixedCarManager.class);
        model.addAttribute("fixedCars", fixedCars);
        List<ResponseAccessType> accessTypes = accessTypeService.listNoPage();
        model.addAttribute("accessTypes", accessTypes);
        return "/backend/inner-car-info";
    }

    @RequestMapping("/access/report.html")
    public String accessReport(Model model) {
        List<FixedCarManager> fixedCars = globalSettingService.list(YstCommon.FIXEDCARMANAGER_KEY, FixedCarManager.class);
        model.addAttribute("fixedCars", fixedCars);
        List<ResponseAccessType> accessTypes = accessTypeService.listNoPage();
        model.addAttribute("accessTypes", accessTypes);
        return "/backend/access-report";
    }

    @GetMapping("/tempcarinfo-payment.html")
    public String tempcarinfo(Long id, Model model, HttpSession session) {
        CarNumVo carNumVo = carNumService.findById(id);
        //如果这里客户端没有上传收费金额,则服务器生成收费信息
        if (!BooleanUtils.toBoolean(carNumVo.getRecord()) && carNumVo.getLeavePass() == null) {
            long now = System.currentTimeMillis();
            int fee = feeTestService.fetchCurCharge(carNumVo.getCarType(), carNumVo.getTime(), now);
            carNumVo.setFee(fee);
            carNumVo.setStopTime(now - carNumVo.getTime());
            carNumService.add(carNumVo);
        }
        carNumVo.getCarNumExtVos().sort(Comparator.comparingInt(e1 -> e1.getCarNumType().ordinal()));
        Long enterTime = carNumVo.getTime();
        Long leaveTime = carNumVo.getLTime();
        Account account = (Account)session.getAttribute(YstCommon.LOGIN_ACCOUNT);
        String userName = "";
        if (account != null) {
            userName = account.getUsername();
        }
        model.addAttribute("id", id);
        model.addAttribute("enterTime", enterTime);
        model.addAttribute("leaveTime", leaveTime);
        model.addAttribute("stopTime", carNumVo.getStopTime() == null ? "" :  FeeUtil.secondToTime(carNumVo.getStopTime()));
        model.addAttribute("userName", userName);
        model.addAttribute("fee", carNumVo.getFee() == null ? "" :FeeUtil.convert(carNumVo.getFee()));
        model.addAttribute("vo", carNumVo);
        for (int i = 0; i < carNumVo.getCarNumExtVos().size(); i++) {
            model.addAttribute("img"+i, carNumVo.getCarNumExtVos().get(i).getUuid());
        }
        return "/backend/tempcarinfo-payment";
    }

    @PostMapping("/tempcarinfo-payment/confirm")
    @ResponseBody
    public String tempcarinfoPaymentConfirm(Long id, HttpSession session) {
        log.info("收费放行 id = {}", id);
        Account account = (Account) session.getAttribute(YstCommon.LOGIN_ACCOUNT);
        return carNumService.tempcarinfoPaymentConfirm(id, account);
    }

    @RequestMapping("/upload")
    @ResponseBody
    public Response upload(@RequestParam("file") MultipartFile file, CarNumVo carNumVo,
                           CarNumExtVo carNumExtVo,
                           Long leaveTime,
                           Integer remainingParkingNum,//剩余的车位数
                           HttpServletRequest request) {
        int status = Status.NORMAL;
        try {
            //先更新剩余车位数
            if (remainingParkingNum != 10000) {
                //remainingParkingNum 这个值为10000的时候，你不要把这个剩余车位数进行更新了，这里主要是我这边用来判断当信息网络不通，延迟用来补发上传数据的逻辑处理
                globalSettingService.set(YstCommon.REMAINING_PARKING_NUM, remainingParkingNum);
            }
            carNumVo.setLTime(leaveTime);
            carNumExtVo.setTime(leaveTime);//这里不要mvc自动注入 是因为两个注入对象的param相同了
            status = carNumService.upload(file, carNumVo, carNumExtVo);
        } catch (Exception e) {
            return Response.build(status, "系统错误", null);
        }
        return Response.build(status, "", null);
    }

    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<byte[]> download(@RequestParam String path) {
        return carNumService.download(path);
    }

    @RequestMapping("/download2")
    @ResponseBody
    public ResponseEntity<byte[]> download2(CarNumVo carNumVo, CarNumExtVo carNumExtVo) {
        return carNumService.download(carNumVo, carNumExtVo);
    }

    @RequestMapping("/delete/{path}")
    @ResponseBody
    public Response delete(@PathVariable("path") String path) {
        try {
            carNumService.delete(path);
        } catch (Exception e) {
            return Response.build(Status.ERROR, "系统错误", null);
        }
        return Response.build(Status.NORMAL, "", null);
    }

    @RequestMapping("/deleteInner")
    @ResponseBody
    public Response deleteInner(@RequestBody Long[] ids) {
        try {
            carNumService.deleteInner(ids);
        } catch (Exception e) {
            return Response.build(Status.ERROR, "系统错误", null);
        }
        return Response.build(Status.NORMAL, "", null);
    }

    @RequestMapping("/query")
    @ResponseBody
    public Response query(CarNumVo carNumVo) {
        List<CarNumVo> vos;
        try {
            vos = carNumService.query(carNumVo);
        } catch (Exception e) {
            return Response.build(Status.ERROR, "系统错误", null);
        }
        return Response.build(Status.NORMAL, "", vos);
    }

    @RequestMapping("/list")
    @ResponseBody
    public PageResponse query(CarNumQueryVo queryVo) {
        queryVo.setLeave(true);
        Page<CarNumVo> page = carNumService.query(queryVo);
        return PageResponse.build(page);
    }

    @RequestMapping("/queryComplex")
    @ResponseBody
    public PageResponse queryComplex(CarNumQueryVo queryVo) {
        queryVo.setCarTypeLimit(CarType.VIP_CAR.getValue());//不做限制
        return carNumService.queryComplex(queryVo);
    }

    @RequestMapping("/watch-car-img")
    public String watchCarImg(@RequestParam Long id,
                              @RequestParam Integer limit,
                              Model model) {
        CarNumVo carNumVo = carNumService.findById(id);
        carNumVo.getCarNumExtVos().sort(Comparator.comparingInt(e1 -> e1.getCarNumType().ordinal()));
        if (limit > carNumVo.getCarNumExtVos().size()) {
            limit = carNumVo.getCarNumExtVos().size();
        }
        for (int i = 0; i < limit; i++) {
            model.addAttribute("img" + carNumVo.getCarNumExtVos().get(i).getCarNumType().ordinal(), carNumVo.getCarNumExtVos().get(i).getUuid());
        }
        return "/backend/watch-car-img";
    }

    @RequestMapping("exportExcel")
    public ResponseEntity<byte[]> exportExcel() {
        List<FixedCarManager> f = globalSettingService.list(YstCommon.FIXEDCARMANAGER_KEY, FixedCarManager.class);
        List<CarNumExcelModel> data = carNumService.list().stream().map(m -> CarNumExcelModel.convert(m, f)).collect(Collectors.toList());
        return FileUtils.buildExcelResponseEntity(data, CarNumExcelModel.class, "出入报表.xlsx");
    }
}
