package com.example.SpringSecurityWithJDBC.Controller;

import com.example.SpringSecurityWithJDBC.Entity.Member;
import com.example.SpringSecurityWithJDBC.Request.MemberRequest;
import com.example.SpringSecurityWithJDBC.Service.AdminService;
import com.example.SpringSecurityWithJDBC.Service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth/member")
public class MemberController {

    private final MemberService memberService;

    private final AdminService adminService;


    public MemberController(MemberService memberService, AdminService adminService) {
        this.memberService = memberService;
        this.adminService = adminService;
    }
    @PostMapping("/add")
    public ResponseEntity<String> addMember(@RequestBody MemberRequest memberRequest){
        return memberService.addMember(memberRequest);
    }
    @GetMapping("/profile")
    public ResponseEntity<?> memberProfile(MemberRequest memberRequest){
        return memberService.getAMember(memberRequest);
    }
    @GetMapping("/all")
    public List<Member> allMember(){
        return memberService.getAllMember();
    }

    @PostMapping("/memberApproval")
    public ResponseEntity<String> approvedMember(@RequestParam Integer MemberId , @RequestParam String StatusType) throws Exception {
        return adminService.triggerMail(MemberId , StatusType);
    }



}
