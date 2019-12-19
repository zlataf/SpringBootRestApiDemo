/**
 * 
 * Spring Data Repository for FileInfo entity
 * Utilizes the EntityManager and Criteria approach.
 * @author zfomenko
 *
 */
package com.example.demo.repo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.FileInfo;


@Repository
@Transactional
public class FileInfoServiceRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileInfoServiceRepository.class);

	 @PersistenceContext
	 private EntityManager entityManager;

	 
	/**
	 * @param fileName
	 * @param fileType
	 * @param dateCreatedBefore
	 * @param dateCreatedAfter
	 * @param publicAccess
	 * @param sizeLarger
	 * @param sizeSmaller
	 * @return
	 */
	public List<Long> searchFileId(String fileName, String fileType, Timestamp dateCreatedBefore, Timestamp dateCreatedAfter,
		Boolean publicAccess, Long  sizeLarger, Long sizeSmaller) {
		LOGGER.info("Enter searchFileId");
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<FileInfo> query = cb.createQuery(FileInfo.class);
		Root<FileInfo> rt = query.from(FileInfo.class);
		
		//Constructing list of parameters
	    List<Predicate> predicates = new ArrayList<Predicate>();
	    
		if(fileName!=null) {
			LOGGER.debug("Search for file  name like %"+ fileName+"%");
			predicates.add(cb.like(rt.get("fileName"),"%"+ fileName+"%"));
		}
		if(fileType!=null) {
			LOGGER.debug("Search for file type "+ fileType);
			predicates.add(cb.equal(rt.get("fileType"),fileType));
		}
		if(dateCreatedBefore!=null) {
			LOGGER.debug("Search for file created before "+ dateCreatedBefore);
			predicates.add(cb.lessThanOrEqualTo(rt.get("uploadedTs"), dateCreatedBefore));
		}
		if(dateCreatedAfter!=null) {
			LOGGER.debug("Search for file created before "+ dateCreatedAfter);
			predicates.add(cb.greaterThanOrEqualTo(rt.get("uploadedTs"), dateCreatedAfter));
		}
		if(sizeLarger!=null) {
			LOGGER.debug("Search for file larger than "+ sizeLarger);
			predicates.add(cb.greaterThanOrEqualTo(rt.get("size"), (Comparable) sizeLarger)); 
		}
		if(sizeSmaller!=null) {
			LOGGER.debug("Search for file smaller than "+ sizeLarger);
			predicates.add(cb.lessThanOrEqualTo(rt.get("size"), (Comparable) sizeSmaller));
		}
		if(publicAccess!=null) {
			LOGGER.debug("Search for file with public access "+ publicAccess);
			predicates.add(cb.equal(rt.get("publicAccess"), publicAccess));
		}
		
		query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		query.select(rt.get("id"));
		
		List<?> res =  entityManager.createQuery(query).getResultList();
		LOGGER.debug("Exit searchFileId");
		return (List<Long>) res;
	}
	
    /**
     * @param record
     * @return
     */
    public Long persistFileInfo(FileInfo record) {
    	LOGGER.debug("Enter persistFileInfo");
    	 entityManager.persist(record);
    	 Long result = record.getId();
    	 LOGGER.debug("Exit persistFileInfo");
    	 return result;
    }

}
