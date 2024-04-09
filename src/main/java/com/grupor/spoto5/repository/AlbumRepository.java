package com.grupor.spoto5.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupor.spoto5.model.Album;

public interface AlbumRepository extends JpaRepository <Album, Long> {

}