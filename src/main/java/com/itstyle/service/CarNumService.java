package com.itstyle.service;

import com.itstyle.domain.car.manager.CarNumVo;
import com.itstyle.domain.car.manager.enums.CarNumType;
import com.itstyle.mapper.CarNumMapper;
import com.itstyle.utils.enums.Status;
import com.itstyle.utils.hibernate.BaseDaoService;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
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

    public int upload(MultipartFile file, CarNumVo carNumVo) {
        int status = Status.NORMAL;
        String uuid = UUID.randomUUID().toString();
        carNumVo.setUuid(uuid);
        CarNumVo find = carNumMapper.findByCarNumAndCarNumTypeAndTime(carNumVo.getCarNum(), carNumVo.getCarNumType(), carNumVo.getTime());

        if (find != null) {
            return Status.WARN_ALREAD_EXIST;
        }
        try {
            carNumMapper.save(carNumVo);
            fileResourceService.upload(file, uuid);
        } catch (Exception e) {
            status = Status.WARN_ALREAD_EXIST;
        }
        return status;
    }

    public ResponseEntity<byte[]> download(String carNum, CarNumType carNumType, Long time) {
        CarNumVo vo = carNumMapper.findByCarNumAndCarNumTypeAndTime(carNum, carNumType, time);
        String uuid = vo.getUuid();
        return fileResourceService.downloadByUuid(uuid);
    }

    public ResponseEntity<byte[]> download(String path) {
        CarNumVo vo = carNumMapper.findByPath(path);
        String uuid = vo.getUuid();
        return fileResourceService.downloadByUuid(uuid);
    }

    public void delete(String path) {
        CarNumVo one = carNumMapper.findByPath(path);
        carNumMapper.delete(one.getId());
        fileResourceService.deleteByUuid(one.getUuid());
    }

    public List<CarNumVo> query(CarNumVo carNumVo) {
        return carNumMapper.findAll(Example.of(carNumVo));
    }
}
