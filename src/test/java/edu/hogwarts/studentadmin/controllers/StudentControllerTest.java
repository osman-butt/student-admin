package edu.hogwarts.studentadmin.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hogwarts.studentadmin.models.ColorType;
import edu.hogwarts.studentadmin.models.House;
import edu.hogwarts.studentadmin.models.HouseColor;
import edu.hogwarts.studentadmin.models.Student;
import edu.hogwarts.studentadmin.services.StudentService;
import edu.hogwarts.studentadmin.services.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Student student;

    @BeforeEach
    public void initData() {
        House gryffindor = new House("Gryffindor","Godric Gryffindor",new HouseColor(ColorType.RED,ColorType.YELLOW));
        this.student = new Student("FIRST",null,"LAST", LocalDate.parse("1980-01-05"),gryffindor,false,1991,null,false);
    }

    @Test
    public void StudentController_CreateStudent_ReturnCreated() throws Exception {
        given(studentService.createStudent(ArgumentMatchers.any())).willAnswer(invocation ->  invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));

        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
