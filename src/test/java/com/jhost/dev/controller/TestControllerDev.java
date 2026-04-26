package com.jhost.dev.controller;

import com.jhost.dev.service.DevService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TestControllerDev {

    DevController devController;

    @Mock
    DevService devService;

    @Mock
    Locale locale;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        devController = new DevController(devService);
    }

    @Test
    public void testMockMVC() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(devController).build();

        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    public void test1() {
        String ptMSG = "PT test message";

        when(devService.getTestMessage(locale)).thenReturn(ptMSG);

        assertEquals(devController.test(locale), ptMSG);
    }
}