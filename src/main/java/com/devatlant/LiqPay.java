package com.devatlant;

import com.devatlant.model.LiqPayContract;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.client.RestTemplate;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import static com.devatlant.LiqPayUtil.base64_encode;
import static com.devatlant.LiqPayUtil.sha1;


public class LiqPay implements LiqPayApi {
    private final JSONParser parser = new JSONParser();
    private final String publicKey;
    private final String privateKey;
    private Proxy proxy;
    private String proxyLogin;
    private String proxyPassword;
    private boolean cnbSandbox;
    private boolean renderPayButton = true;

    private LiqPayRequest liqPayRequest;

    public LiqPay(String publicKey, String privateKey, RestTemplate restTemplate) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        liqPayRequest = new LiqPayRequest(restTemplate);
        checkRequired();
    }

    public LiqPay(String publicKey, String privateKey, Proxy proxy, String proxyLogin, String proxyPassword) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.proxy = proxy;
        this.proxyLogin = proxyLogin;
        this.proxyPassword = proxyPassword;
        checkRequired();
    }

    private void checkRequired() {
        if (this.publicKey == null || this.publicKey.isEmpty()) {
            throw new IllegalArgumentException("publicKey is empty");
        }
        if (this.privateKey == null || this.privateKey.isEmpty()) {
            throw new IllegalArgumentException("privateKey is empty");
        }
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public String getProxyLogin() {
        return proxyLogin;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyLogin(String proxyLogin) {
        this.proxyLogin = proxyLogin;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public boolean isCnbSandbox() {
        return cnbSandbox;
    }

    public void setCnbSandbox(boolean cnbSandbox) {
        this.cnbSandbox = cnbSandbox;
    }

    public boolean isRenderPayButton() {
        return renderPayButton;
    }

    public void setRenderPayButton(boolean renderPayButton) {
        this.renderPayButton = renderPayButton;
    }

    @Override
    public Map<String, Object> api(String path, Map<String, Object> params) throws Exception {
        Map<String, Object> data = generateData(params);
        String resp = liqPayRequest.post(LIQPAY_API_URL + path, data);
        JSONObject jsonObj = (JSONObject) parser.parse(resp);
        return LiqPayUtil.parseJson(jsonObj);
    }

    protected Map<String, Object> generateData(Map<String, Object> params) {
        HashMap<String, Object> apiData = new HashMap<>();
        String data = base64_encode(JSONObject.toJSONString(withBasicApiParams(params)));
        apiData.put("data", data);
        apiData.put("signature", createSignature(data));
        return apiData;
    }

    protected TreeMap<String, Object> withBasicApiParams(Map<String, Object> params) {
        TreeMap<String, Object> tm = new TreeMap<>(params);
        tm.put("public_key", publicKey);
        tm.put("version", API_VERSION);
        return tm;
    }

    protected TreeMap<String, Object> withSandboxParam(TreeMap<String, Object> params) {
        if (params.get("sandbox") == null && isCnbSandbox()) {
            TreeMap<String, Object> tm = new TreeMap<>(params);
            tm.put("sandbox", "1");
            return tm;
        }
        return params;
    }

    @Override
    public String cnb_form(Map<String, Object> params) {
        checkCnbParams(params);
        String data = base64_encode(JSONObject.toJSONString(withSandboxParam(withBasicApiParams(params))));
        String signature = createSignature(data);
        String language = params.get("language") != null ? (String) params.get("language") : DEFAULT_LANG;
        return renderHtmlForm(data, language, signature);
    }

  @Override
  public LiqPayContract generateApiContract(Map<String, Object> params) {
    checkCnbParams(params);
    final String data = base64_encode(JSONObject.toJSONString(withSandboxParam(withBasicApiParams(params))));
    final String signature = createSignature(data);
    return new LiqPayContract(data, signature);
  }

    private String renderHtmlForm(String data, String language, String signature) {
        String form = "";
        form += "<form method=\"post\" action=\"" + LIQPAY_API_CHECKOUT_URL + "\" accept-charset=\"utf-8\">\n";
        form += "<input type=\"hidden\" name=\"data\" value=\"" + data + "\" />\n";
        form += "<input type=\"hidden\" name=\"signature\" value=\"" + signature + "\" />\n";
        if (this.renderPayButton) {
            form += "<input type=\"image\" src=\"//static.liqpay.ua/buttons/p1" + language + ".radius.png\" name=\"btn_text\" />\n";
        }
        form += "</form>\n";
        return form;
    }

    protected void checkCnbParams(Map<String, Object> params) {
        if (params.get("amount") == null)
            throw new NullPointerException("amount can't be null");
        if (params.get("currency") == null)
            throw new NullPointerException("currency can't be null");
        if (params.get("description") == null)
            throw new NullPointerException("description can't be null");
    }

    protected String str_to_sign(String str) {
        return base64_encode(sha1(str));
    }

    @Override
    public String createSignature(String base64EncodedData) {
        return str_to_sign(privateKey + base64EncodedData + privateKey);
    }
}
