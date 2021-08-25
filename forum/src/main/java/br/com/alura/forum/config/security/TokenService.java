package br.com.alura.forum.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {
	
	@Value("${forum.jwt.expiration}")
	private String expiration;
	
	@Value("${forum.jwt.secret}")
	private String secret;

	public String generateToken(Authentication authentication) {
		Usuario loggedUser = (Usuario) authentication.getPrincipal();
		Date generationDate = new Date();
		return Jwts.builder()
				.setIssuer("Forum API Application")
				.setSubject(loggedUser.getId().toString())
				.setIssuedAt(new Date())
				.setExpiration(new Date(generationDate.getTime() + Long.parseLong(expiration)))
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	
	}

	public boolean isValidToken(String token) {
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {		
			return false;
		}
	}

	public Long getUserID(String token) {
		Claims body = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		String userID = body.getSubject();
		return Long.parseLong(userID);
	}

}
