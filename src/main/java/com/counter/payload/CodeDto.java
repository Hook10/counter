package com.counter.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodeDto {

  private Long id;
  @NotEmpty
  @Size(min = 2, message = "Code must be at least 4 symbols, example a0a0")
  private String value;

}
