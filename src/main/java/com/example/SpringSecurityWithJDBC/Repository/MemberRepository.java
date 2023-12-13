package com.example.SpringSecurityWithJDBC.Repository;

import com.example.SpringSecurityWithJDBC.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

}
