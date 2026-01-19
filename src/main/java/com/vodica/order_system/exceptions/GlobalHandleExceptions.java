package com.vodica.order_system.exceptions;

import com.stripe.exception.StripeException;
import com.vodica.order_system.reponse.ApiResponse;
import jakarta.persistence.OptimisticLockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalHandleExceptions {
    //400 bad request (@valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidError(MethodArgumentNotValidException ex){
        Map<String,Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((err)->
                errors.put(err.getField(),err.getDefaultMessage())
                );
        log.warn("Invalid Request error: ",ex);
        ApiResponse res = new ApiResponse("error","Bad Request",null,errors);
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    //401 Invalid Credentials
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleBadCredentialsError(BadCredentialsException ex){
        log.error("Bad credentials error: ",ex);
        return new ResponseEntity<>(
                new ApiResponse("error","Invalid Credentials"),
                HttpStatus.UNAUTHORIZED
        );
    }

    //404 Resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundError(ResourceNotFoundException ex){
        log.warn("Resource not found error: ",ex);
        return new ResponseEntity<>(
                new ApiResponse("error",ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    //409 Already Exists
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleResourceAlreadyExistsError(ResourceAlreadyExistsException ex){
        log.warn("Already exists error: ",ex);
        return new ResponseEntity<>(
                new ApiResponse("error",ex.getMessage()),
                HttpStatus.CONFLICT
        );
    }

    //400 Business Exception
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse> handleBusinessException(BusinessException ex){
        log.warn("Business error: ",ex);
        return new ResponseEntity<>(
                new ApiResponse("error",ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    //409 conflict optimistic
    @ExceptionHandler({OptimisticLockException.class, OptimisticLockingFailureException.class})
    public ResponseEntity<ApiResponse> handleOptimisticLockError(Exception ex){
        log.warn("Optimistic lock error: ",ex);
        return new ResponseEntity<>(
                new ApiResponse("error","The resource was updated by another request. Please retry."),
                HttpStatus.CONFLICT
        );
    }

    // stripe exception error
    @ExceptionHandler(StripeException.class)
    public ResponseEntity<ApiResponse> handleStripeError(StripeException ex){
        log.error("Stripe error: ",ex);
        return new ResponseEntity<>(
                new ApiResponse("error","Payment error! Please try again"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
