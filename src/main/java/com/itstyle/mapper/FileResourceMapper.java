package com.itstyle.mapper;

import com.itstyle.domain.FileResource.FileResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface FileResourceMapper extends JpaRepository<FileResource, Long> {
    FileResource getByUuid(String uuid);

    /**
     * 根据新图片名获取指定图片
     * @param fileName
     * @return
     */
    FileResource getByFileName(String fileName);
    /***
     * 删除文件
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM file_resource WHERE uuid=?1",nativeQuery = true)
    void deleteByUuid(String uuid);
}
