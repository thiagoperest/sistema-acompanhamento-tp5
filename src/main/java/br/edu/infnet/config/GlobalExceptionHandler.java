package br.edu.infnet.config;

import br.edu.infnet.model.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Manipulador global de exceções para a API
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata erros de validação de dados (Bean Validation)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ResponseDto<Map<String, String>> response = ResponseDto.erro(
                "Dados inválidos. Verifique os campos informados.",
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Trata exceções de tempo de execução (regras de negócio)
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDto<Void>> handleRuntimeException(RuntimeException ex) {

        ResponseDto<Void> response = ResponseDto.erro(ex.getMessage());

        HttpStatus status = HttpStatus.BAD_REQUEST;

        String message = ex.getMessage().toLowerCase();
        if (message.contains("não encontrado") || message.contains("not found")) {
            status = HttpStatus.NOT_FOUND;
        } else if (message.contains("credenciais inválidas") || message.contains("desativada")) {
            status = HttpStatus.UNAUTHORIZED;
        } else if (message.contains("já cadastrado") || message.contains("já existe")) {
            status = HttpStatus.CONFLICT;
        }

        return ResponseEntity.status(status).body(response);
    }

    /**
     * Trata exceções de argumentos ilegais
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDto<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {

        ResponseDto<Void> response = ResponseDto.erro(
                "Argumento inválido: " + ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Trata exceções genéricas não capturadas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<Void>> handleGenericException(Exception ex) {

        System.err.println("Erro não tratado: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
        ex.printStackTrace();

        ResponseDto<Void> response = ResponseDto.erro(
                "Erro interno do servidor. Tente novamente mais tarde."
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Trata exceções de estado ilegal
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ResponseDto<Void>> handleIllegalStateException(IllegalStateException ex) {

        ResponseDto<Void> response = ResponseDto.erro(
                "Estado inválido: " + ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}