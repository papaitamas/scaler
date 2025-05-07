package com.scaler.hrmeetings;

import com.scaler.hrmeetings.security.JwtRole;
import com.scaler.hrmeetings.security.JwtTokenUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HrMeetingsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HrMeetingsApplication.class, args);
    }

}
