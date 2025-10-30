package com.ghallab.Ghallab_Bank.exceptions;

import com.ghallab.Ghallab_Bank.res.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<?>> handleAllUnkownException( Exception ex){
            Response<?> response = Response.builder().
                    statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message(ex.getMessage())
                                    .build();

            return new ResponseEntity<>(response , HttpStatus.INTERNAL_SERVER_ERROR)  ;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response<?>> handleNotFoundException( NotFoundException ex){
        Response<?> response = Response.builder().
                statusCode(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response , HttpStatus.NOT_FOUND)  ;
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response<?>> handleBadRequestException( BadRequestException ex){
        Response<?> response = Response.builder().
                statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST)  ;
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Response<?>> handleInsufficientBalanceException( InsufficientBalanceException ex){
        Response<?> response = Response.builder().
                statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST)  ;
    }
    @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<Response<?>> handleInvalidTransactionException( InvalidTransactionException ex){
        Response<?> response = Response.builder().
                statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST)  ;
    }


}
