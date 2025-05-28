package com.aioveu.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
public class LoggingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        tranceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        // Buffer the response body
        response = new BufferingClientHttpResponseWrapper(response);

        traceResponse(response);
        return response;
    }

    private void tranceRequest(HttpRequest request, byte[] body) throws UnsupportedEncodingException {
        log.debug("=========================== request begin ===========================");
        log.debug("uri : {}", request.getURI());
        log.debug("method : {}", request.getMethod());
        log.debug("headers : {}", request.getHeaders());
        log.debug("request body : {}", new String(body, StandardCharsets.UTF_8));
        log.debug("============================ request end ============================");
    }

    private void traceResponse(ClientHttpResponse httpResponse) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getBody(), StandardCharsets.UTF_8));
        String line = bufferedReader.readLine();
        while (line != null) {
            inputStringBuilder.append(line);
            inputStringBuilder.append('\n');
            line = bufferedReader.readLine();
        }
        log.debug("============================ response begin ============================");
        log.debug("Status code  : {}", httpResponse.getStatusCode());
        log.debug("Status text  : {}", httpResponse.getStatusText());
        log.debug("Headers      : {}", httpResponse.getHeaders());
        log.debug("Response body: {}", inputStringBuilder.toString());
        log.debug("============================= response end =============================");
    }

    private static class BufferingClientHttpResponseWrapper implements ClientHttpResponse {
        private final ClientHttpResponse response;
        private byte[] body;

        public BufferingClientHttpResponseWrapper(ClientHttpResponse response) {
            this.response = response;
        }

        @Override
        public InputStream getBody() throws IOException {
            if (body == null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream is = response.getBody();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) > -1) {
                    baos.write(buffer, 0, len);
                }
                baos.flush();
                body = baos.toByteArray();
            }
            return new ByteArrayInputStream(body);
        }

        @Override
        public HttpHeaders getHeaders() {
            return response.getHeaders();
        }

        @Override
        public HttpStatus getStatusCode() throws IOException {
            return response.getStatusCode();
        }

        @Override
        public int getRawStatusCode() throws IOException {
            return response.getRawStatusCode();
        }

        @Override
        public String getStatusText() throws IOException {
            return response.getStatusText();
        }

        @Override
        public void close() {
            response.close();
        }
    }
}
