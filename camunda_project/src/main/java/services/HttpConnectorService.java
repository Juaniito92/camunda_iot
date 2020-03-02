package services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpConnectorService {
    private final static Logger logger = Logger.getLogger(HttpConnectorService.class.getName());
    private final static String HEADER_JSON_INPUT_PARAM = "x-json-input-str";

    public HttpResponse sendJsonPost(String apiUrl, String pathUrl, String xauth, String json, List<File> files) throws IOException {
        if (!CollectionUtils.isEmpty(files)) {
            return sendJsonPostMultipart(apiUrl, pathUrl, xauth, json, files);
        } else {
            return sendApplicationJsonPost(apiUrl, pathUrl, xauth, json);
        }
    }

    public HttpResponse sendGet(String apiUrl, String pathUrl, String xauth) throws IOException {
        String urlWithParams = apiUrl + pathUrl;
        HttpGet httpCall = new HttpGet(urlWithParams);

        httpCall.setHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
        httpCall.setHeader("x-auth", "" + xauth);

        HttpResponse response = launchRequest(httpCall);

        return response;
    }

    private HttpResponse sendApplicationJsonPost(String apiUrl, String pathUrl, String xauth, String json) throws IOException {
        String urlWithParams = apiUrl + pathUrl;
        HttpPost httpPost = new HttpPost(urlWithParams);

        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
        httpPost.setHeader("Content-type", ContentType.APPLICATION_JSON.getMimeType());
        httpPost.setHeader("x-auth", "" + xauth);

        HttpResponse response = launchRequest(httpPost);

        return response;
    }


    private HttpResponse sendJsonPostMultipart(String apiUrl, String pathUrl, String xauth, String json, List<File> files) throws IOException {
        String urlWithParams = apiUrl + pathUrl;
        HttpPost httpPost = new HttpPost(urlWithParams);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        files.forEach(f -> builder.addBinaryBody("file", f, ContentType.DEFAULT_BINARY, f.getName()));

        httpPost.setEntity(builder.build());
        httpPost.setHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
        httpPost.setHeader("x-auth", "" + xauth);
        httpPost.setHeader(HEADER_JSON_INPUT_PARAM, json);

        HttpResponse response = launchRequest(httpPost);

        return response;
    }

    private HttpResponse launchRequest(HttpRequestBase httpRequest) throws IOException, ClientProtocolException {
        HttpClient client = HttpClientBuilder.create().build();
        StopWatch watch = new StopWatch();
        logger.info("Sending request to URL : " + httpRequest.getURI());
        watch.start();
        HttpResponse response = client.execute(httpRequest);
        watch.stop();
        logger.info("Response Code : " + response.getStatusLine().getStatusCode() + " // Elapse time: " + watch.getTime() + " ms");
        return response;
    }

    public String extractResponseText(HttpResponse response) throws UnsupportedOperationException, IOException {
        return extractResponseText(response, false);

    }

    public String extractResponseText(HttpResponse response, boolean keepNewlines) throws UnsupportedOperationException, IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
            if (keepNewlines) {
                result.append("\n");
            }
        }

        logger.info(result.toString());

        return result.toString();

    }

}
