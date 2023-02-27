package com.counter.controller;

import com.counter.payload.CodeDto;
import com.counter.service.CodeServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gen-code")
public class CodeController {

  private final CodeServiceImpl codeService;

  public CodeController(CodeServiceImpl codeService) {
    this.codeService = codeService;
  }

  @PostMapping
  public ResponseEntity<CodeDto> getNextCodeWithStartingValue(@Valid @RequestBody CodeDto codeDto) {
    return new ResponseEntity<>(codeService.generateCodeWithStartValue(codeDto),
        HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<CodeDto> getNextCode() {
    return ResponseEntity.ok(codeService.generateNextCode());
  }

}
