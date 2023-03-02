package com.nrifintech.cms.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nrifintech.cms.config.jwt.JwtUtils;
import com.nrifintech.cms.services.UserService;

import io.jsonwebtoken.ExpiredJwtException;

//conflict between 2 types of 
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    @Autowired 
    private MyUserDetailsService userDetailsServiceImple;
    // private UserService userDetailsServiceImple;
    @Autowired
    private JwtUtils jutUtil;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String requestTokenHeader = request.getHeader("Authorization");
        System.out.println(requestTokenHeader);
        String username=null;
        String jwtToken=null;
        
        if(requestTokenHeader!=null && requestTokenHeader.startsWith("Bearer ")){
            try
            {
                jwtToken=requestTokenHeader.substring(7);
                username=this.jutUtil.extractUsername(jwtToken);
            }catch(ExpiredJwtException e){
                e.printStackTrace();
                System.out.println("jwt token has expired");
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("error");
            }
        }else{
            System.out.println("Invalid Token , not start with bearer string");
        }

        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            final UserDetails userDetails= this.userDetailsServiceImple.loadUserByUsername(username);
            if(this.jutUtil.validateToken(jwtToken, userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        else
        {
            System.out.println("Token is not valid!!");
        }

        filterChain.doFilter(request, response);
		
	}
}
