package center.jhub.dev.controller;

import center.jhub.dev.service.DevService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TestControllerDev {

    RestApiController restApiController;

    @Mock
    DevService devService;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    Locale locale;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        restApiController = new RestApiController(devService, objectMapper);
    }

    @Test
    public void testMockMVC() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(restApiController).build();

        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }
}