package com.fofa.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fofa.entity.QueryResultEntity;
import com.fofa.entity.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

import okhttp3.OkHttpClient;

@Slf4j
@Service
public class FofaService {

    @Value("${fofa.email}")
    private String fofaEmail;

    @Value("${fofa.key}")
    private String fofaKey;

    private final RestTemplate restTemplate;

    public FofaService() {
        OkHttpClient client = new OkHttpClient();
        OkHttp3ClientHttpRequestFactory factory = new OkHttp3ClientHttpRequestFactory(client);
        this.restTemplate = new RestTemplate(factory);
    }

//    @PostConstruct
//    private void loadProperties() {
//        Resource resource = new ClassPathResource("settings.ini");
//        try {
//            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
//            this.fofaEmail = properties.getProperty("fofa.email");
//            this.fofaKey = properties.getProperty("fofa.key");
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to load properties from settings.ini", e);
//        }
//    }
    @PostConstruct
    private void loadProperties() {
        Resource resource = new FileSystemResource("settings.ini"); // 从当前目录读取
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            this.fofaEmail = properties.getProperty("fofa.email");
            this.fofaKey = properties.getProperty("fofa.key");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties from settings.ini", e);
        }
    }

    public Map<String, Object> getHostDetails(String ip) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", fofaEmail);
        params.add("key", fofaKey);
        params.add("details", "true");
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://fofa.info/api/v1/host/" + ip);
        builder.queryParams(params);
        ResponseEntity<Map> response = restTemplate.getForEntity(builder.toUriString(), Map.class);
        return response.getBody();
    }

    public QueryResultEntity getSearchResults(String queryStr, String page, String size, String fields) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("qbase64", base64EncodeQuery(queryStr));
        params.add("email", fofaEmail);
        params.add("key", fofaKey);
        params.add("fields", fields);
        params.add("size", size);
        params.add("page", page);
        log.info(params.toString());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://fofa.info/api/v1/search/all");
        builder.queryParams(params);

        ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        QueryResultEntity queryResultEntity = null;
        try {
            queryResultEntity = objectMapper.readValue(response.getBody(), QueryResultEntity.class);
            log.info(queryResultEntity.toString());
            // 处理原始结果并转换为ResultEntity对象
            List<ResultEntity> processedResults = processResults(queryResultEntity.getResults(), fields.split(","));
            queryResultEntity.setFinalResults(processedResults); // 设置处理过的结果列表
            queryResultEntity.setResults(null); // 清除原始结果以避免混淆
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
        }
        return queryResultEntity;
    }

    private List<ResultEntity> processResults(List<List<String>> originResults, String[] fields) {
        List<ResultEntity> resultEntityList = new ArrayList<>();
        for (List<String> row : originResults) {
            ResultEntity resultEntity = new ResultEntity(); // 创建一次ResultEntity对象
            for (int i = 0; i < fields.length && i < row.size(); i++) {
                String field = fields[i];
                String value = row.get(i);
                switch (field) {
                    case "ip":
                        resultEntity.setIp(value);
                        break;
                    case "port":
                        resultEntity.setPort(value);
                        break;
                    case "protocol":
                        resultEntity.setProtocol(value);
                        break;
                    case "domain":
                        resultEntity.setDomain(value);
                        break;
                    case "link":
                        resultEntity.setLink(value);
                        break;
                    case "title":
                        resultEntity.setTitle(decodeHtmlEntities(value));
                        break;
                    case "city":
                        resultEntity.setCity(value);
                        break;
                }
            }
            resultEntityList.add(resultEntity);
        }
        log.info(resultEntityList.toString());
        return resultEntityList;
    }

    public static String decodeHtmlEntities(String input) {
        return StringEscapeUtils.unescapeHtml4(input);
    }

    private String base64EncodeQuery(String query) {
        return Base64.getEncoder().encodeToString(query.getBytes());
    }
}
