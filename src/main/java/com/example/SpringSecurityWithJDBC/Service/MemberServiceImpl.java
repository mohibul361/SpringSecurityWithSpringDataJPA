package com.example.SpringSecurityWithJDBC.Service;

import com.example.SpringSecurityWithJDBC.Entity.Member;
import com.example.SpringSecurityWithJDBC.Repository.MemberRepository;
import com.example.SpringSecurityWithJDBC.Request.MemberRequest;
import com.example.SpringSecurityWithJDBC.enums.StatusType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    @Override
    public ResponseEntity<String> addMember(MemberRequest memberRequest){

        Member member = Member.builder()
                .memberName(memberRequest.getMemberName())
                .email(memberRequest.getEmail())
                .mobileNumber(memberRequest.getMobileNumber())
                .build();

        member.setStatusType(StatusType.valueOf("PENDING"));

        memberRepository.save(member);

        return ResponseEntity.ok("Member added successfully!");

    }
    @Override
    public ResponseEntity<?> getAMember(MemberRequest memberRequest){

        Optional<Member> memberOptional = memberRepository.findById(memberRequest.getId());

        if(memberOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member with this id not found!");
        }

        Member member = memberOptional.get();

        MemberRequest memberProfile = MemberRequest.builder()
                .memberName(member.getMemberName())
                .email(member.getEmail())
                .mobileNumber(member.getMobileNumber())
                .statusType(member.getStatusType())
                .build();

        return ResponseEntity.ok(memberProfile);
    }
    @Override
    public List<Member> getAllMember(){
        return memberRepository.findAll();
    }
}
