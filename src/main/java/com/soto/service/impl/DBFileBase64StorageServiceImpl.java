package com.soto.service.impl;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.soto.exception.FileStorageException;
import com.soto.exception.MyFileNotFoundException;
import com.soto.model.DBFileBase64;
import com.soto.repository.IDBFileBase64Repository;
import com.soto.service.IDBFileBase64StorageService;

@Service
public class DBFileBase64StorageServiceImpl implements IDBFileBase64StorageService {
	
	@Autowired
	IDBFileBase64Repository dbFileBase64Repository;

	@Override
	public DBFileBase64 storeFile(MultipartFile file) {
		//Normalizar nombre de archivo
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
		try {
			//Verificamos si el nombre del archivo contiene caracteres no válidos
			if(fileName.contains("..")) {
				throw new FileStorageException("¡Lo siento! El nombre del archivo contiene una secuencia de ruta no válida. " + fileName);
			}
			
		//Codificamos el archivo a base 64
		String encodedString = Base64.getEncoder().encodeToString(file.getBytes());
		
		//Creamos objeto DBFile y lo insertamos en la base de datos
		DBFileBase64 dbFileBase64 = new DBFileBase64(fileName, file.getContentType(), encodedString);
		return dbFileBase64Repository.save(dbFileBase64);
			
		}catch (IOException e) {
			throw new FileStorageException("No se pudo almacenar el archivo " + fileName + ". ¡Inténtalo de nuevo!", e);
		}
	}

	@Override
	public DBFileBase64 getFile(String fileId) {
		try {
			return dbFileBase64Repository.findOne(fileId);
		}catch (Exception e) {
			throw new MyFileNotFoundException("Archivo no encontrado: " + fileId, e);
		}
	}
}
