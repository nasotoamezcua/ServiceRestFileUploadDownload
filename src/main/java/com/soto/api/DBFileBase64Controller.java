package com.soto.api;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.soto.model.DBFileBase64;
import com.soto.model.UploadFileResponse;
import com.soto.service.IDBFileBase64StorageService;

@RestController
@RequestMapping("/fileDataBaseCode64")
public class DBFileBase64Controller {
	
	@Autowired
	private IDBFileBase64StorageService dbFileBase64StorageServicea;
	
	@PostMapping("/uploadFile")
	public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
		
		DBFileBase64 dbFileBase64 = dbFileBase64StorageServicea.storeFile(file);
		
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/fileDataBaseCode64")
				.path("/downloadFile/")
				.path(dbFileBase64.getId())
				.toUriString();
		
		return new UploadFileResponse(dbFileBase64.getFileName(), fileDownloadUri, file.getContentType(), file.getSize());
	}
	
	@PostMapping("/uploadMultipleFiles")
	public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files){
		return Arrays.asList(files)
				.stream()
				.map(file -> uploadFile(file))
				.collect(Collectors.toList());
	}
	
	@GetMapping("/downloadFile/{fileId}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileId){
		//Cargar Archivos como Resource
		DBFileBase64 dbFileBase64 = dbFileBase64StorageServicea.getFile(fileId);
		
		//Decodificar el archivo en base 64
		byte[] decodedBytes = Base64.getDecoder().decode(dbFileBase64.getFileBase64());
		
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(dbFileBase64.getFileType()))
				//.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFileBase64.getFileName() + "\"")
				.body(new ByteArrayResource(decodedBytes));
	}

}
