package com.spring.memory.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.spring.memory.dto.ErrorResponseDTO;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

        // 401 - Unauthorized
        @ExceptionHandler(UsernameNotFoundException.class)
        public ResponseEntity<ErrorResponseDTO> handleAuth(UsernameNotFoundException ex, HttpServletRequest req) {
                ErrorResponseDTO err = ErrorResponseDTO.builder()
                                .timestamp(Instant.now())
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .title("Unauthorized")
                                .detail(ex.getMessage())
                                .instance(req.getRequestURI())
                                .build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err);
        }

        // 401 - Unauthorized (Bad Credentials - wrong password/username)
        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ErrorResponseDTO> handleBadCredentials(BadCredentialsException ex,
                        HttpServletRequest req) {
                ErrorResponseDTO err = ErrorResponseDTO.builder()
                                .timestamp(Instant.now())
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .title("Unauthorized")
                                .detail("Invalid email or password")
                                .instance(req.getRequestURI())
                                .build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err);
        }

        // 400 - Bad Request (Custom business logic errors)
        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<ErrorResponseDTO> handleBadRequest(BadRequestException ex, HttpServletRequest req) {
                ErrorResponseDTO err = ErrorResponseDTO.builder()
                                .timestamp(Instant.now())
                                .status(HttpStatus.BAD_REQUEST.value())
                                .title("Bad Request")
                                .detail(ex.getMessage())
                                .instance(req.getRequestURI())
                                .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        }

        // 401 - Unauthorized (Custom JWT token validation failures)
        @ExceptionHandler(InvalidTokenException.class)
        public ResponseEntity<ErrorResponseDTO> handleInvalidToken(InvalidTokenException ex, HttpServletRequest req) {
                ErrorResponseDTO err = ErrorResponseDTO.builder()
                                .timestamp(Instant.now())
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .title("Unauthorized")
                                .detail(ex.getMessage())
                                .instance(req.getRequestURI())
                                .build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err);
        }

        // 401 - Unauthorized (JWT library exceptions)
        @ExceptionHandler({ ExpiredJwtException.class, UnsupportedJwtException.class, MalformedJwtException.class,
                        SignatureException.class })
        public ResponseEntity<ErrorResponseDTO> handleJwtExceptions(Exception ex, HttpServletRequest req) {
                ErrorResponseDTO err = ErrorResponseDTO.builder()
                                .timestamp(Instant.now())
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .title("Unauthorized")
                                .detail("Invalid or expired authentication token")
                                .instance(req.getRequestURI())
                                .build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err);
        }

        @Override
        protected ResponseEntity<Object> handleMethodArgumentNotValid(
                        MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
                        WebRequest request) {

                List<ErrorResponseDTO.FieldValidationError> fieldErrors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> new ErrorResponseDTO.FieldValidationError(
                                                error.getField(),
                                                error.getDefaultMessage()))
                                .toList();

                ErrorResponseDTO err = ErrorResponseDTO.builder()
                                .timestamp(Instant.now())
                                .status(HttpStatus.BAD_REQUEST.value())
                                .title("Validation Failed")
                                .detail("One or more fields have validation errors")
                                .instance(((org.springframework.web.context.request.ServletWebRequest) request)
                                                .getRequest().getRequestURI())
                                .errors(fieldErrors)
                                .build();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        }

        // 500 - Internal Server Error (fallback)
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponseDTO> handleGeneral(Exception ex, HttpServletRequest req) {
                ErrorResponseDTO err = ErrorResponseDTO.builder()
                                .timestamp(Instant.now())
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .title("Internal Server Error")
                                .detail("An unexpected error occurred")
                                .instance(req.getRequestURI())
                                .build();

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
        }

}
