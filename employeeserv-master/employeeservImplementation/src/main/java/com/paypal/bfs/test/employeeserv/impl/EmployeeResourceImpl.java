package com.paypal.bfs.test.employeeserv.impl;

import com.paypal.bfs.test.employeeserv.EmployeeRepository;
import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.sun.istack.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implementation class for employee resource.
 */
@RestController
public class EmployeeResourceImpl implements EmployeeResource {
	
	@Autowired
	EmployeeRepository employeeRepository;

    @Override
    public ResponseEntity<Object> employeeGetById(String id) {

    	Employee employee = employeeRepository.findById(Integer.parseInt(id)).orElse(null);

    	if(Objects.isNull(employee)) {
    		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    	}
    	return new ResponseEntity<>(employee, HttpStatus.OK);
    }
    
    @Override
    public ResponseEntity<Object> employeePostById(@Valid @RequestBody Employee employee) {
    	try {
    		Employee emp = employeeRepository.findById(employee.getId()).orElse(null);
    		//Idempotency logic
    		if(!Objects.isNull(emp)) {
    			return new ResponseEntity<>("Employee with same id already present", HttpStatus.ALREADY_REPORTED);
    		}
    		employeeRepository.save(employee);
    	}catch(Exception e) {
    		return new ResponseEntity<>("Incorrect Request", HttpStatus.BAD_REQUEST);
    	}
    	return new ResponseEntity<>(employee, HttpStatus.OK);
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
