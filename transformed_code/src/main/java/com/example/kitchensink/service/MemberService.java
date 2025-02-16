package com.example.kitchensink.service;

import com.example.kitchensink.exception.DuplicateEmailException;
import com.example.kitchensink.exception.MemberNotFoundException;
import com.example.kitchensink.model.Member;
import com.example.kitchensink.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public Member register(Member member) {
        if (memberRepository.findByEmail(member.getEmail()) != null) {
            throw new DuplicateEmailException("Email already exists");
        }
        return memberRepository.save(member);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberById(String id) {
        Optional<Member> member = memberRepository.findById(id);
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new MemberNotFoundException("Member not found with id: " + id);
        }
    }

    public void deleteMember(String id) {
        if (!memberRepository.existsById(id)) {
            throw new MemberNotFoundException("Member not found with id: " + id);
        }
        memberRepository.deleteById(id);
    }

    public Member updateMember(String id, Member member) {
       Optional<Member> existingMemberOptional = memberRepository.findById(id);

       if(existingMemberOptional.isEmpty()) {
           throw new MemberNotFoundException("Member not found with id: " + id);
       }

       Member existingMember = existingMemberOptional.get();

       if(!existingMember.getEmail().equals(member.getEmail()) && memberRepository.findByEmail(member.getEmail()) != null) {
           throw new DuplicateEmailException("Email already exists");
       }

       existingMember.setName(member.getName());
       existingMember.setEmail(member.getEmail());
       existingMember.setPhoneNumber(member.getPhoneNumber());

       return memberRepository.save(existingMember);
    }
}

