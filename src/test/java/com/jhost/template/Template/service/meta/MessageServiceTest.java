package com.jhost.template.Template.service.meta;

import com.jhost.template.Template.config.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class MessageServiceTest {

    MessageService messageService;

    @Mock
    MessageSource messageSource;

    @Mock
    Constants.MessagePaths messagePath;

    @Mock
    Locale locale;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        messageService = new MessageService(messageSource);
    }

    @Test
    void getMessage() {
        String msg = "TEST";

        when(messageSource.getMessage(messagePath.getMessage(), null, locale)).thenReturn(msg);

        assertEquals(messageService.getMessage(messagePath, locale), msg);
    }
}