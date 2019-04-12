package com.soto.service.impl;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.soto.exception.FileStorageException;
import com.soto.exception.MyFileNotFoundException;
import com.soto.property.FileStorageProperties;
import com.soto.service.IFileStorageService;

@Service
public class FileStorageServiceImpl implements IFileStorageService{
	
	private final Path fileStorageLocation;

	@Autowired
	public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
		
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
				.toAbsolutePath()
				.normalize();
		
		try {
			Files.createDirectories(this.fileStorageLocation);
		}catch(Exception e) {
			throw new FileStorageException("No se pudo crear el directorio donde se almacenarán los archivos cargados.", e);
		}
	}
	
	public String storeFile(MultipartFile file) {
		//Normalizar nombre de archivo
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
		try {
			//Verificamos si el nombre del archivo contiene caracteres no válidos
			if(fileName.contains("..")) {
				throw new FileStorageException("¡Lo siento! El nombre del archivo contiene una secuencia de ruta no válida. " + fileName);
			}
			
			//Copia el archivo a la ubicación de destino (Reemplazando el archivo existente con el mismo nombre)
			Path carpetaUbicacion = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), carpetaUbicacion, StandardCopyOption.REPLACE_EXISTING);
			return fileName;
			
		}catch (Exception e) {
			throw new FileStorageException("No se pudo almacenar el archivo " + fileName + ". ¡Inténtalo de nuevo!", e);
		}
	}
	
	public Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if(resource.exists()) {
				return resource;
			}else {
				throw new MyFileNotFoundException("Archivo no encontrado: " + fileName);
			}
		}catch (MalformedURLException e) {
			throw new MyFileNotFoundException("Archivo no encontrado: " + fileName, e);
		}
	}
}
