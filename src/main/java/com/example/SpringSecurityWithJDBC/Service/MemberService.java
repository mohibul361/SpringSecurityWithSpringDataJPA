package com.example.SpringSecurityWithJDBC.Service;

import com.example.SpringSecurityWithJDBC.Entity.Member;
import com.example.SpringSecurityWithJDBC.Request.MemberRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MemberService {

    ResponseEntity<String> addMember(MemberRequest memberRequest);

    ResponseEntity<?> getAMember(MemberRequest memberRequest);

    List<Member> getAllMember();
}
