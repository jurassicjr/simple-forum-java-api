package br.com.alura.forum.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repositories.UsuarioRepository;

public class TokenAuthenticationMiddleware extends OncePerRequestFilter {
	
	private TokenService tokenService;
	private UsuarioRepository repository;
	
	public TokenAuthenticationMiddleware(TokenService tokenService, UsuarioRepository repository) {
		this.tokenService = tokenService;
		this.repository = repository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = getToken(request);
		boolean isValid = tokenService.isValidToken(token);
		if(isValid) {
			authenticateUser(token);
		}
		
		
		filterChain.doFilter(request, response);
	}

	private void authenticateUser(String token) {
		Long userID = tokenService.getUserID(token);
		Usuario user = repository.findById(userID).orElseThrow();
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private String getToken(HttpServletRequest request) {
		 String token = request.getHeader("Authorization");
		 if(token == null || token.isEmpty() || !token.startsWith("Bearer ")) return null;
		 
		 return token.substring(7, token.length());
		
	}

}
