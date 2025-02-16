package com.example.kitchensink.controller;

import com.example.kitchensink.model.Member;
import com.example.kitchensink.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class MemberWebControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() { 
        memberRepository.deleteAll();
    }
        
    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");
    
    
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
            registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }
       
            

    @Test
    void registerMember_success() throws Exception {
        mockMvc.perform(post("/kitchensink/index")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "John Smith")
                        .param("email", "john.smith@mailinator.com")
                        .param("phoneNumber", "2125551212"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("index"));
    }

    @Test
    void registerMember_invalidData_returnsRegisterForm() throws Exception {
        mockMvc.perform(post("/kitchensink/index")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "")
                        .param("email", "invalid-email")
                        .param("phoneNumber", "abc"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeHasFieldErrors("newmember", "name", "email", "phoneNumber"));
    }



}

