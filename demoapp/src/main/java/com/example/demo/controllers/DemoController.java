package com.example.demo.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.entities.FileInfo;
import com.example.demo.services.InfoDataService;


/**
 * DemoController
 * REST Controller to handler incoming requests.
 * It produces an HTTP response as JSON.
 * The API root-endpoint is : "/api/demo" 
 * @author zfomenko
 *
 */
@RestController
@RequestMapping(value = "/api/demo", method = { RequestMethod.GET, RequestMethod.POST })
public class DemoController {

	// TODO: externalize as a configuration item
	private static final String PATH_TO_UPLOAD = "C://Users//zlata//Documents//ToUpload//"; 
	private static final Logger LOGGER = LoggerFactory.getLogger(DemoController.class);
	@Autowired
	InfoDataService infoDataService;

	/**
	 * This endpoint is to retrieve all records from the metadata table.
	 * This method allows you to get all the records from the metadata table.
	 * Should be used for small data sets, mainly for testing purposes.
	 * @return ResponseEntity<List<FileInfo>>
	 */
	@GetMapping(value = "/getFilesMetaDataList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FileInfo>> getAllFilesInfo() {
		LOGGER.debug("/getFilesMetaDataList");
		List<FileInfo> result = infoDataService.findAllRecords();
		return ResponseEntity.ok().body(result);
	}
	

	/**
	 * This endpoint is to download a file by given file id. 
	 * @param id 
	 * @return ResponseEntity<Resource>
	 */
	@GetMapping(value = "/downloadFile/{id}")
		public ResponseEntity<Resource> downloadFile(@PathVariable("id") Long id ) {
		LOGGER.debug("/downloadFile/"+id);
		FileInfo result = infoDataService.findRecordById(id);
		String mimeType =result.getType();

		Resource resource;
		try {
			resource = infoDataService.downloadFile(PATH_TO_UPLOAD + id);
			
			return ResponseEntity.ok()
	                .contentType(MediaType.parseMediaType(mimeType))
	                .body(resource);
		} catch (MalformedURLException | NullPointerException e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.badRequest().body(null);
		}

	}


	/**
	 * This endpoint is to find previously saved file metadata by the given file id 
	 * @param id
	 * @return ResponseEntity<FileInfo>
	 */
	@GetMapping(value = "/getFileMetaData/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FileInfo> getFileData(@PathVariable("id") Long id) {
		LOGGER.debug("/getFileMetaData/"+id);
		FileInfo result = infoDataService.findRecordById(id);
		//return new ResponseEntity<FileInfo>(result,HttpStatus.OK );
		if(result!=null)
			return ResponseEntity.ok().body(result);
		else
			return ResponseEntity.badRequest().body(result);
	}

	/**
	 * This endpoint is to find file ids 
	 * with a search criterion
	 * @param fileName is optional
	 * @param fileType is optional
	 * @param addedBefore is optional
	 * @param addedAfter is optional
	 * @param publicAccess is optional
	 * @param sizeLarger is optional
	 * @param sizeSmaller is optional
	 * @return ResponseEntity<List<Long>>
	 */
	@GetMapping(value = "/searchFileId", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Long>> searchFileId(@RequestParam(value = "fileName", required = false) String fileName,
			@RequestParam(value = "fileType", required = false) String fileType,
			@RequestParam(value = "addedBefore", required = false) String addedBefore,
			@RequestParam(value = "addedAfter", required = false) String addedAfter,
			@RequestParam(value = "publicAccess", required = false) Boolean publicAccess,
			@RequestParam(value = "sizeLarger", required = false) Long sizeLarger,
			@RequestParam(value = "sizeSmaller", required = false) Long sizeSmaller) {
		LOGGER.debug("/searchFileId/");
		if (fileName == null && fileType == null && addedBefore == null && addedAfter == null && sizeLarger == null
				&& sizeSmaller == null) {
			LOGGER.error("Empty parameters list in searchFileId");
			//return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			return ResponseEntity.badRequest().body(null);
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Timestamp dateBefore = null;
		Timestamp dateAfter = null;

		// set the SimpleDateFormat parsing to be strict
		dateFormat.setLenient(false);
		try {
			if (addedBefore != null) {
				dateBefore =new Timestamp( dateFormat.parse(addedBefore).getTime());
			}
			if (addedAfter != null) {
				dateAfter = new Timestamp(dateFormat.parse(addedAfter).getTime());
			}

			List<Long> result = infoDataService.searchFileId(fileName, fileType, dateBefore, dateAfter,
					publicAccess, sizeLarger, sizeSmaller);
			return ResponseEntity.ok().body(result);
		} catch (ParseException e) {
			LOGGER.error((e.getLocalizedMessage()== null) ? "Bad parameters for the searchFileId.": e.getLocalizedMessage());
			return ResponseEntity.badRequest().body(null);
		}

	}

	/**
	 * This endpoint is to upload file and persist file meta-data to DB
	 * 
	 * @param fileDescription is optional
	 * @param publicAccess is required
	 * @param file is required and should not be empty
	 * @return  ResponseEntity<String>
	 */
	@PostMapping(value = "/uploadFile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> singleFileUpload(
			@RequestParam(value = "fileDescription", required = false) String fileDescription,
			@RequestParam(value = "publicAccess", required = true) Boolean publicAccess,
			@RequestParam(value = "file", required = true) MultipartFile file) {
		LOGGER.debug("/uploadFile");
		StringBuilder output = new StringBuilder();

		if (file.isEmpty()) {
			output.append("The file is empty and will not be uploaded.");
			LOGGER.error(output.toString());
			return ResponseEntity.badRequest().body(output.toString());
		}

		try {
			Path path = Paths.get(file.getOriginalFilename());
			String mimeType = Files.probeContentType(path);
			output.append("File ").append(file.getOriginalFilename()).append(", fileDescription ")
					.append(fileDescription);
			Calendar calendar = Calendar.getInstance();
			Timestamp currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());
			FileInfo record = new FileInfo(file.getOriginalFilename(), fileDescription, mimeType, 
					currentTimestamp, publicAccess, file.getSize());
			Long id = infoDataService.persistFileInfo(record);
			File fileToUpload = new File(PATH_TO_UPLOAD, id.toString());
			file.transferTo(fileToUpload);

		} catch (IllegalStateException | IOException e) {
			output.append(e.getLocalizedMessage());
			return ResponseEntity.badRequest().body(output.toString());
		}

		return ResponseEntity.ok().body(output.toString());

	}

}
