package com.scaler.hrmeetings;

import com.scaler.hrmeetings.security.JwtRole;
import com.scaler.hrmeetings.security.JwtTokenUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HrMeetingsApplication {

    public static void main(String[] args) {
        // only for testing purpose to get token for PostMan
        String token = JwtTokenUtil.generateToken(1L, JwtRole.WRITE, 1000 * 60 * 60);
        System.out.println("Bearer " + token);

        SpringApplication.run(HrMeetingsApplication.class, args);
    }

}
