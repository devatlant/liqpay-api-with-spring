package com.devatlant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LiqPayRequestTest {

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private LiqPayRequest liqPayRequest;

    @Test
    public void testGetProxyUser() {
        // assertEquals("dXNlcjpwYXNz", LiqPayRequest.getProxyUser("user", "pass"));
    }

    @Test
    public void testPost() {
        ResponseEntity responseEntity = new ResponseEntity("test", HttpStatus.OK);
        Map<String, String> testMap = new HashMap<>();
        testMap.put("1", "test");
        when(restTemplate.postForEntity(anyString(), anyObject(), any(Class.class))).thenReturn(responseEntity);
        liqPayRequest.post("test.url", testMap);
        verify(restTemplate).postForEntity(anyString(), anyObject(), any(Class.class));

    }

    @Test(expected = RuntimeException.class)
    public void shouldPostThrowRuntimeExceptionTest() {
        ResponseEntity responseEntity = new ResponseEntity("test", HttpStatus.CONFLICT);
        Map<String, String> testMap = new HashMap<>();
        testMap.put("2", "test2");
        when(restTemplate.postForEntity(anyString(), anyObject(), any(Class.class))).thenReturn(responseEntity);
        liqPayRequest.post("test.url", testMap);
        verify(restTemplate).postForEntity(anyString(), anyObject(), any(Class.class));

    }
}