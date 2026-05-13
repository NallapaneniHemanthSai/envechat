package com.envechat.backend.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiRoomsAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRoomsWithoutTokenReturns401() throws Exception {
        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.path").value("/api/rooms"));
    }

    @Test
    void authPingWithoutTokenReturns200() throws Exception {
        mockMvc.perform(get("/api/auth/ping"))
                .andExpect(status().isOk());
    }
}
