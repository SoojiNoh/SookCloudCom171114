package com.smu.saason.api;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smu.saason.bean.Student;
import com.smu.saason.dao.StudentDAO;

import net.minidev.json.parser.ParseException;

@RestController
@RequestMapping("/api")
public class StudentRestlet extends AbstractRestlet{
	@Autowired
	StudentDAO studentDAO;
	
    @RequestMapping(method = RequestMethod.GET, value = "/students", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getStudents() throws ParseException, IOException, SQLException {
    	List<Student> students = studentDAO.getStudents();
    	return toJson(students);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/students/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void createNewStudents(
    		@PathVariable(required=true) String id,
    		@RequestParam(required=true) String name)
    throws ParseException, IOException, SQLException {
    	System.out.println(id + ", " + name);
    	studentDAO.insert(new Student(name, "email", "pass", System.currentTimeMillis()));
    	// return toJson(students);
    }
}
