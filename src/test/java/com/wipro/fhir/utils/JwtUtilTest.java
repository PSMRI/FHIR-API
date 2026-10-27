/*
* AMRIT - Accessible Medical Records via Integrated Technologies
* Integrated EHR (Electronic Health Records) Solution
*
* Copyright (C) "Piramal Swasthya Management and Research Institute"
*
* This file is part of AMRIT.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see https://www.gnu.org/licenses/.
*/
package com.wipro.fhir.utils;

import java.util.Date;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtUtil Test Suite")
class JwtUtilTest {

    private static final String SECRET = "amrit-bengen-test-secret-key-that-is-long-enough-for-hs256";
    private static final String OTHER_SECRET = "a-completely-different-secret-key-also-long-enough-for-hs256";

    @Mock
    private TokenDenylist tokenDenylist;

    private JwtUtil jwtUtil;

    @BeforeEach
    @DisplayName("Wire the util with a test secret and a mocked denylist before each test")
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "SECRET_KEY", SECRET);
        ReflectionTestUtils.setField(jwtUtil, "tokenDenylist", tokenDenylist);
    }

    private String token(String secret, String subject, String jti, Date expiry) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        var builder = Jwts.builder().subject(subject).signWith(key);
        if (jti != null) {
            builder.id(jti);
        }
        if (expiry != null) {
            builder.expiration(expiry);
        }
        return builder.compact();
    }

    private String validToken(String subject, String jti) {
        return token(SECRET, subject, jti, new Date(System.currentTimeMillis() + 600_000));
    }

    @Nested
    @DisplayName("validateToken")
    class ValidateTokenTests {

        @Test
        @DisplayName("validateToken should return the claims for a correctly signed token")
        void validateToken_shouldReturnClaimsForValidToken() {
            when(tokenDenylist.isTokenDenylisted("jti-1")).thenReturn(false);

            Claims claims = jwtUtil.validateToken(validToken("amrit-user", "jti-1"));

            assertNotNull(claims);
            assertEquals("amrit-user", claims.getSubject());
            assertEquals("jti-1", claims.getId());
        }

        @Test
        @DisplayName("validateToken should skip the denylist check for a token without a jti")
        void validateToken_shouldSkipDenylistCheckWithoutJti() {
            Claims claims = jwtUtil.validateToken(validToken("amrit-user", null));

            assertNotNull(claims);
            assertEquals("amrit-user", claims.getSubject());
        }

        @Test
        @DisplayName("validateToken should reject a token whose jti has been denylisted")
        void validateToken_shouldRejectDenylistedToken() {
            when(tokenDenylist.isTokenDenylisted("jti-1")).thenReturn(true);

            assertNull(jwtUtil.validateToken(validToken("amrit-user", "jti-1")));
        }

        @Test
        @DisplayName("validateToken should reject a token signed with a different secret")
        void validateToken_shouldRejectTokenSignedWithDifferentSecret() {
            String foreign = token(OTHER_SECRET, "amrit-user", "jti-1",
                    new Date(System.currentTimeMillis() + 600_000));

            assertNull(jwtUtil.validateToken(foreign));
        }

        @Test
        @DisplayName("validateToken should reject an expired token")
        void validateToken_shouldRejectExpiredToken() {
            String expired = token(SECRET, "amrit-user", "jti-1",
                    new Date(System.currentTimeMillis() - 60_000));

            assertNull(jwtUtil.validateToken(expired));
        }

        @Test
        @DisplayName("validateToken should reject a malformed token")
        void validateToken_shouldRejectMalformedToken() {
            assertNull(jwtUtil.validateToken("not-a-jwt"));
        }

        @Test
        @DisplayName("validateToken should reject a null token")
        void validateToken_shouldRejectNullToken() {
            assertNull(jwtUtil.validateToken(null));
        }

        @Test
        @DisplayName("validateToken should reject every token when no secret is configured")
        void validateToken_shouldRejectWhenSecretIsNotConfigured() {
            String signed = validToken("amrit-user", null);
            ReflectionTestUtils.setField(jwtUtil, "SECRET_KEY", null);

            assertNull(jwtUtil.validateToken(signed));
        }
    }

    @Nested
    @DisplayName("Claim extraction")
    class ClaimExtractionTests {

        @Test
        @DisplayName("extractUsername should return the token subject")
        void extractUsername_shouldReturnSubject() {
            assertEquals("amrit-user", jwtUtil.extractUsername(validToken("amrit-user", null)));
        }

        @Test
        @DisplayName("extractClaim should apply the supplied resolver to the claims")
        void extractClaim_shouldApplySuppliedResolver() {
            lenient().when(tokenDenylist.isTokenDenylisted("jti-9")).thenReturn(false);

            assertEquals("jti-9", jwtUtil.extractClaim(validToken("amrit-user", "jti-9"), Claims::getId));
        }

        @Test
        @DisplayName("extractClaim should raise when the token cannot be parsed")
        void extractClaim_shouldRaiseForMalformedToken() {
            assertThrows(Exception.class, () -> jwtUtil.extractClaim("not-a-jwt", Claims::getSubject));
        }

        @Test
        @DisplayName("extractUsername should raise when no secret is configured")
        void extractUsername_shouldRaiseWhenSecretIsNotConfigured() {
            String signed = validToken("amrit-user", null);
            ReflectionTestUtils.setField(jwtUtil, "SECRET_KEY", "");

            assertThrows(IllegalStateException.class, () -> jwtUtil.extractUsername(signed));
        }
    }
}
