package com.soto.service.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.soto.exception.FileStorageException;
import com.soto.exception.MyFileNotFoundException;
import com.soto.model.DBFile;
import com.soto.repository.IDBFileRepository;
import com.soto.service.IDBFileStorageService;

@Service
public class DBFileStorageServiceImpl  implements IDBFileStorageService{
	
	@Autowired
	private IDBFileRepository dbFileRepository;

	@Override
	public DBFile storeFile(MultipartFile file) {
		//Normalizar nombre de archivo
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
		try {
			//Verificamos si el nombre del archivo contiene caracteres no válidos
			if(fileName.contains("..")) {
				throw new FileStorageException("¡Lo siento! El nombre del archivo contiene una secuencia de ruta no válida. " + fileName);
			}
			
			//Creamos objeto DBFile y lo insertamos en la base de datos
			DBFile dbFile = new DBFile(fileName, file.getContentType(), file.getBytes());
			return dbFileRepository.save(dbFile);
		}catch (IOException e) {
			throw new FileStorageException("No se pudo almacenar el archivo " + fileName + ". ¡Inténtalo de nuevo!", e);
		}
	}

	@Override
	public DBFile getFile(String fileId) {
		try {
			return dbFileRepository.findOne(fileId);
		}catch (Exception e) {
			throw new MyFileNotFoundException("Archivo no encontrado: " + fileId, e);
		}
	}
}
