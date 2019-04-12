package com.soto.service;

import org.springframework.web.multipart.MultipartFile;

import com.soto.model.DBFile;

public interface IDBFileStorageService {
	
	DBFile storeFile(MultipartFile file);
	DBFile getFile(String fileId);

}
