package com.example.demo.services;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.example.demo.entities.FileInfo;
import com.example.demo.repo.FileInfoRepository;
import com.example.demo.repo.FileInfoServiceRepository;

import org.slf4j.Logger;


/**
 * InfoDataService
 * Combines different approaches to manipulate data 
 * and to separate business logic from
 * persistence layers and REST Controller layer.
 * 
 * @author zfomenko
 *
 */
@Service
public class InfoDataService {

	private static final Logger LOGGER = LoggerFactory.getLogger(InfoDataService.class);
	
	 
	@Autowired
    private FileInfoRepository fileInfoRepository;
	
	@Autowired
	private FileInfoServiceRepository  fileInfoServiceRepository;
	
	/**
	 * InfoDataService constructor
	 */
	public InfoDataService() {
		
	}
	
	/**
	 * Retrieves all records from the metadata table.
	 * Should be used for small data sets, mainly for testing purposes.
	 * @return List<FileInfo>
	 */

	public List<FileInfo> findAllRecords(){
		LOGGER.info("Enter findAllRecords");
    	List<FileInfo> result = fileInfoRepository.findAllRecords();  
    	LOGGER.info("Exit findAllRecords");
    	return result;
    }
	

    /**
     * This method is to find the file metadata by the given file id 
     * @param id
     * @return FileInfo
     */
    public FileInfo findRecordById(Long id) {
    	LOGGER.info("Enter findRecordById");
    	FileInfo result = fileInfoRepository.findRecordById(id);
    	LOGGER.info("Exit findRecordById");
    	return result;
    }


    /**
     *  This method is to find records for the files with fileName like %name%
     * @param name
     * @return List<FileInfo>
     */
    public List<FileInfo> findRecordByName(String name){
    	LOGGER.info("Enter findRecordByName");
    	List<FileInfo> result = fileInfoRepository.findRecordByName(name);
    	LOGGER.info("Exit findRecordByName");
    	return result;
    }    


    /**
     * This method is to persist file meta-data into DB
     * @param record
     * @return id Long 
     */
    public Long persistFileInfo(FileInfo record) {
    	LOGGER.info("Enter persistFileInfo");
     	 Long result = fileInfoServiceRepository.persistFileInfo(record);
    	 LOGGER.info("Exit persistFileInfo");
    	 return result;
    }
    

	/**
	 * This method is to search file ids 
	 * with a search criterion
	 * @param fileName
	 * @param fileType
	 * @param dateCreatedBefore
	 * @param dateCreatedAfter
	 * @param publicAccess
	 * @param sizeLarger
	 * @param sizeSmaller
	 * @return List<Long>
	 */
	public List<Long> searchFileId(String fileName, String fileType, Timestamp dateCreatedBefore, Timestamp dateCreatedAfter,
		Boolean publicAccess, Long  sizeLarger, Long sizeSmaller) {
		LOGGER.info("Enter searchFileId");
		
		List<Long> res = fileInfoServiceRepository.searchFileId(fileName, fileType, dateCreatedBefore, dateCreatedAfter,
				publicAccess, sizeLarger, sizeSmaller);
		LOGGER.info("Exit searchFileId");
		return (List<Long>) res;
	}

	/**
	 * This method is to download file from the server
	 * @param fileName  s
	 * @return Resource
	 * @throws MalformedURLException
	 */
	public Resource downloadFile(String fileName) throws MalformedURLException {
	            Path filePath = Paths.get(fileName);
	            Resource resource = new UrlResource(filePath.toUri());
	            if(resource.exists()) {
	                return resource;
	            } else {
	                throw new MalformedURLException("File not found " + fileName);
	            }
	         
	}

}
