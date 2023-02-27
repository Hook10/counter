package com.counter.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.counter.payload.CodeDto;
import com.counter.service.CodeService;
import com.counter.service.CodeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = CodeController.class)
@ActiveProfiles("test")
class CodeControllerTest {

  @MockBean
  private CodeServiceImpl codeService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetNextCode() throws Exception {
    // Prepare test data
    CodeDto codeDto = CodeDto.builder().value("a0a0").build();

    CodeDto responseDto = CodeDto.builder().value("a0a1").build();

    // Setup mock behavior
    when(codeService.generateCodeWithStartValue(any(CodeDto.class))).thenReturn(responseDto);

    // Perform the request and verify the results
    MvcResult result = mockMvc.perform(post("/gen-code")
            .content(objectMapper.writeValueAsString(codeDto))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andReturn();

    String responseString = result.getResponse().getContentAsString();
    CodeDto response = objectMapper.readValue(responseString, CodeDto.class);
    Assertions.assertNotNull(response);
    Assertions.assertEquals(responseDto.getValue(), response.getValue());
    verify(codeService, times(1)).generateCodeWithStartValue(any(CodeDto.class));
  }

  @Test
  public void testGetNextCode_InvalidInput() throws Exception {
    // Prepare test data
    CodeDto codeDto = CodeDto.builder().value("ae3").build();
    String requestBody = new ObjectMapper().writeValueAsString(codeDto);

    // Act and Assert
    mockMvc.perform(post("/gen-code")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException));

    // Verify
    verifyNoInteractions(codeService);
  }
}
