package com.itstyle.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itstyle.common.YstCommon;
import com.itstyle.dao.RedisDao;
import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.domain.car.manager.enums.ChargeSituation;
import com.itstyle.domain.car.manager.enums.ChargeType;
import com.itstyle.domain.park.ParkCar;
import com.itstyle.domain.park.ParkCarOrder;
import com.itstyle.domain.park.enums.ParkCarStatus;
import com.itstyle.domain.report.ChargeRecord;
import com.itstyle.task.AssessTokenTask;
import com.itstyle.utils.TemplateUtils;
import com.itstyle.utils.enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ParkCarService {
    private static final Logger log = LoggerFactory.getLogger(ParkCarService.class);
    @Resource
    private RedisDao redisDao;
    @Resource
    private AssessTokenTask assessTokenTask;
    @Resource
    private ChargeRecordService chargeRecordService;

    private static Gson gson = new Gson();

    public List<ParkCar> polling(String mcNo) {
        Map<String, ParkCar> parkCarMap = readRedis(mcNo);
        if (parkCarMap != null) {
            deleteRedis(mcNo);
            return parkCarMap.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<ParkCar> pollAll() {
        try {
            String json = getAllDelete();
            Map<String, ParkCar> parkCarMap = gson.fromJson(json, new TypeToken<Map<String, ParkCar>>() {
            }.getType());
            if (parkCarMap != null) {
                return parkCarMap.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
            }
        } catch (IOException e) {
            log.error("[ParkCarService] pollAll error", e);
        }
        return new ArrayList<>();
    }

    /**
      上传车辆信息
     */
    public int uploadBill(String mcNo, String carNo, Long operTime, Integer fee, String openId, Long enterTime,CarType carType) {
        log.info("[ParkCarService] uploadBill mcNo = {}, carNo = {}, operTime= {}, fee = {}, openId = {}, carType={}"
                , mcNo, carNo, operTime, fee, openId,carType);
        try {
            ParkCarOrder order = readRedisOrder(openId);
            order.mcNo = mcNo;
            order.carNo = carNo;
            order.operTime = operTime;
            order.fee = fee;
            order.enterTime = enterTime;
            order.carType = carType;
            writeRedisOrder(openId, order);
            TemplateUtils.createOrder(queryPay(openId), assessTokenTask.getAssessToken(), false);
        } catch (Exception e) {
            log.error("[ParkCarService] uploadBill error", e);
            return Status.ERROR;
        }
        return Status.NORMAL;
    }


    /**
     * 绑定车牌
     */
    public int uploadInfo(String carNo, String openId) {
        log.info("[ParkCarService] uploadInfo carNo = {}, openId = {}", carNo, openId);
        try {
            ParkCarOrder order = readRedisOrder(openId);
            order.carNo = carNo;
            order.operTime = System.currentTimeMillis();
            ParkCar parkCar = ParkCarOrder.convert(order);
            Map<String, ParkCar> parkCarMap = new HashMap<>();
            parkCarMap.put(order.mcNo, parkCar);
            writeRedisOrder(openId, order);
            writeRedis(order.mcNo, parkCarMap);
        } catch (Exception e) {
            log.error("[ParkCarService] uploadInfo error", e);
            return Status.ERROR;
        }
        return Status.NORMAL;
    }


    /**
     * 车辆准备入库
     */
    public int init(String mcNo, String openId) {
        log.info("[ParkCarService] init mcNo = {}, openId = {}", mcNo, openId);
        try {
            Map<String, ParkCar> parkCars = readRedis(mcNo);

            if (parkCars == null) {
                parkCars = new HashMap<>();
            }
            if (parkCars.containsKey(openId)) {
                log.error("[ParkCarService] init already exist warn");
                return Status.WARN_ALREAD_EXIST;
            }
            ParkCar parkCar = new ParkCar();
            parkCar.openId = openId;
            parkCar.operTime = System.currentTimeMillis();
            parkCar.mcNo = mcNo;
            parkCar.status = ParkCarStatus.INIT;

            parkCars.put(openId, parkCar);
            writeRedis(mcNo, parkCars);
            makeOrderRecord(parkCar);
        } catch (Exception e) {
            log.error("[ParkCarService] init error", e);
            return Status.ERROR;
        }
        return Status.NORMAL;
    }


    /**
     * @param ready 如果ready是true代表可以推送信息，如果为false代表只生成订单信息
     *      *              车辆准备支付离场费用
     */
    public int ready(String mcNo, String openId, boolean ready) {
        log.info("[ParkCarService] ready mcNo = {}, openId = {}", mcNo, openId);
        try {
            Map<String, ParkCar> parkCars = readRedis(mcNo);

            if (parkCars == null) {
                parkCars = new HashMap<>();
            }
            if (parkCars.containsKey(openId)) {
                log.error("[ParkCarService] ready already exist warn");
                return Status.WARN_ALREAD_EXIST;
            }
            ParkCar parkCar = new ParkCar();
            parkCar.openId = openId;
            parkCar.operTime = System.currentTimeMillis();
            parkCar.mcNo = mcNo;
            parkCar.status = ParkCarStatus.READY;

            parkCars.put(openId, parkCar);
            if (ready) {
                writeRedis(mcNo, parkCars);
            }
            makeOrderRecord(parkCar);
        } catch (Exception e) {
            log.error("[ParkCarService] ready error", e);
            return Status.ERROR;
        }
        return Status.NORMAL;
    }


    /**
     * 车辆完成支付离场费用
     */
    public int done(String openId) {
        log.info("[ParkCarService] done openId = {}", openId);
        try {
            ParkCarOrder order = readRedisOrder(openId);
            ParkCar parkCar = ParkCarOrder.convert(order);
            Map<String, ParkCar> parkCars = new HashMap<>();
            parkCar.status = ParkCarStatus.DONE;
            parkCar.operTime = System.currentTimeMillis();
            parkCars.put(openId, parkCar);
            writeRedis(parkCar.mcNo, parkCars);
            TemplateUtils.createOrder(queryPay(openId), assessTokenTask.getAssessToken(), true);
            //上传费用记录
            chargeRecode(order);
        } catch (Exception e) {
            log.error("[ParkCarService] done error", e);
            return Status.ERROR;
        }
        delteredisOrder(openId);
        return Status.NORMAL;
    }


    /**
     * 查询车辆需要支付的离场费用
     */
    public ParkCarOrder queryPay(String openId) {
        log.info("[ParkCarService] queryPay doneopenId = {}", openId);
        ParkCarOrder order;
        try {
            order = readRedisOrder(openId);
        } catch (Exception e) {
            log.error("[ParkCarService] queryPay error", e);
            return null;
        }
        return order;
    }

    private void makeOrderRecord(ParkCar parkCar) {
        ParkCarOrder order = ParkCarOrder.make(parkCar);
        writeRedisOrder(parkCar.openId, order);
    }

    private void chargeRecode(ParkCarOrder order) {
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setCarNum(order.carNo);
        chargeRecord.setCarType(CarType.TEMP_CAR_A);
        chargeRecord.setCarRealType(order.carType);
        chargeRecord.setChargeType(ChargeType.ONLINE_PAYMENT);
        chargeRecord.setEnterTime(order.enterTime);
        chargeRecord.setFee(order.fee);
        chargeRecord.setLeaveTime(System.currentTimeMillis());
        chargeRecord.setChargePersonnel("微信支付");
        chargeRecord.setChargeSituation(ChargeSituation.NORMAL_CHARGE);
        chargeRecord.setReceivableFee(chargeRecord.getFee());
        chargeRecord.setDiscountAmount(0);
        chargeRecordService.upload(chargeRecord);
    }

    private static String PARK_SERVICE_REDIS_KEY = "PARK_SERVICE";

    private Map<String, ParkCar> readRedis(String mcNo) {
        String json = redisDao.hget(PARK_SERVICE_REDIS_KEY, mcNo);
        return gson.fromJson(json, new TypeToken<Map<String, ParkCar>>() {
        }.getType());
    }

    private void writeRedis(String mcNo, Map<String, ParkCar> parkCars) {
        String json = gson.toJson(parkCars);
        redisDao.hset(PARK_SERVICE_REDIS_KEY, mcNo, json);
    }

    private void deleteRedis(String mcNo) {
        redisDao.hdelete(PARK_SERVICE_REDIS_KEY, mcNo);
    }

    private String getAllDelete() throws IOException {
        return redisDao.execute("getAllParkCarDelete.lua", PARK_SERVICE_REDIS_KEY, YstCommon.INNER_MC_NO);
    }

    private static String PARK_SERVICE_ORDER_REDIS_KEY = "PARK_SERVICE_ORDER";

    private ParkCarOrder readRedisOrder(String openId) {
        String json = redisDao.hget(PARK_SERVICE_ORDER_REDIS_KEY, openId);
        return gson.fromJson(json, ParkCarOrder.class);
    }

    private void writeRedisOrder(String openId, ParkCarOrder order) {
        redisDao.hset(PARK_SERVICE_ORDER_REDIS_KEY, openId, gson.toJson(order));
    }

    private void delteredisOrder(String openId) {
        redisDao.hdelete(PARK_SERVICE_ORDER_REDIS_KEY, openId);
    }
}
