package com.example.SpringSecurityWithJDBC.Request;

import com.example.SpringSecurityWithJDBC.enums.StatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberRequest {

    private Long id;

    private String memberName;

    private String email;

    private String mobileNumber;

    private StatusType statusType;

}
