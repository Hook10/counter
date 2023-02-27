package com.counter.service;

import com.counter.entity.Code;
import com.counter.payload.CodeDto;
import com.counter.repository.CodeRepository;
import com.counter.utils.CodeGenerator;
import jakarta.transaction.Transactional;
import java.util.Comparator;
import org.springframework.stereotype.Service;

@Service
public class CodeServiceImpl implements CodeService {

  public static final String STARTING_CODE = "a0a0";

  private final CodeRepository codeRepository;
  private final CodeGenerator codeGenerator;

  public CodeServiceImpl(CodeRepository codeRepository, CodeGenerator codeGenerator) {
    this.codeRepository = codeRepository;
    this.codeGenerator = codeGenerator;
  }

  @Override
  public CodeDto generateCodeWithStartValue(CodeDto codeDto) {

    Code code = mapToEntity(codeDto);

    String newValue = codeGenerator.generateCode(code.getValue());
    code.setValue(newValue);
    Code newCode = codeRepository.save(code);

    return mapToDto(newCode);
  }

  @Override
  public CodeDto generateNextCode() {
    Code lustCode = codeRepository.findAll().stream().max(Comparator.comparing(Code::getId))
        .orElse(null);

    if (lustCode == null) {
      Code firstCode = codeRepository.save(Code.builder()
          .value(STARTING_CODE)
          .build());
      return mapToDto(firstCode);
    }
    String newCode = codeGenerator.generateCode(lustCode.getValue());
    Code savedCode = codeRepository.save(Code.builder()
        .value(newCode).build());
    return mapToDto(savedCode);
  }

  private Code mapToEntity(CodeDto codeDto) {
    return Code.builder()
        .id(codeDto.getId())
        .value(codeDto.getValue())
        .build();
  }

  private CodeDto mapToDto(Code code) {
    return CodeDto.builder()
        .id(code.getId())
        .value(code.getValue())
        .build();
  }
}