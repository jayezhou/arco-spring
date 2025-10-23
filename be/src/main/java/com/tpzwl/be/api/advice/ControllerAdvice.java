package com.tpzwl.be.api.advice;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.tpzwl.be.api.payload.response.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class ControllerAdvice {

	private static final Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);

	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Response<String> handleException(Exception ex, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		return new Response<String>((long) HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), null);
	}

	@ExceptionHandler(value = AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public Response<String> handleAccessDeniedException(Exception ex, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		return new Response<String>((long) HttpStatus.FORBIDDEN.value(), ex.getMessage(), null);
	}

}