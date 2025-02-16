package com.example.kitchensink.controller;

import com.example.kitchensink.model.Member;
import com.example.kitchensink.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import org.testcontainers.containers.MongoDBContainer;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class MemberRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Member member1;

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void setUp() throws Exception {
        memberRepository.deleteAll();

        member1 = new Member();
        member1.setName("John Doe");
        member1.setEmail("john.doe@example.com");
        member1.setPhoneNumber("2125551212");
    }

    @Test
    void createMember_success() throws Exception {
        mockMvc.perform(post("/kitchensink/rest/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(member1.getName())))
                .andExpect(jsonPath("$.email", is(member1.getEmail())));
    }

    @Test
    void createMember_invalidData_returnsBadRequest() throws Exception {
        Member invalidMember = new Member();
        invalidMember.setName(""); // Invalid name

        mockMvc.perform(post("/kitchensink/rest/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidMember)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllMembers_success() throws Exception {
        memberRepository.save(member1);

        mockMvc.perform(get("/kitchensink/rest/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(member1.getName())));
    }

    @Test
    void getMemberById_success() throws Exception {
        memberRepository.save(member1);

        mockMvc.perform(get("/kitchensink/rest/members/" + member1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(member1.getName())));
    }

    @Test
    void getMemberById_notFound_returnsNotFound() throws Exception {
        mockMvc.perform(get("/kitchensink/rest/members/nonExistentId"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateMember_success() throws Exception {
        memberRepository.save(member1);

        Member updatedMember = new Member();
        updatedMember.setId(member1.getId());
        updatedMember.setName("Updated Name");
        updatedMember.setEmail("updated.email@example.com");
        updatedMember.setPhoneNumber("0000000000");

        mockMvc.perform(put("/kitchensink/rest/members/" + member1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedMember)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updatedMember.getName())))
                .andExpect(jsonPath("$.email", is(updatedMember.getEmail())));
    }

    @Test
    void updateMember_notFound_returnsNotFound() throws Exception {
        Member updatedMember = new Member();
        updatedMember.setId("nonExistentId");
        updatedMember.setName("Updated Name");
        updatedMember.setEmail("updated.email@example.com");
        updatedMember.setPhoneNumber("0000000000");

        mockMvc.perform(put("/kitchensink/rest/members/nonExistentId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedMember)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMember_success() throws Exception {
        memberRepository.save(member1);

        mockMvc.perform(delete("/kitchensink/rest/members/" + member1.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteMember_notFound_returnsNotFound() throws Exception {
        mockMvc.perform(delete("/kitchensink/rest/members/nonExistentId"))
                .andExpect(status().isNotFound());
    }
}

