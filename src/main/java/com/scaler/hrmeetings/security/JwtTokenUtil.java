package com.scaler.hrmeetings.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public class JwtTokenUtil {

    private static final Set<String> READ_ROLES = Set.of(JwtRole.READ.name(), JwtRole.WRITE.name());
    private static final Set<String> WRITE_ROLES = Set.of(JwtRole.WRITE.name());

    // just for testing in Postman to have temporary token, not necessary 
    public static String generateToken(Long callerId, JwtRole role, long expirationMillis) {
        return Jwts.builder()
                .setSubject("user@example.com")
                .addClaims(Map.of(
                        "role", role.name(),
                        "callerId", callerId
                ))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(SignatureAlgorithm.HS256, JwtAuthenticationFilter.key)
                .compact();
    }

    public static void checkRoleInToken(JwtRole expectedRole) {
        JwtUserDetails user = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userRole = user.getRole();

        boolean hasAccess = switch (expectedRole) {
            case WRITE -> WRITE_ROLES.contains(userRole);
            case READ  -> READ_ROLES.contains(userRole);
            default -> false;
        };

        if (!hasAccess) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Missing role");
        }
    }
}
