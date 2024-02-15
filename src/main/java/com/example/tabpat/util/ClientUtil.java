package com.example.tabpat.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class ClientUtil {

    String apiKey = "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";

    public String doPost(String url, JSONObject data) {

        HttpClient client = HttpClient.newHttpClient();

        String response = "";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("OpenAI-Beta", "assistants=v1")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(data.toString()))
                .build();
        response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();
        return response;
    }

    public String doGet(String url) {

        HttpClient client = HttpClient.newHttpClient();

        String response = "";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("OpenAI-Beta", "assistants=v1")
                .header("Authorization", "Bearer " + apiKey)
                .GET()
                .build();
        response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();
        return response;
    }


    public String addMessage(String thread, String message) {
        JSONObject roleMessage = new JSONObject();
        roleMessage.put("role", "user");
        roleMessage.put("content", message);

        String url = "https://api.openai.com/v1/threads/" + thread + "/messages";
        return doPost(url, roleMessage);
    }

    public String runTheThread(String thread) {
        JSONObject roleMessage = new JSONObject();
        roleMessage.put("assistant_id", "asst_4APmG3nezz4wSE9sOWo1iZkJ");

        String url = "https://api.openai.com/v1/threads/" + thread + "/runs";
        return doPost(url, roleMessage);
    }

    //列出运行
    public String getRunStatus(String thread, String runId) {
        String url = "https://api.openai.com/v1/threads/" + thread + "/runs/" + runId;
        return doGet(url);
    }

    //列出运行步骤
    public String getRunStep(String thread, String runId) {
        String url = "https://api.openai.com/v1/threads/" + thread + "/runs/" + runId + "steps";
        return doGet(url);
    }

    public String getMessage(String thread) {
        String url = "https://api.openai.com/v1/threads/" + thread + "/messages";
        return doGet(url);
    }

}
