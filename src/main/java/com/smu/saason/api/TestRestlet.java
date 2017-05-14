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

/**
 * Created by mint on 04/05/2017.
 */

@RestController
@RequestMapping("/api")
public class TestRestlet extends AbstractRestlet{
	@Autowired
	StudentDAO studentDAO;
	
    @RequestMapping(method = RequestMethod.GET, value = "/test", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String search() throws ParseException, IOException, SQLException {
    	List<Student> students = studentDAO.getStudents();
    	return toJsonstring(students);
    }

	
}
