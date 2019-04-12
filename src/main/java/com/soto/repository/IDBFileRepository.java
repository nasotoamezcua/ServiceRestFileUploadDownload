package com.soto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.soto.model.DBFile;

@Repository
public interface IDBFileRepository extends JpaRepository<DBFile, String>{

}
