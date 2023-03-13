package com.nrifintech.cms.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.nrifintech.cms.config.jwt.JwtUtils;
import com.nrifintech.cms.errorhandler.UserIsDisabledException;
import com.nrifintech.cms.repositories.TokenBlacklistRepo;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired 
    private MyUserDetailsService userDetailsServiceImple;
    @Autowired
    private JwtUtils jutUtil;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
    @Autowired
    private TokenBlacklistRepo blacklistRepo;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException, JwtException, UserIsDisabledException {
		final String requestTokenHeader = request.getHeader("Authorization");
		System.out.println(requestTokenHeader);
		String username = null;
		String jwtToken = null;

		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			try {
				jwtToken = requestTokenHeader.substring(7);
				username = this.jutUtil.extractUsername(jwtToken);
			} catch (ExpiredJwtException e) {
				// e.printStackTrace();
				resolver.resolveException(request, response, null, e);
				// System.out.println("Jwt token has expired");
			} catch (Exception e) {
				// e.printStackTrace();
				// System.out.println("error");
				resolver.resolveException(request, response, null, e);
			}
		} else {
			System.out.println("Invalid Token , not start with Bearer string");
//			resolver.resolveException(request, response, null, new AccessDeniedException("Access Denied."));
		}
		
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null && blacklistRepo.findById(jwtToken).orElse(null)==null){
            final UserDetails userDetails= this.userDetailsServiceImple.loadUserByUsername(username);
            
            if(!userDetails.isEnabled())
                resolver.resolveException(request, response, null, new UserIsDisabledException("InActive User"));

			else if (this.jutUtil.validateToken(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		} else {
			System.out.println("Token is not valid!!");
			
		}

		try {
			filterChain.doFilter(request, response);
		} catch (AccessDeniedException e) {

			resolver.resolveException(request, response, null, e);
		} catch (Exception e) {
			resolver.resolveException(request, response, null, e);
		}
	}
}
