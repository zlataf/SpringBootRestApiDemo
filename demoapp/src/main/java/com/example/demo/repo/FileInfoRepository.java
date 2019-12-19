package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.FileInfo;

/**
 * 
 * Spring Data Repository for FileInfo entity
 * Utilizes @Query and @Param approach.
 * @author zfomenko
 *
 */
@Repository
@Transactional
public interface FileInfoRepository extends JpaRepository<FileInfo,Long>{
	/**
	 * This method is to obtain all records from the file uploads DB
	 * @return List<FileInfo>
	 */
	@Query(value = "select new FileInfo(f.id,f.fileName,f.description,f.type,f.uploadedTs, f.publicAccess, f.size) from FileInfo f ")
	List<FileInfo> findAllRecords();
	
    /**
     * This method is to find the file metadata by the given file id 
     * @param id
     * @return FileInfo
     */
    @Query(value = "select new FileInfo(f.id,f.fileName,f.description,f.type,f.uploadedTs, f.publicAccess, f.size) from FileInfo f where f.id = :id")
    FileInfo findRecordById(@Param("id") Long id);

    /**
     * This method is to find records for the files with fileName like %name%
     * @param name
     * @return List<FileInfo>
     */
    @Query(value = "select new FileInfo(f.id,f.fileName,f.description,f.type,f.uploadedTs, f.publicAccess, f.size) from FileInfo f where f.fileName = :name")
	List<FileInfo> findRecordByName(@Param("name") String name);

}
