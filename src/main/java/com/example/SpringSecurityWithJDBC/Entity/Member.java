package com.example.SpringSecurityWithJDBC.Entity;

import com.example.SpringSecurityWithJDBC.enums.StatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String memberName;

    private String email;

    private String mobileNumber;

    private String otp;

    @Enumerated(EnumType.STRING)
    private StatusType statusType;

}
