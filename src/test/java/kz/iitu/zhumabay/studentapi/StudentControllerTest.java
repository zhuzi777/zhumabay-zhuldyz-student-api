package kz.iitu.zhumabay.studentapi;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kz.iitu.zhumabay.studentapi.model.Student;
import kz.iitu.zhumabay.studentapi.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    private static final String ZHULDYZ_JSON = """
            {"name":"Zhuldyz","surname":"Zhumabay","email":"zhuldyz.zhumabay@iitu.edu.kz","groupName":"IT2-2308"}
            """;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void infoShowsStudentName() throws Exception {
        mockMvc.perform(get("/api/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value("Zhuldyz Zhumabay"));
    }

    @Test
    void createStudent() throws Exception {
        mockMvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON).content(ZHULDYZ_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.surname").value("Zhumabay"));
    }

    @Test
    void getAllAndGetById() throws Exception {
        Student saved = repository.save(new Student("Zhuldyz", "Zhumabay", "zhuldyz.zhumabay@iitu.edu.kz", "IT2-2308"));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(get("/api/students/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Zhuldyz"));
    }

    @Test
    void updateStudent() throws Exception {
        Student saved = repository.save(new Student("Zhuldyz", "Zhumabay", "old@iitu.edu.kz", "IT2-2308"));
        String changes = """
                {"name":"Zhuldyz","surname":"Zhumabay","email":"zhuldyz.new@iitu.edu.kz","groupName":"IT2-2308"}
                """;

        mockMvc.perform(put("/api/students/" + saved.getId()).contentType(MediaType.APPLICATION_JSON).content(changes))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("zhuldyz.new@iitu.edu.kz"));
    }

    @Test
    void deleteStudent() throws Exception {
        Student saved = repository.save(new Student("Zhuldyz", "Zhumabay", null, "IT2-2308"));

        mockMvc.perform(delete("/api/students/" + saved.getId()))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/students/" + saved.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void nonexistentStudentReturns404() throws Exception {
        mockMvc.perform(get("/api/students/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Student with id 999 not found"));
    }

    @Test
    void invalidStudentReturns400() throws Exception {
        mockMvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void filterStudentsBySurname() throws Exception {
        repository.save(new Student("Zhuldyz", "Zhumabay", null, "IT2-2308"));
        repository.save(new Student("Asan", "Abai", null, "IT2-2308"));

        mockMvc.perform(get("/api/students").param("surname", "zhumabay"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].surname").value("Zhumabay"));
    }
}
