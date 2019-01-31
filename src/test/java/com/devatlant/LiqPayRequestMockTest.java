package com.devatlant;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class LiqPayRequestMockTest {


    @Mock
    private LiqPayRequest liqPayRequest;


    private LiqPay liqPay;

    @Before
    public void setUp() {
        liqPayRequest = Mockito.mock(LiqPayRequest.class);
        liqPay = new LiqPay("public key", "private key");
        MockitoAnnotations.initMocks(liqPay);
    }


    @Test
    public void testApi() throws Exception {
        Map<String, String> dataTest = new HashMap<>();

        dataTest.put("data", "eyJwdWJsaWNfa2V5IjoicHVibGljIGtleSIsInZlcnNpb24iOiIzIn0=");
        dataTest.put("signature", "P2Ua6fBuyW9duKTPK7+en2AOPmg=");

        when(liqPayRequest.post(LiqPay.LIQPAY_API_URL + "test", dataTest)).thenReturn("{\"myvar\":\"test\"}");
        ReflectionTestUtils.setField(liqPay, "liqPayRequest", liqPayRequest);

        liqPay.api("test", new HashMap<String, String>());
        verify(liqPayRequest).post(LiqPay.LIQPAY_API_URL + "test", dataTest);

    }

}
