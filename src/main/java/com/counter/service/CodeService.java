package com.counter.service;

import com.counter.payload.CodeDto;

public interface CodeService {

  CodeDto generateCodeWithStartValue(CodeDto codeDto);

  CodeDto generateNextCode();

}
