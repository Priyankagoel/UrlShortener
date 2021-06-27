package com.germinait.assignment.urlshortener.dao;

import com.germinait.assignment.urlshortener.model.UrlModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlDao extends JpaRepository<UrlModel, Long> {
    UrlModel findByLongUrl(String longUrl);
    UrlModel findByShortUrl(String shortUrl);
}
