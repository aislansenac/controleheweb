package com.aislan.controle_horas_extras.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class ValidationHandler extends ResponseEntityExceptionHandler  {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            org.springframework.web.bind.MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, String> error = new HashMap<>();

        String msg = ex.getMostSpecificCause().getMessage();
        if (msg != null && msg.contains("Duplicate entry")) {
            error.put("erro", "Valor duplicado. Verifique se o registro já existe.");
        } else {
            error.put("erro", "Violação de integridade de dados." + msg);
        }
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> error = new HashMap<>();

        Throwable mostSpecificCause = ex.getMostSpecificCause();
        if (mostSpecificCause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException formatException) {
            String fieldName = formatException.getPath().get(0).getFieldName();
            error.put(fieldName, "Formato inválido para o campo '" + fieldName + "'.");
        } else {
            error.put("erro", "Erro ao processar o corpo da requisição. Verifique os dados enviados.");
        }

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
