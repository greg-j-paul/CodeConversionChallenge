package com.example.kitchensink.service;

import com.example.kitchensink.exception.DuplicateEmailException;
import com.example.kitchensink.exception.MemberNotFoundException;
import com.example.kitchensink.model.Member;
import com.example.kitchensink.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private Member member1;
    private Member member2;

    @BeforeEach
    void setUp() {
        member1 = new Member();
        member1.setId("1");
        member1.setName("John Smith");
        member1.setEmail("john.smith@mailinator.com");
        member1.setPhoneNumber("2125551212");

        member2 = new Member();
        member2.setId("2");
        member2.setName("Jane Smith");
        member2.setEmail("jane.smith@example.com");
        member2.setPhoneNumber("0987654321");
    }

    @Test
    void registerNewMember_success() {
        when(memberRepository.findByEmail(member1.getEmail())).thenReturn(null);
        when(memberRepository.save(member1)).thenReturn(member1);

        Member registeredMember = memberService.register(member1);

        assertNotNull(registeredMember);
        assertEquals(member1.getName(), registeredMember.getName());
        verify(memberRepository, times(1)).save(member1);
    }

    @Test
    void registerNewMember_duplicateEmail_throwsException() {
        when(memberRepository.findByEmail(member1.getEmail())).thenReturn(member1);

        assertThrows(DuplicateEmailException.class, () -> memberService.register(member1));
    }

    @Test
    void getAllMembers_success() {
        List<Member> members = new ArrayList<>();
        members.add(member1);
        members.add(member2);

        when(memberRepository.findAll()).thenReturn(members);

        List<Member> allMembers = memberService.getAllMembers();

        assertNotNull(allMembers);
        assertEquals(2, allMembers.size());
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void getMemberById_success() {
        when(memberRepository.findById(member1.getId())).thenReturn(Optional.of(member1));

        Member retrievedMember = memberService.getMemberById(member1.getId());

        assertNotNull(retrievedMember);
        assertEquals(member1.getId(), retrievedMember.getId());
        verify(memberRepository, times(1)).findById(member1.getId());
    }

    @Test
    void getMemberById_notFound_throwsException() {
        String nonExistentId = "3";
        when(memberRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberService.getMemberById(nonExistentId));
    }

    @Test
    void deleteMember_success() {
        String memberId = member1.getId();
        when(memberRepository.existsById(memberId)).thenReturn(true);

        memberService.deleteMember(memberId);

        verify(memberRepository, times(1)).deleteById(memberId);
    }

    @Test
    void deleteMember_notFound_throwsException() {
        String nonExistentId = "3";
        when(memberRepository.existsById(nonExistentId)).thenReturn(false);

        assertThrows(MemberNotFoundException.class, () -> memberService.deleteMember(nonExistentId));
    }

    @Test
    void updateMember_success() {
        String memberId = member1.getId();
        Member updatedMember = new Member();
        updatedMember.setId(memberId);
        updatedMember.setName("Updated Name");
        updatedMember.setEmail("updated.email@example.com");
        updatedMember.setPhoneNumber("1122334455");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member1));
        when(memberRepository.findByEmail(updatedMember.getEmail())).thenReturn(null);
        when(memberRepository.save(any(Member.class))).thenReturn(updatedMember);

        Member result = memberService.updateMember(memberId, updatedMember);

        assertNotNull(result);
        assertEquals(updatedMember.getName(), result.getName());
        assertEquals(updatedMember.getEmail(), result.getEmail());
        assertEquals(updatedMember.getPhoneNumber(), result.getPhoneNumber());
        verify(memberRepository, times(1)).save(member1);
    }

    @Test
    void updateMember_notFound_throwsException() {
        String nonExistentId = "3";
        Member updatedMember = new Member();
        updatedMember.setId(nonExistentId);
        updatedMember.setName("Updated Name");
        updatedMember.setEmail("updated.email@example.com");
        updatedMember.setPhoneNumber("1122334455");

        when(memberRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberService.updateMember(nonExistentId, updatedMember));
    }

    @Test
    void updateMember_duplicateEmail_throwsException() {
        String memberId = member1.getId();
        Member updatedMember = new Member();
        updatedMember.setId(memberId);
        updatedMember.setName("Updated Name");
        updatedMember.setEmail("jane.smith@example.com");
        updatedMember.setPhoneNumber("1122334455");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member1));
        when(memberRepository.findByEmail(updatedMember.getEmail())).thenReturn(member2);

        assertThrows(DuplicateEmailException.class, () -> memberService.updateMember(memberId, updatedMember));
    }
}

