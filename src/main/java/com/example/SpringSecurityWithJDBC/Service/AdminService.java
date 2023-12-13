package com.example.SpringSecurityWithJDBC.Service;

import com.example.SpringSecurityWithJDBC.Entity.Member;
import com.example.SpringSecurityWithJDBC.enums.StatusType;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

public interface AdminService {

    @Transactional
    ResponseEntity<String> approve(Integer id, String status) throws Exception;

    String declineMember(Member optionalMember, StatusType status, String email, String memberName) throws Exception;

    //ResponseEntity<String> sendOtp(Integer MemberId) throws Exception;

    @Transactional
    ResponseEntity<String> triggerMail(Integer MemberId, String ApproveStatus) throws Exception;
}
