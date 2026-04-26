package com.jhost.dev.service;

import center.jhub.data.dto.in.dev.DevRestInDTO;
import com.jhost.dev.config.Constants;
import com.jhost.dev.service.meta.MessageService;
import java.util.Objects;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class DevService {

    private final MessageService messageService;

    public DevService(MessageService messageService) {
        this.messageService = messageService;
    }

    public String getTestMessage(Locale locale){
        return messageService.getMessage(Constants.MessagePaths.TEST_MESSAGE, locale);
    }

    public Object getRest(DevRestInDTO dto) {
        return null;
    }
}
