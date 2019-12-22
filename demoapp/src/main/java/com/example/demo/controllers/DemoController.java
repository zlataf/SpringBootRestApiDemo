package com.example.demo.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.entities.FileInfo;
import com.example.demo.error.DemoAppExeption;
import com.example.demo.services.InfoDataService;
import com.exmple.demo.utilities.Utilities;

/**
 * DemoController REST Controller to handler incoming requests. It produces an
 * HTTP response as JSON. The API root-endpoint is : "/api/demo"
 * 
 * @author zfomenko
 *
 */
@RestController
@RequestMapping(value = "/api/demo", method = { RequestMethod.GET, RequestMethod.POST })
public class DemoController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DemoController.class);
	@Autowired
	InfoDataService infoDataService;

	@Value("${app.pathToUpload}")
	private String pathToUpload;

	/**
	 * This endpoint is to retrieve all records from the metadata table. This method
	 * allows you to get all the records from the metadata table. Should be used for
	 * small data sets, mainly for testing purposes.
	 * 
	 * @return ResponseEntity<List<FileInfo>>
	 */
	@GetMapping(value = "/getFilesMetaDataList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FileInfo>> getAllFilesInfo() {
		LOGGER.debug("/getFilesMetaDataList" + pathToUpload);
		List<FileInfo> result = infoDataService.findAllRecords();
		return ResponseEntity.ok().body(result);
	}

	/**
	 * This endpoint is to download a file by given file id.
	 * 
	 * @param id
	 * @return ResponseEntity<Resource>
	 */
	@GetMapping(value = "/downloadFile/{id}")
	public ResponseEntity<Resource> downloadFile(@PathVariable("id") Long id) {
		LOGGER.debug("/downloadFile/" + id);
		FileInfo result = infoDataService.findRecordById(id);
		if (result == null) {
			throw new DemoAppExeption(Utilities.NoDataForId + id);
		}
		String mimeType = result.getType();
		Resource resource;
		try {
			resource = infoDataService.downloadFile(pathToUpload + id);
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(resource);
		} catch (MalformedURLException e) {
			throw new DemoAppExeption(Utilities.NoFileForId + id);
		}

	}

	/**
	 * This endpoint is to find previously saved file metadata by the given file id
	 * 
	 * @param id
	 * @return ResponseEntity<FileInfo>
	 */
	@GetMapping(value = "/getFileMetaData/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FileInfo> getFileData(@PathVariable("id") Long id) {
		LOGGER.debug("/getFileMetaData/" + id);
		FileInfo result = infoDataService.findRecordById(id);
		if (result != null)
			return ResponseEntity.ok().body(result);
		else
			throw new DemoAppExeption(Utilities.NoDataForId + id);
	}

	/**
	 * This endpoint is to find file IDs with a search criterion
	 * 
	 * @param fileName     is optional
	 * @param fileType     is optional
	 * @param addedBefore  is optional
	 * @param addedAfter   is optional
	 * @param publicAccess is optional
	 * @param sizeLarger   is optional
	 * @param sizeSmaller  is optional
	 * @return ResponseEntity<List<Long>>
	 */
	@GetMapping(value = "/searchFileId", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Long>> searchFileId(@RequestParam(value = "fileName", required = false) String fileName,
			@RequestParam(value = "fileType", required = false) String fileType,
			@RequestParam(value = "addedBefore", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate addedBefore,
			@RequestParam(value = "addedAfter", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate addedAfter,
			@RequestParam(value = "publicAccess", required = false) Boolean publicAccess,
			@RequestParam(value = "sizeLarger", required = false) Long sizeLarger,
			@RequestParam(value = "sizeSmaller", required = false) Long sizeSmaller) {
		LOGGER.debug("/searchFileId/");
		if (fileName == null && fileType == null && addedBefore == null && addedAfter == null && sizeLarger == null
				&& sizeSmaller == null && publicAccess == null) {
			throw new DemoAppExeption(Utilities.EmptyParametersList);
		}

		Timestamp dateBefore = null;
		Timestamp dateAfter = null;
		if (addedBefore != null) {
			dateBefore = Timestamp.valueOf(addedBefore.atStartOfDay());
		}
		if (addedAfter != null) {
			dateAfter = Timestamp.valueOf(addedAfter.atStartOfDay());
		}
		List<Long> result = infoDataService.searchFileId(fileName, fileType, dateBefore, dateAfter, publicAccess,
				sizeLarger, sizeSmaller);
		return ResponseEntity.ok().body(result);

	}

	/**
	 * This endpoint is to upload file and persist file meta-data to DB
	 * 
	 * @param fileDescription is optional
	 * @param publicAccess    is required
	 * @param file            is required and should not be empty
	 * @return ResponseEntity<String>
	 */
	@PostMapping(value = "/uploadFile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> singleFileUpload(
			@RequestParam(value = "fileDescription", required = false) String fileDescription,
			@RequestParam(value = "publicAccess", required = true) Boolean publicAccess,
			@RequestParam(value = "file", required = true) MultipartFile file) {
		LOGGER.debug("/uploadFile");
		StringBuilder output = new StringBuilder();

		if (file.isEmpty()) {
			throw new DemoAppExeption(Utilities.EmptyFile);
		}

		try {
			Path path = Paths.get(file.getOriginalFilename());
			String mimeType = Files.probeContentType(path);

			Calendar calendar = Calendar.getInstance();
			Timestamp currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());
			FileInfo record = new FileInfo(file.getOriginalFilename(), fileDescription, mimeType, currentTimestamp,
					publicAccess, file.getSize());
			Long id = infoDataService.persistFileInfo(record);
			File fileToUpload = new File(pathToUpload, id.toString());
			file.transferTo(fileToUpload);
			output.append("Uploaded file: ").append(file.getOriginalFilename()).append(", id= ").append(id.toString());
		} catch (IllegalStateException | IOException e) {
			throw new DemoAppExeption(e.getLocalizedMessage());
		}

		return ResponseEntity.ok().body(output.toString());

	}

}
