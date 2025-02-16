package com.example.kitchensink.config;

import com.example.kitchensink.model.Member;
import com.example.kitchensink.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Autowired
    private MemberRepository memberRepository;

    @Bean
    public ApplicationRunner initializeData() {
        return args -> {
            if (memberRepository.findByEmail("john.smith@mailinator.com") == null) {
                Member member = new Member();
                member.setName("John Smith");
                member.setEmail("john.smith@mailinator.com");
                member.setPhoneNumber("2125551212");
                memberRepository.save(member);
            }
        };
    }
}