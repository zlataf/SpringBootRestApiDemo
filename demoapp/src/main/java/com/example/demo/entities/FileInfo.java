package com.example.demo.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * FileInfo class 
 * Entity to map the METADATA Table
 * 
 * @author zfomenko
 *
 */
@Entity
@Table(name = "METADATA")
public class FileInfo {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;
    @Column(name="FILENAME")
	private String fileName;
	@Column(name="FILEDESCRIPTION")
	private String description;
	@Column(name="FILETYPE")
	private String type;
	@Column(name="UPLOADED_TS") 
	private Timestamp uploadedTs;
	@Column(name="PUBLICACCESS")  
	private Boolean  publicAccess;
	@Column(name="SIZE")
	private Long size;
	
	/**
	 * FileInfo constructor
	 * 
	 */
	public FileInfo() {
		;
	}
	 
	 /**
     * FileInfo constructor
     * 
     * @param id
     * @param fileName
     * @param description
     * @param type
     */
    public FileInfo(Long  id, String fileName, String description, String type) {
    	this.id = id;
    	this.fileName = fileName;
    	this.description = description;
    	this.type = type;
    }

	/**
	 * FileInfo constructor
	 * 
	 * @param originalFilename
	 * @param fileDescription
	 * @param fileType
	 * @param uploadedTs
	 * @param publicAccess
	 * @param size
	 */
	public FileInfo(String originalFilename, String fileDescription, String fileType, 
			java.util.Date uploadedTs, boolean publicAccess, Long size) {
    	this.fileName = originalFilename;
    	this.description = fileDescription;
    	this.type = fileType;
    	this.size = size;
    	this.uploadedTs=(Timestamp) uploadedTs;
    	this.publicAccess=publicAccess;
	}
	/**
	 * FileInfo constructor
	 * 
	 * @param id
	 * @param originalFilename
	 * @param fileDescription
	 * @param fileType
	 * @param uploadedTs
	 * @param publicAccess
	 * @param size
	 */
	public FileInfo(Long id,String originalFilename, String fileDescription, String fileType,
			java.util.Date uploadedTs, boolean publicAccess, Long size) {
		this.id = id;
    	this.fileName = originalFilename;
    	this.description = fileDescription;
    	this.type = fileType;
    	this.size = size;
    	this.uploadedTs=(Timestamp) uploadedTs;
    	this.publicAccess=publicAccess;
	}	
	
	/**
	 * get id
	 * @return id
	 */
	public Long getId() {
		return this.id;
	}
	/**
	 * set id
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * get fileName
	 * @return
	 */
	public String getFileName() {
		return this.fileName;
	}
	/**
	 * set fileName
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * get type
	 * @return
	 */
	public String getType() {
		return this.type;
	}
	/**
	 * set tupe
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * get description
	 * @return
	 */
	public String getDescription() {
		return this.description;
	}
	/**
	 * set description
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * get size
	 * @return
	 */
	public Long getSize() {
		return this.size;
	}
	/**
	 * set size
	 * @param size
	 */
	public void setSize(Long size) {
		this.size = size;
	}
	/**
	 * get uploadedTs
	 * @return
	 */
	public Timestamp getUploadedTs() {
		return this.uploadedTs;
	}
	/**
	 * set uploadedTs
	 * @param uploadedTs
	 */
	public void setUploadedTs(Timestamp uploadedTs) {
		this.uploadedTs = uploadedTs;
	}
	/**
	 * get publicAccess
	 * @return
	 */
	public Boolean getPublicAccess() {
		return this.publicAccess;
	}
	/**
	 * set publicAccess
	 * @param publicAccess
	 */
	public void setPublicAccess(Boolean publicAccess) {
		this.publicAccess = publicAccess;
	}
	

}
