package com.fggang.tools;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class HttpClientUtils {

    private static final int COMMON_TIMEOUT = 5000;
    private static final int MAX_TOTAL = 200;
    private static final int MAX_PER_ROUTE = 20;

    private static final PoolingHttpClientConnectionManager cm;
    private static final HttpRequestRetryHandler retryHandler;
    private static final ExecutorService executor;
    private static final HttpClient client;

    static {
        // 初始化连接池
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(MAX_TOTAL);
        cm.setDefaultMaxPerRoute(MAX_PER_ROUTE);

        // 初始化请求重试处理器
        retryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= 3) {
                    // 如果已经重试了3次，放弃
                    return false;
                } else if (exception instanceof InterruptedIOException) {
                    // 超时
                    return true;
                } else if (exception instanceof UnknownHostException) {
                    // 目标服务器不可达
                    return false;
                } else if (exception instanceof ConnectTimeoutException) {
                    // 连接被拒绝
                    return false;
                } else if (exception instanceof SSLException) {
                    // ssl握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求不是幂等的，就重试
                return !(request instanceof HttpEntityEnclosingRequest);
            }
        };

        // 初始化线程池
        executor = Executors.newFixedThreadPool(5);

        // 初始化HttpClient
        HttpClientBuilder builder = HttpClients.custom()
                .setConnectionManager(cm)
                .setRetryHandler(retryHandler);
        client = builder.build();
    }

    // 封装HttpGet请求
    public static String get(String url, Map<String, String> queryParams, Map<String, String> headers) throws IOException, URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url);
        if (queryParams != null) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setConfig(
                RequestConfig.custom()
                        .setConnectionRequestTimeout(COMMON_TIMEOUT)
                        .setSocketTimeout(COMMON_TIMEOUT)
                        .setConnectTimeout(COMMON_TIMEOUT)
                        .build());

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }

        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();

        return EntityUtils.toString(entity, Charset.forName("UTF-8"));
    }

    // 封装HttpPost请求（只支持表单数据）
    public static String post(String url, Map<String, String> formData, Map<String, String> headers) throws UnsupportedEncodingException, IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(
                RequestConfig.custom()
                        .setConnectionRequestTimeout(COMMON_TIMEOUT)
                        .setSocketTimeout(COMMON_TIMEOUT)
                        .setConnectTimeout(COMMON_TIMEOUT)
                        .setExpectContinueEnabled(false)
                        .build());

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }

        if (formData != null) {
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            for (Map.Entry<String, String> entry : formData.entrySet()) {
                entityBuilder.addPart(entry.getKey(), new StringBody(entry.getValue(), Charset.forName("UTF-8")));
            }
            HttpEntity entity = entityBuilder.build();
            httpPost.setEntity(entity);
        }

        HttpResponse response = client.execute(httpPost);
        HttpEntity entity = response.getEntity();

        return EntityUtils.toString(entity, Charset.forName("UTF-8"));
    }

    // 异步封装HttpGet请求
    public static void asyncGet(String url, Map<String, String> queryParams, Map<String, String> headers, Consumer<String> callback) {
        executor.submit(() -> {
            try {
                String result = get(url, queryParams, headers);
                callback.accept(result);
            } catch (IOException | URISyntaxException e) {
                callback.accept(null);
            }
        });
    }

    // 异步封装HttpPost请求（只支持表单数据）
    public static void asyncPost(String url, Map<String, String> formData, Map<String, String> headers, Consumer<String> callback) {
        executor.submit(() -> {
            try {
                String result = post(url, formData, headers);
                callback.accept(result);
            } catch (IOException e) {
                callback.accept(null);
            }
        });
    }
}
