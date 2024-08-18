package com.fofa;

import com.fofa.entity.ResultEntity;
import com.fofa.service.FofaService;
import com.fofa.util.AESUtil;
import com.fofa.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootApplication
@EnableWebSecurity
@RestController
public class FofaCloudServerApplication {

    private final FofaService fofaService;

    @Autowired
    public FofaCloudServerApplication(FofaService fofaService) {
        this.fofaService = fofaService;
    }

    @GetMapping("/api")
    public ResponseEntity<?> api(@RequestParam String ak,
                                 @RequestParam(required = false) String queryStr,
                                 @RequestParam(required = false) String page,
                                 @RequestParam(required = false) String fields,
                                 @RequestParam(required = false) String size,
                                 @RequestParam(required = false) String hostMerge,
                                 @RequestParam(required = false) String ip) throws UnsupportedEncodingException {

        Map<String, Object> response = new HashMap<>();
        new AESUtil().loadProperties(); // 首先加载属性
        String result = AESUtil.decrypt(URLDecoder.decode( ak, "UTF-8" ));
        if(result == null){
            return ResponseEntity.badRequest().body("密钥错误");
        }
        if(fields != null && hostMerge == null){
            fields = URLDecoder.decode( fields, "UTF-8" );
            if(fields.split(",").length < 2){
                return ResponseEntity.badRequest().body("fileds参数数量需要大于1");
            }
        }
        try {
//            if (hostMerge != null && hostMerge.equalsIgnoreCase("true")) {
//                response.putAll(fofaService.getHostDetails(ip));
//            } else{
//                return ResponseEntity.ok(fofaService.getSearchResults(URLDecoder.decode( queryStr, "UTF-8" ), page, size, fields));
//            }
//
//            return ResponseEntity.ok(response);
            return ResponseEntity.ok(fofaService.getSearchResults(URLDecoder.decode( queryStr, "UTF-8" ), page, size, fields));
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping(value = "/export", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void export(
            @RequestBody Map<String, Object> requestBody,
            HttpServletResponse response
    ) throws IOException {
        new AESUtil().loadProperties(); // 首先加载属性
        String ak = (String) requestBody.get("ak");
        String result = AESUtil.decrypt(URLDecoder.decode(ak, "UTF-8"));
        if (result == null) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        List<ResultEntity> resultEntityList = (List<ResultEntity>) requestBody.get("resultEntityList");
        if (resultEntityList == null) {
            throw new IllegalArgumentException("结果实体列表不能为空");
        }

        // 直接调用ExcelUtil.export方法
        ExcelUtil.export(
                response,
                "FOFA导出数据" + now.format(formatter),
                "数据表",
                resultEntityList,
                ResultEntity.class
        );
    }

    public static void main(String[] args) throws Exception {
        /**
         * 生成指定数量的ak
         */
        new AESUtil().loadProperties(); // 首先加载属性
        FileWriter fileWriter = new FileWriter("ak.txt");
        for (int i = 0; i < 30; i++) {
            log.info(AESUtil.encrypt(String.valueOf(i)));
            fileWriter.write(AESUtil.encrypt(String.valueOf(i)) + System.lineSeparator());
        }
        fileWriter.close();
        SpringApplication.run(FofaCloudServerApplication.class, args);
    }
}