package com.itstyle.service;

import com.itstyle.domain.car.manager.CarNumVo;
import com.itstyle.domain.car.manager.enums.CarNumType;
import com.itstyle.mapper.CarNumMapper;
import com.itstyle.utils.enums.Status;
import com.itstyle.utils.hibernate.BaseDaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.UUID;

@Service
public class CarNumService extends BaseDaoService<CarNumVo, Long> {
    @Resource
    private CarNumMapper carNumMapper;
    @Resource
    private FileResourceService fileResourceService;

    public CarNumService() {
        jpaRepository = carNumMapper;
    }

    public int upload(MultipartFile file, String carNum, CarNumType carNumType) {
        int status = Status.NORMAL;
        String uuid = UUID.randomUUID().toString();
        CarNumVo carNumVo = new CarNumVo();
        carNumVo.setCarNum(carNum);
        carNumVo.setUuid(uuid);
        carNumVo.setCarNum1Type(CarNumVo.buildCarNumAndType(carNum, carNumType));
        try {
            carNumMapper.save(carNumVo);
            fileResourceService.upload(file, uuid);
        } catch (Exception e) {
            status = Status.WARN_ALREAD_EXIST;
        }
        return status;
    }

    public ResponseEntity<byte[]> findByCarNumAndType(String carNum, CarNumType carNumType) {
        CarNumVo vo = query(carNum, carNumType);
        String uuid = vo.getUuid();
        return fileResourceService.downloadByUuid(uuid);
    }

    public void delete(Long id) {
        CarNumVo one = carNumMapper.findOne(id);
        carNumMapper.delete(id);
        fileResourceService.deleteByUuid(one.getUuid());
    }

    public CarNumVo query(String carNum, CarNumType carNumType) {
        return carNumMapper.findByCarNum1Type(CarNumVo.buildCarNumAndType(carNum, carNumType));
    }
}
