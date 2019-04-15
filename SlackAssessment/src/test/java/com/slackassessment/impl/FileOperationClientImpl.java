package com.slackassessment.impl;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.slackassessment.constants.Constants;

import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FileOperationClientImpl {
	
	private static OkHttpClient client = new OkHttpClient();
   
    private static JSONObject callSlackAPI(Request request) throws IOException, JSONException {
        JSONObject obj = null;
        Response response = null;
        String responseString = null;
        int retry = 5;
        while (retry-- > 0) {
            try {
                response = client.newCall(request).execute();
            } catch (java.net.SocketTimeoutException e) {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } finally {
                if (response != null) {
                    responseString = response.body().string();
                    response.close();
                    break;
                }
            }
        }
        obj = new JSONObject(responseString);
        return obj;
    }

    private static JSONObject upload(MultipartBody.Builder multipartBuilder, String url) throws IOException, JSONException {
        RequestBody requestBody = multipartBuilder.build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        return callSlackAPI(request);
    }

    public static JSONObject delete(String body, String token) throws IOException, JSONException {
        RequestBody requestBody = RequestBody.create(Constants.JSON, body);
        Request request = new Request.Builder().header("Authorization", "Bearer " + token).url(Constants.deleteUrl).post(requestBody)
                .build();
        return callSlackAPI(request);
    }

    private static JSONObject list(HttpUrl.Builder urlBuilder) throws IOException, JSONException {
        Request request = new Request.Builder().url(urlBuilder.build().toString()).build();
        return callSlackAPI(request);
    }

    public static JSONObject fileUpload(MultipartBody.Builder multipartBuilder) throws IOException, JSONException {
        return upload(multipartBuilder, Constants.uploadUrl);
    }

    public static JSONObject listFiles(String token) throws IOException, JSONException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.listUrl).newBuilder();
        urlBuilder.addQueryParameter("token", token);
        return list(urlBuilder);
    }

    public static JSONObject listFiles(Map<String, String> params) throws IOException, JSONException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.listUrl).newBuilder();
        Set<Entry<String, String>> entrySet = params.entrySet();
        for (Entry<String, String> entry : entrySet) {
            urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
        }
        return list(urlBuilder);
    }

    public static void main(String args[]) throws Exception {

    }
}
