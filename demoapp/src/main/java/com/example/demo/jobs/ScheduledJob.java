package com.example.demo.jobs;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.entities.FileInfo;
import com.example.demo.services.InfoDataService;
import com.exmple.demo.utilities.Utilities;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Component
public class ScheduledJob {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledJob.class);
	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	InfoDataService infoDataService;

	@Value("#{'${app.emailRecipientList}'.split(',')}")
	private List<String> emailRecipientList;

	@Value("${app.emailSender}")
	private String emailSender;


	/**
	 * This method is to run a scheduled job as a cron task.
	 * Format : second minute hour (day of month) month (day of week)
	 */
	@Scheduled(cron = "0 * * * * *")
	public void run() {

		String[] emailArray = new String[emailRecipientList.size()];
		emailArray = emailRecipientList.toArray(emailArray);
		LOGGER.debug("Sending a email to " + emailArray);
        //Current time
		LocalDateTime now = LocalDateTime.now();
		//Current time - 1 hour
		LocalDateTime after = LocalDateTime.now().minusHours(1);

		Timestamp dateCreatedBefore = Timestamp.valueOf(now);
		Timestamp dateCreatedAfter = Timestamp.valueOf(after);
		// Search for file id's added for the last hour 
		List<Long> ids = infoDataService.searchFileId(null, null, 
				dateCreatedBefore, dateCreatedAfter, null, null,null);
		// Create a email body 
		StringBuffer emailBody =new StringBuffer();
		emailBody.append("New Files Uploaded:");
        for(Long id : ids) {
        	try {
        	 FileInfo metaData = infoDataService.findRecordById(id);
        	 emailBody.append(System.lineSeparator());
        	 emailBody.append("*******************************************************");
        	 emailBody.append(System.lineSeparator());
        	 emailBody.append("ID:"+metaData.getId());
        	 emailBody.append(", ORIGINAL NAME: "+metaData.getFileName());
        	 Long size = metaData.getSize()/1000;
        	 emailBody.append(", SIZE (KB):"+size);
        	 emailBody.append((metaData.getPublicAccess())?", PUBLIC ACCESS: true":" , PUBLIC ACCESS: false");
        	 emailBody.append(", TIME UPLOADED:"+metaData.getUploadedTs().toLocalDateTime());
        	 emailBody.append(System.lineSeparator());
        	}
        	catch(Exception e) {
        		emailBody.append(Utilities.NO_DATA_FOR_ID+id);        		
        	}
         }
        // Sending a scheduled email 
        SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(emailArray);
		msg.setSubject(Utilities.SCHEDULER_SUBJECT);
		msg.setText(emailBody.toString());
		msg.setFrom(emailSender);

		// TODO
		// Uncomment the next line after adding a correct credentials to the
		// application.properies file
		// javaMailSender.send(msg);

	}
}
