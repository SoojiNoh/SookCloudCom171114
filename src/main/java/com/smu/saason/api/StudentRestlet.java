package com.smu.saason.api;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    public String students() throws ParseException, IOException, SQLException {
    	List<Student> students = studentDAO.getStudents();
    	return toJson(students);
    }

	
}
