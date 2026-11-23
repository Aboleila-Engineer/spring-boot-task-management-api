package com.saad.tasks;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.*;
@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("demo")
class ApiIntegrationTest {
 @Autowired MockMvc mvc;
 @Autowired UserRepository users;
 @Autowired TaskRepository tasks;
 @BeforeEach void reset(){tasks.deleteAll(); users.deleteAll();}
 @Test void servesHome() throws Exception {mvc.perform(get("/")).andExpect(status().isOk());}
 @Test void taskLifecycleWithAssignment() throws Exception {
 mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content("{\"fullName\":\"Demo User\",\"email\":\"demo@example.com\"}")).andExpect(status().isCreated());
 long userId=users.findAll().get(0).id;
 mvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content("{\"description\":\"Write tests\",\"completed\":false,\"userId\":"+userId+"}")).andExpect(status().isCreated()).andExpect(jsonPath("$.userId").value(userId));
 long id=tasks.findAll().get(0).id;
 mvc.perform(get("/api/tasks/"+id)).andExpect(status().isOk()).andExpect(jsonPath("$.userId").value(userId));
 mvc.perform(get("/api/tasks")).andExpect(status().isOk()).andExpect(jsonPath("$[0].userId").value(userId));
 mvc.perform(put("/api/tasks/"+id).contentType(MediaType.APPLICATION_JSON).content("{\"description\":\"Review tests\",\"completed\":false,\"userId\":null}")).andExpect(status().isOk());
 mvc.perform(patch("/api/tasks/"+id).contentType(MediaType.APPLICATION_JSON).content("{\"completed\":true}")).andExpect(status().isOk()).andExpect(jsonPath("$.completed").value(true));
 mvc.perform(delete("/api/tasks/"+id)).andExpect(status().isNoContent());
 mvc.perform(get("/api/tasks/"+id)).andExpect(status().isNotFound());
 }
 @Test void rejectsInvalidTaskAndMissingAssignee() throws Exception {
 mvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isBadRequest());
 mvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content("{\"description\":\"Task\",\"completed\":false,\"userId\":999999}")).andExpect(status().isNotFound());
 }
 @Test void rejectsDuplicateEmail() throws Exception {
 String data="{\"fullName\":\"Demo\",\"email\":\"demo@example.com\"}";
 mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(data)).andExpect(status().isCreated());
 mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(data)).andExpect(status().isConflict());
 }
}
