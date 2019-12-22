package com.example.demo.error;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.exmple.demo.utilities.Utilities;

@ControllerAdvice
public class DemoAppExeptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(DemoAppExeption.class)
	@ResponseBody
	public ResponseEntity<ErrorResponse> handleNotFound(Exception e) throws IOException {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setError(e.getMessage());
		errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
		errorResponse.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = { MethodArgumentTypeMismatchException.class, MalformedURLException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<ErrorResponse> handleBadRequestException(Exception e) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		errorResponse.setTimestamp(LocalDateTime.now());

		if (e instanceof MethodArgumentTypeMismatchException) {
			errorResponse.setError("MethodArgumentTypeMismatchException : " + e.getMessage());
			String parameter = ((MethodArgumentTypeMismatchException) e).getName();
			if (parameter.equalsIgnoreCase("addedAfter") || parameter.equalsIgnoreCase("addedBefore")) {
				parameter = "Use the Format " + Utilities.Date_Format + " for " + parameter;

			}
			errorResponse.setContext(parameter);

		} else if (e instanceof MalformedURLException) {
			errorResponse.setError("MalformedURLException : " + e.getMessage());
		}

		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

}
