package com.paypal.bfs.test.employeeserv.api;

import static org.junit.Assert.assertTrue;

import java.net.http.HttpHeaders;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.api.model.Address;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeResourceTest {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = null;
    @Test
    public void testCreateEmployeeSuccess() throws Exception {
        Employee emp = new Employee();
        Address addr = new Address();
        addr.setCity("city1");
        addr.setCountry("country1");
        addr.setLine1("line_1");
        addr.setLine2("line_2");
        addr.setState("state1");
        addr.setZipCode("7112");
        emp.setFirstName("fn1");
        emp.setLastName("ln1");
        emp.setId(123);
        emp.setAddress(addr);
        HttpEntity<Employee> entity = new HttpEntity<Employee>(emp,null);
        ResponseEntity<Employee> response_post = restTemplate.exchange(
          createURLWithPort("/v1/bfs/employees/create/"), HttpMethod.POST, entity, Employee.class);

        ResponseEntity<Employee> response_get = restTemplate.exchange(
                createURLWithPort("/v1/bfs/employees/123"), HttpMethod.GET, null, Employee.class);
        String firstname_get = ((HttpEntity<Employee>) response_get).getBody().getFirstName();
        String firstname_post = ((HttpEntity<Employee>) response_post).getBody().getFirstName();
        
        assertTrue(firstname_get.equals("fn1"));
        assertTrue(firstname_post.equals("fn1"));
    }    
    
    
    @Test
    public void testCreateEmployeeFailure() throws Exception {
        Employee emp = new Employee();
        Address addr = new Address();
        addr.setCity("city1");
        addr.setCountry("country1");
        addr.setLine1("line_1");
        addr.setLine2("line_2");
        addr.setState("state1");
        addr.setZipCode("7112");
        emp.setLastName("ln1");
        emp.setAddress(addr);
        HttpEntity<Employee> entity = new HttpEntity<Employee>(emp,null);
        ResponseEntity<String> response_post = restTemplate.exchange(
          createURLWithPort("/v1/bfs/employees/create/"), HttpMethod.POST, entity, String.class);

        ResponseEntity<String> response_get = restTemplate.exchange(
                createURLWithPort("/v1/bfs/employees/456"), HttpMethod.GET, null, String.class);
        HttpStatus status_get = response_get.getStatusCode();
        HttpStatus status_post = response_post.getStatusCode();
        
        assertTrue(status_get.value() == 204);
        assertTrue(status_post.is4xxClientError());
    }

   

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }


}