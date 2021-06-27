package com.germinait.assignment.urlshortener.service.impl;

import com.germinait.assignment.urlshortener.common.IntToCharConverter;
import com.germinait.assignment.urlshortener.common.UrlValidator;
import com.germinait.assignment.urlshortener.dao.UrlDao;
import com.germinait.assignment.urlshortener.dto.UrlDto;
import com.germinait.assignment.urlshortener.model.UrlModel;
import com.germinait.assignment.urlshortener.service.UrlService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UrlServiceImpl implements UrlService {

    @Value("${domain.url}")
    private String domainUrl;

    @Autowired
    private UrlDao urlDao;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<UrlDto> getUrl() {

        List<UrlDto> urlDtos = urlDao.findAll().stream().map(urlModel -> appendDomainUrl(convertToDto(urlModel))).collect(Collectors.toList());
        return urlDtos;
    }

    @Override
    public UrlDto getUrlById(Long urlId) {
        Optional<UrlModel> urlModel = urlDao.findById(urlId);
        if (urlModel.isPresent())
        {
            return appendDomainUrl(convertToDto(urlModel.get()));
        }
        return null;
    }

    @Override
    public UrlDto addUrl(UrlDto urlDto) {
        if(UrlValidator.validator.validateURL(urlDto.getLongUrl()))
        {
            String shortenUrl = alreadyExists(urlDto.getLongUrl());

            //generate and add url in database
            if(shortenUrl.length() == 0) {
                UrlModel urlModel = convertToEntity(urlDto);
                UrlModel savedUrlModel = urlDao.save(urlModel);
                shortenUrl = generateUrl(savedUrlModel.getId());
                savedUrlModel.setShortUrl(shortenUrl);
                UrlModel response = urlDao.save(savedUrlModel);
                return appendDomainUrl(convertToDto(response));
            }

            UrlModel urlModel = urlDao.findByLongUrl(urlDto.getLongUrl());

            return convertToDto(urlModel);
        }

        return null;
    }

    @Override
    public String getLongUrl(String shortUrl) {
        UrlDto urlDto = convertToDto(urlDao.findByShortUrl(shortUrl));
        return urlDto.getLongUrl();
    }

    private String generateUrl(Long id) {
        List<Integer> rems = getRemainders(id);
        StringBuilder shortenUrl = new StringBuilder();
        for (int rem: rems) {
            shortenUrl.append(IntToCharConverter.intToCharMap.get(rem));
        }
        return shortenUrl.toString();
    }

    private String  alreadyExists(String longUrl) {

        UrlModel urlModel = urlDao.findByLongUrl(longUrl);
        if(urlModel != null)
        {
            return urlModel.getShortUrl();
        }
        return "";
    }

    private UrlModel convertToEntity(UrlDto urlDto) throws ParseException {
        UrlModel urlModel = modelMapper.map(urlDto, UrlModel.class);
        return urlModel;
    }

    private UrlDto convertToDto(UrlModel urlModel) {
        UrlDto urlDto = modelMapper.map(urlModel, UrlDto.class);
        return urlDto;
    }

    private List<Integer> getRemainders(Long id) {
        List<Integer> rems = new LinkedList<>();
        while(id > 0) {
            int remainder = (int)(id % 62);
            ((LinkedList<Integer>) rems).addFirst(remainder);
            id /= 62;
        }
        return rems;
    }

    private UrlDto appendDomainUrl(UrlDto urlDto)
    {
        urlDto.setShortUrl(new StringBuilder().append(domainUrl).append(urlDto.getShortUrl()).toString());
        return urlDto;
    }
}
