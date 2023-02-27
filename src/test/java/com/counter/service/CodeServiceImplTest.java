package com.counter.service;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.counter.entity.Code;
import com.counter.payload.CodeDto;
import com.counter.repository.CodeRepository;
import com.counter.utils.CodeGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

class CodeServiceImplTest {

  private CodeServiceImpl codeService;

  @Mock
  private CodeRepository codeRepository;

  @Mock
  private CodeGenerator codeGenerator;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    codeService = new CodeServiceImpl(codeRepository, codeGenerator);
  }

  @Test
  void testGenerateCode() {
    // Prepare test data
    CodeDto codeDto = CodeDto.builder().value("a0a0").build();

    Code code = Code.builder().id(1L).value("a0a1").build();

    // Setup mock behavior
    when(codeGenerator.generateCode(codeDto.getValue())).thenReturn("a0a1");
    when(codeRepository.save(any(Code.class))).thenReturn(code);

    // Call the method being tested
    CodeDto responseCode = codeService.generateCodeWithStartValue(codeDto);

    // Verify the results
    Assertions.assertNotNull(responseCode);
    Assertions.assertEquals("a0a0", codeDto.getValue());
    Assertions.assertEquals("a0a1", responseCode.getValue());
    verify(codeGenerator, times(1)).generateCode(codeDto.getValue());
    verify(codeRepository, times(1)).save(any(Code.class));
  }

  @Test
  void testGenerateCode_DataIntegrityViolationException() {
    // Prepare test data
    CodeDto codeDto = CodeDto.builder().value("a0a0").build();

    // Setup mock behavior
    when(codeGenerator.generateCode(codeDto.getValue())).thenReturn("a0a1");
    when(codeRepository.save(any(Code.class))).thenThrow(DataIntegrityViolationException.class);

    // Call the method being tested and verify it throws an exception
    Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
      codeService.generateCodeWithStartValue(codeDto);
    });
  }
}
