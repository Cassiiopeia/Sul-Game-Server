package org.sejong.sulgamewiki.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.sejong.sulgamewiki.common.exception.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        ex.getStatus(),
        ex.getMessage(),
        ex.getSource()
    );
    return new ResponseEntity<>(errorResponse, ex.getStatus());
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        ex.getMessage(),
        "global"
    );
    log.error(ex.getMessage(), ex);
    ex.printStackTrace();
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
