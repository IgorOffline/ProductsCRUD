package practice.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import practice.common.MessageRes;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestController
@ControllerAdvice
public class MyRestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var list = new ArrayList<MessageRes>();
        ex.getBindingResult().getAllErrors().forEach(error -> list.add(new MessageRes(error.getDefaultMessage())));
        return new ResponseEntity(list, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    public List<MessageRes> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        var list = new ArrayList<MessageRes>();
        ex.getConstraintViolations().forEach(violation -> list.add(new MessageRes(violation.getMessage())));
        return list;
    }
}