package mj.kakao.bookservice.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import mj.kakao.bookservice.model.entity.BookResultEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApiService {
    
    @Value("${api.url}")
    private String url;

    @Value("${api.key}")
    private String key;

    private int limitTotalCount = 1000;

    public List<BookResultEntity>  retrieveBookList(String query) throws IOException, URISyntaxException {
        HttpGet httpGet = new HttpGet("");
        int timeout = 10 * 60 * 1000;
        httpGet.setHeader("Authorization", "KakaoAK " + key);
        httpGet.setConfig(RequestConfig.custom()
                .setConnectTimeout(timeout)
                .setSocketTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .build()
        );
        int totalBookListCount = 0;
        int size = 50;
        int page = 1;

        List<BookResultEntity> bookResultList = new ArrayList();

        while(totalBookListCount < limitTotalCount) {
        URI uri = new URIBuilder(url)
            .addParameter("query", query)
                .addParameter("page", String.valueOf(page++))
                .addParameter("size", String.valueOf(50))
            .build();
        log.info("URI : {}", uri.toString());
        httpGet.setURI(uri);

        String json = null;

            try (CloseableHttpClient httpClient = HttpClients.createDefault()){
                try(CloseableHttpResponse response = httpClient.execute(httpGet)) {
                    HttpStatus status = HttpStatus.valueOf(response.getStatusLine().getStatusCode());
                    if(status == HttpStatus.OK) {
                        json = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                    } else {
                        log.warn("Http Code {} : {}", status.value(), status.getReasonPhrase());
                    }
                }
            }
            boolean isEnd = false;
            if(json != null) {
                JSONObject jsonObject = new JSONObject(json);
                JSONObject meta = jsonObject.getJSONObject("meta");
                isEnd = meta.getBoolean("is_end");

                JSONArray contentArray = jsonObject.getJSONArray("documents");
                for(int i =0; i < contentArray.length(); i++) {
                    JSONObject content = contentArray.getJSONObject(i);
                    bookResultList.add(BookResultEntity
                            .builder()
                            .title(content.getString("title"))
                            .contents(content.getString("contents"))
                            .build());
                    totalBookListCount += 1;
                    if(totalBookListCount == limitTotalCount) {
                        break;
                    }
                }
            }

            if(isEnd && totalBookListCount < limitTotalCount) {
                if(query.equals("자바")) {
                    break;
                }else {
                    query = "자바";
                    page = 1;
                }

            }
        }

        return  bookResultList;

    }


}
