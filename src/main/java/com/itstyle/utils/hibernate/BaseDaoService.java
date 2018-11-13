package com.itstyle.utils.hibernate;

import com.itstyle.utils.BeanUtilIgnore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class BaseDaoService<BEAN, ID extends Serializable> {
    protected JpaRepository<BEAN, ID> jpaRepository;

    public List<BEAN> list() {
        return jpaRepository.findAll();
    }

    public void add(BEAN bean) {
        jpaRepository.save(bean);
    }

    public void delete(ID id) {
        jpaRepository.delete(id);
    }

    public BEAN findById(ID id) {
        return jpaRepository.findOne(id);
    }

    public void update(ID id, BEAN bean) {
        BEAN one = jpaRepository.findOne(id);
        BeanUtilIgnore.copyPropertiesIgnoreNull(bean, one);
        jpaRepository.save(one);
    }

    public JpaRepository<BEAN, ID> getJpaRepository() {
        return jpaRepository;
    }

    public void setJpaRepository(JpaRepository<BEAN, ID> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
}
