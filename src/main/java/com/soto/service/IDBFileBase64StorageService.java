package com.soto.service;

import org.springframework.web.multipart.MultipartFile;

import com.soto.model.DBFileBase64;

public interface IDBFileBase64StorageService {
	
	DBFileBase64 storeFile(MultipartFile file);
	DBFileBase64 getFile(String fileId);

}
