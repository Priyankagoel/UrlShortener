package com.germinait.assignment.urlshortener.service;

import com.germinait.assignment.urlshortener.dto.UrlDto;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface UrlService {
    List<UrlDto> getUrl();

    UrlDto getUrlById(Long urlId);

    UrlDto addUrl(UrlDto urlDto);

    String getLongUrl(String shortUrl);
}
