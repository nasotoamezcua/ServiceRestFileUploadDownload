package com.soto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.soto.model.DBFileBase64;

@Repository
public interface IDBFileBase64Repository extends JpaRepository<DBFileBase64, String>{

}
