package com.wipro.fhir.utils;

import java.util.function.Function;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String SECRET_KEY;

	@Autowired
	private TokenDenylist tokenDenylist;

	// Generate a key using the secret
	private SecretKey getSigningKey() {
		if (SECRET_KEY == null || SECRET_KEY.isEmpty()) {
			throw new IllegalStateException("JWT secret key is not set in application.properties");
		}
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	}

	// Validate and parse JWT Token
	public Claims validateToken(String token) {
		try {
			Claims claims = Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
				
			String jti = claims.getId();
			
			// Check if token is denylisted (only if jti exists)
			if (jti != null && tokenDenylist.isTokenDenylisted(jti)) {
				return null;
			}
			
			return claims;
		} catch (Exception e) {
			return null; // Handle token parsing/validation errors
		}
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claims != null ? claimsResolver.apply(claims) : null;
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser()
			.verifyWith(getSigningKey())
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}
}
