package com.example.tabpat.web;

import com.example.tabpat.form.MessageForm;
import com.example.tabpat.service.MessageService;
import com.example.tabpat.service.Result;
import com.google.protobuf.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    private MessageService messageService;

    @Autowired
    public void setMessageController(MessageService messageController) {
        this.messageService = messageController;
    }

    @PostMapping(value = "/secure/message")
    @ResponseBody
    public Result getMessage(@RequestBody MessageForm messageForm) {
        try {
            System.out.println(messageForm);
            String message = messageForm.getMessage();
            return messageService.getMessage(message);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }
}
