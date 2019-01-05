package com.devatlant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class LiqPayRequest {

    private final RestTemplate restTemplate;

    @Autowired
    public LiqPayRequest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String post(String url, Map<String, String> list){
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String> entry : list.entrySet())
            map.add(entry.getKey(),entry.getValue());

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(
                map, headers);

        final ResponseEntity<String> responseEntity = restTemplate.postForEntity(url,request,String.class);
        if (!responseEntity.getStatusCode().is2xxSuccessful()){
            throw new RuntimeException(String.format("post to %s failed. Http status code : %s . Message: %s",url,
                    responseEntity.getStatusCode(), responseEntity.getBody()));
        }
        return responseEntity.getBody();
    }
}
