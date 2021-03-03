package com.paypal.bfs.test.employeeserv.api;

import com.paypal.bfs.test.employeeserv.api.model.Employee;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Interface for employee resource operations.
 */
public interface EmployeeResource {

    /**
     * Retrieves the {@link Employee} resource by id.
     *
     * @param id employee id.
     * @return {@link Employee} resource.
     */
    @RequestMapping("/v1/bfs/employees/{id}")
    ResponseEntity<Object> employeeGetById(@PathVariable("id") String id);

    /**
     * Adds the {@link Employee} resource by id.
     * 
     * @param employee The Employee object to be added in the database
     * @return The employee object added in database in success scenerio and error message in failure.
     */
    @RequestMapping(value="/v1/bfs/employees/create/", method = RequestMethod.POST)
    ResponseEntity<Object> employeePostById(@Valid @RequestBody Employee employee);
}
