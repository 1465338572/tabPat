package com.example.tabpat.service;

import com.example.tabpat.domain.UserDo;
import com.example.tabpat.domain.UserThreadDo;
import com.example.tabpat.util.ClientUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


@Service
public class MessageService extends BaseService {
    String apiKey = "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";

    private ClientUtil clientUtil;

    @Autowired
    public void setClientUtil(ClientUtil clientUtil) {
        this.clientUtil = clientUtil;
    }

    @Transactional
    public Result getMessage(String message) throws ServiceException {
        try {
//            HttpClient client = HttpClient.newHttpClient();
//            JSONObject data = new JSONObject();
//            JSONObject roleMessage = new JSONObject();
//            roleMessage.put("role", "user");
//            roleMessage.put("content", "老婆！！！");
//
//            JSONArray arrayMessage = new JSONArray();
//            arrayMessage.add(roleMessage);
//            data.put("model", "gpt-4");
//            data.put("messages", arrayMessage);
//            data.put("temperature", 0.7);
//
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
//                    .header("Content-Type", "application/json")
//                    .header("Authorization", "Bearer " + apiKey)
//                    .POST(HttpRequest.BodyPublishers.ofString(data.toString()))
//                    .build();
//            Void response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
//                    .thenApply(HttpResponse::body)
//                    .thenAccept(System.out::println)
//                    .join();
//            System.out.println(response);

            String username = getCurrentUsername();

            UserDo userDo = userDao.getUserByName(username);

            UserThreadDo userThreadDo = userThreadDao.getThreadByUserId(userDo.getUserId());
            String threadId = userThreadDo.getThread();
            clientUtil.addMessage(threadId, message);
            String runResponse = clientUtil.runTheThread(threadId);
            String runId = null;
            if (runResponse != null) {

                String status = null;
                String step = null;

                // 处理响应
                ObjectMapper mapper = new ObjectMapper();
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

                while (!"completed".equals(status)) {
                    JsonNode rootNode = mapper.readTree(runResponse);
                    runId = rootNode.path("id").asText();
                    String statusResponse = clientUtil.getRunStatus(threadId, runId);
                    JsonNode statusNode = mapper.readTree(statusResponse);
                    status = statusNode.path("status").asText();

//                    String stepResponse = clientUtil.getRunStatus(threadId, runId);
//                    JsonNode stepNode = mapper.readTree(stepResponse);
//                    System.out.println(stepNode);
//                    step = statusNode.path("status").asText();


                    Runnable task = () -> System.out.println("执行任务...");

                    // 每5秒执行一次任务
                    ScheduledFuture<?> future = executor.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);

                    if (!"completed".equals(status)) {
                        future.cancel(true);
                    }
                }
                executor.shutdown();

                String response = clientUtil.getMessage(threadId);

                JsonNode jsonMessage = mapper.readTree(response);

                return Result.create(200, "消息回复成功", jsonMessage);
            } else {
                return Result.create(500, "消息回复失败");
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

}
