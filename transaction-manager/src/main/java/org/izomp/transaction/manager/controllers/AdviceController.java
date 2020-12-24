package org.izomp.transaction.manager.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class AdviceController {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleExceptions(Exception ex) {
        log.warn("Advice Controller: ", ex);
        return ResponseEntity.status(400)
                .body(ex.getMessage());
    }
}
