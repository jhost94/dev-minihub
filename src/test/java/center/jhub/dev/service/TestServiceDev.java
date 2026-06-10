package center.jhub.dev.service;

import center.jhub.dev.config.Constants;
import center.jhub.dev.service.meta.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TestServiceDev {

    DevService devService;

    @Mock
    MessageService messageService;

    @Mock
    Locale locale;

    @Mock
    Constants.MessagePaths messagePath;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        devService = new DevService(messageService);
    }

    @Test
    void getTestMessage() {
        String textValue = "TEST";

        when(messageService.getMessage(messagePath, locale)).thenReturn(textValue);

        assertEquals(messageService.getMessage(messagePath, locale), textValue);
    }
}