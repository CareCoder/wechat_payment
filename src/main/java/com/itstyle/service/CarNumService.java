package com.itstyle.service;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.car.manager.CarNumQueryVo;
import com.itstyle.domain.car.manager.CarNumVo;
import com.itstyle.domain.car.manager.enums.CarNumExtVo;
import com.itstyle.mapper.CarNumExtMapper;
import com.itstyle.mapper.CarNumMapper;
import com.itstyle.utils.enums.Status;
import com.itstyle.utils.hibernate.BaseDaoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CarNumService extends BaseDaoService<CarNumVo, Long> {
    @Resource
    private CarNumMapper carNumMapper;
    @Resource
    private CarNumExtMapper carNumExtMapper;
    @Resource
    private FileResourceService fileResourceService;

    @PostConstruct
    private void init() {
        jpaRepository = carNumMapper;
    }

    public int upload(MultipartFile file, CarNumVo carNumVo, CarNumExtVo carNumExtVo) {
        int status = Status.NORMAL;
        String uuid = UUID.randomUUID().toString();
        carNumExtVo.setUuid(uuid);
        List<CarNumVo> find = carNumMapper.findAll(Example.of(carNumVo));
        if (find != null && !find.isEmpty()) {
            Optional<CarNumExtVo> any = find.stream().flatMap(e -> e.getCarNumExtVos().stream())
                    .filter(e -> e.getCarNumType() == carNumExtVo.getCarNumType()).findAny();
            if (any.isPresent()) {
                return Status.WARN_ALREAD_EXIST;
            }
            if (find.size() == 1) {
                carNumVo = find.get(0);
            }
        }

        carNumVo.getCarNumExtVos().add(carNumExtVo);
        try {
            carNumMapper.save(carNumVo);
            fileResourceService.upload(file, uuid);
        } catch (Exception e) {
            status = Status.WARN_ALREAD_EXIST;
        }
        return status;
    }

    public ResponseEntity<byte[]> download(CarNumVo carNumVo, CarNumExtVo carNumExtVo) {
        List<CarNumVo> all = carNumMapper.findAll(Example.of(carNumVo));
        if (all != null && !all.isEmpty()) {
            CarNumVo vo = all.get(0);
            String uuid = vo.getUuid(carNumExtVo.getCarNumType());
            return fileResourceService.downloadByUuid(uuid);
        }
        return null;
    }

    public ResponseEntity<byte[]> download(String path) {
        CarNumExtVo carNumExtVo = carNumExtMapper.findByPath(path);
        return fileResourceService.downloadByUuid(carNumExtVo.getUuid());
    }

    public void delete(String path) {
        CarNumExtVo carNumExtVo = carNumExtMapper.findByPath(path);
        carNumExtMapper.delete(carNumExtVo.getId());
        fileResourceService.deleteByUuid(carNumExtVo.getUuid());
    }

    public List<CarNumVo> query(CarNumVo carNumVo) {
        return carNumMapper.findAll(Example.of(carNumVo));
    }

    public List<CarNumVo> query(CarNumQueryVo queryVo) {
        PageRequest pageRequest = PageResponse.getPageRequest(queryVo.getPage(), queryVo.getLimit());
        Specification<CarNumVo> sp = (root, query, cb) -> {
            List<Predicate> predicate = new ArrayList<>();
            if (StringUtils.isNotEmpty(queryVo.getCarNum())) {
                predicate.add(cb.like(root.get("carNum").as(String.class), "%" + queryVo.getCarNum() + "%"));
            }
            if (queryVo.getCarType() != null) {
                predicate.add(cb.equal(root.get("carNumType").as(String.class), queryVo.getCarType()));
            }
            if (queryVo.getStartTime() != null) {
                predicate.add(cb.ge(root.get("time").as(Long.class), queryVo.getStartTime()));
            }
            if (queryVo.getEndTime() != null) {
                predicate.add(cb.le(root.get("time").as(Long.class), queryVo.getEndTime()));
            }
            query.where(predicate.toArray(new Predicate[0]));
            return query.getRestriction();
        };
        Page<CarNumVo> all = carNumMapper.findAll(sp, pageRequest);
        return all.getContent();
    }
}
