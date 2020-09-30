//package com.commissions.galaxy.exceptions;
//
//import java.util.Date;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//@ControllerAdvice
//@RestController
//public class ExceptionResponseHandler extends ResponseEntityExceptionHandler  {
//	   @ExceptionHandler(Exception.class)
//	    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest req) {
//		   System.out.println("IN EXCEPTION HANDLER");
//	        ExceptionResponse exceptionResponse = new ExceptionResponse(
//	            new Date(),
//	            ex.getMessage(),
//	            req.getDescription(false)
//	        );
//	        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//	    }
//}
