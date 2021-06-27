package com.germinait.assignment.urlshortener.controller;

import com.germinait.assignment.urlshortener.dto.UrlDto;
import com.germinait.assignment.urlshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
public class UrlController {

    @Autowired
    private UrlService urlService;

    @GetMapping("api/v1/get/url")
    @ResponseBody
    public List<UrlDto> getUrl() {

        return urlService.getUrl();
    }

    @GetMapping("api/v1/get/url/{urlId}")
    @ResponseBody
    public UrlDto getUrlById(@PathVariable final Long urlId) {
        return urlService.getUrlById(urlId);
    }

    @PostMapping("api/v1/add/url")
    @ResponseBody
    public UrlDto addUrl(@RequestBody(required = true) final UrlDto urlDto) {
        return urlService.addUrl(urlDto);
    }

    @GetMapping("/{shortUrl}")
    @ResponseBody
    public RedirectView getLongUrl(@PathVariable final String shortUrl)
    {
        String longUrl =  urlService.getLongUrl(shortUrl);
        return new RedirectView(longUrl);
    }
}
