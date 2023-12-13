package com.example.SpringSecurityWithJDBC.Service;

import com.example.SpringSecurityWithJDBC.Entity.Member;
import com.example.SpringSecurityWithJDBC.Repository.MemberRepository;
import com.example.SpringSecurityWithJDBC.async.AsyncMailSender;
import com.example.SpringSecurityWithJDBC.enums.StatusType;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.lookups.v1.PhoneNumber;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import static com.example.SpringSecurityWithJDBC.enums.StatusType.APPROVED;
import static com.example.SpringSecurityWithJDBC.enums.StatusType.DECLINE;

@Service
public class AdminServiceImpl implements AdminService{

    private final MemberRepository memberRepository;

    private final AsyncMailSender asyncMailSender;

    private final JavaMailSender javaMailSender;


    public AdminServiceImpl(MemberRepository memberRepository, AsyncMailSender asyncMailSender, JavaMailSender javaMailSender) {
        this.memberRepository = memberRepository;
        this.asyncMailSender = asyncMailSender;
        this.javaMailSender = javaMailSender;
    }

    @Override
    @Transactional
    public ResponseEntity<String> approve(Integer memberId, String status) throws Exception{

        System.out.println("Member Id : " + memberId);

        Optional<Member> optionalMember = memberRepository.findById(Long.valueOf(memberId));

        if(optionalMember.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member with this id not found!");
        }

        Member member = optionalMember.get();

        String email = member.getEmail();

        System.out.println(email);

        if(status.equals("APPROVED")){

            if(!Objects.equals(member.getStatusType(), StatusType.APPROVED)) {

                String username = String.valueOf(member.getMemberId());

                System.out.println("username : " + username);

                String password = generateTemporaryPassword();

                System.out.println("password : " + password);

                String memberName = member.getMemberName();

                member.setStatusType(APPROVED);

                memberRepository.save(member);

                asyncMailSender.sendEmail(email , username , password , memberName);

                return ResponseEntity.ok("Member approved successfully!");
            }
            else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member is already approved");
            }
        }
        else {
            String memberName = member.getMemberName();
            String message = declineMember(member, member.getStatusType(), email, memberName);

            if (message.equals("0")) {
                return ResponseEntity.ok("Member declined successfully");
            } else if (message.equals("1")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member already declined!");
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Member declined failed!");
            }

        }
    }

    @Override
    public String declineMember(Member optionalMember , StatusType status , String email , String memberName) throws Exception {

        try{
            InternetAddress internetAddress = new InternetAddress(email);

            internetAddress.validate();

            if(!Objects.equals(status, StatusType.DECLINE)){
                optionalMember.setStatusType(DECLINE);

                memberRepository.save(optionalMember);

                String body = "Dear "+memberName+"\n\n"
                        + "We regret to inform you that your account request has been declined.\n\n"
                        + "If you have any questions or concerns, please contact our support team.\n\n"
                        + "Thank you,\nYourCompany Support";
                String subject = "Account Request Declined";


                String toEmail = email;

                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("mohibul323@gmail.com");
                message.setTo(toEmail);
                message.setText(body);
                message.setSubject(subject);

                javaMailSender.send(message);

                return "Member declined successfully!";

            }
            return "Member declined!";
        }catch (Exception e){
            e.printStackTrace();
        }

        return "nothing";
     }

    public String generateTemporaryPassword() {

        int length = 6;

        SecureRandom random = new SecureRandom();

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }

        return password.toString();
    }

    @Transactional
    public String sendEmail(String email, String username, String password, String memberName) throws Exception {
        try {

            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();

            String body = "Dear " + memberName + "\n\n"
                    + "Your account has been created. Below are your login credentials:\n\n"
                    + "Username: " + username + "\n"
                    + "Password: " + password + "\n\n"
                    + "Please log in using these credentials and change your password after the first login.\n\n"
                    + "Thank you,\nYourCompany Support";
            String subject = "Account Created: Your Login Credentials";

            String toEmail = email;

            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("mohibul323@gmail.com");
            message.setTo(toEmail);
            message.setText(body);
            message.setSubject(subject);
            javaMailSender.send(message);

            return "Email sent successfully";
        }  catch (AddressException e) {

            System.out.println("Invalid email address: " + e.getMessage());
            throw new BadCredentialsException("Invalid email address");
        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("Sending email failed");
            throw new BadCredentialsException("Sending email failed");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> triggerMail(Integer MemberId, String StatusType) throws Exception {
        return approve(MemberId , StatusType);
    }

}
