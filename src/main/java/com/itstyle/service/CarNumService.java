package com.itstyle.service;

import com.itstyle.domain.car.manager.CarNumVo;
import com.itstyle.domain.car.manager.enums.CarNumType;
import com.itstyle.mapper.CarNumMapper;
import com.itstyle.utils.hibernate.BaseDaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Service
public class CarNumService extends BaseDaoService<CarNumVo, Long> {
    @Resource
    private CarNumMapper carNumMapper;
    @Resource
    private FileResourceService fileResourceService;

    public CarNumService() {
        jpaRepository = carNumMapper;
    }

    public void upload(MultipartFile file, String carNum, CarNumType carNumType) {
        String uuid = fileResourceService.upload(file);
        CarNumVo carNumVo = new CarNumVo();
        carNumVo.setCarNum(carNum);
        carNumVo.setUuid(uuid);
        carNumVo.setCarNum1Type(CarNumVo.buildCarNumAndType(carNum, carNumType));
        carNumMapper.save(carNumVo);
    }

    public ResponseEntity<byte[]> findByCarNumAndType(String carNum, CarNumType carNumType) {
        CarNumVo vo = carNumMapper.findByCarNum1Type(CarNumVo.buildCarNumAndType(carNum, carNumType));
        String uuid = vo.getUuid();
        return fileResourceService.downloadByUuid(uuid);
    }
}
