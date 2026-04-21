package com.jhost.template.Template.service;

import com.jhost.template.Template.config.Constants;
import com.jhost.template.Template.service.meta.MessageService;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class TestService {

    private final MessageService messageService;

    public TestService(MessageService messageService) {
        this.messageService = messageService;
    }

    public String getTestMessage(Locale locale){
        return messageService.getMessage(Constants.MessagePaths.TEST_MESSAGE, locale);
    }
}
