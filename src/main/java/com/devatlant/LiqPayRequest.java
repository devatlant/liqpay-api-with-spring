package com.devatlant;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

public class LiqPayRequest {

    private final RestTemplate restTemplate;

    public LiqPayRequest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String post(String url, Map<String, Object> list){
        final MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        for (Map.Entry<String, Object> entry : list.entrySet())
            map.add(entry.getKey(),entry.getValue());

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(
                map, headers);

        final ResponseEntity<String> responseEntity = restTemplate.postForEntity(url,request,String.class);
        if (!responseEntity.getStatusCode().is2xxSuccessful()){
            throw new RuntimeException(String.format("post to %s failed. Http status code : %s . Message: %s",url,
                    responseEntity.getStatusCode(), responseEntity.getBody()));
        }
        return responseEntity.getBody();
    }
}
