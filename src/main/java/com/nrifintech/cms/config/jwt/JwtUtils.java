package com.nrifintech.cms.config.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * This class is responsible for generating and validating JWT tokens.
 */
@Service
public class JwtUtils {

	@Value("${app.secret}")
    private String SECRET_KEY;

	   /**
		* It extracts the username from the token
		* 
		* @param token The JWT whose subject claim will be returned.
		* @return The subject of the token.
		*/
	    public String extractUsername(String token) {
	        return extractClaim(token, Claims::getSubject);
	    }

	    /**
		 * > Extracts the expiration date from the token
		 * 
		 * @param token The JWT whose claims you want to extract.
		 * @return The expiration date of the token.
		 */
		public Date extractExpiration(String token) {
	        return extractClaim(token, Claims::getExpiration);
	    }

	    /**
		 * It takes a token and a function as input and returns the result of applying the function to the
		 * claims extracted from the token
		 * 
		 * @param token The JWT token
		 * @param claimsResolver A function that takes in a Claims object and returns a value of type T.
		 * @return A Claims object
		 */
		public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	        final Claims claims = extractAllClaims(token);
	        return claimsResolver.apply(claims);
	    }
	    /**
		 * It takes a token and returns the claims
		 * 
		 * @param token The token that needs to be validated.
		 * @return A Claims object.
		 */
		private Claims extractAllClaims(String token) {
	        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	    }

	   /**
		* > If the expiration date of the token is before the current date, then the token is expired
		* 
		* @param token The JWT token that needs to be validated.
		* @return A boolean value.
		*/
	    public Boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	    }

	    /**
		 * It creates a JWT token with the user's username as the subject, and the user's authorities as
		 * the roles
		 * 
		 * @param userDetails The user details object that contains the user information.
		 * @return A token
		 */
		public String generateToken(UserDetails userDetails) {
	        Map<String, Object> claims = new HashMap<>();
	        return createToken(claims, userDetails.getUsername(),10L*24*60);
	    }

		
	    /**
		 * It creates a token with a 10 minute expiration time.
		 * 
		 * @param userDetails The user details object that contains the username.
		 * @return A JWT token
		 */
		public String generateResetToken(UserDetails userDetails) {
	        Map<String, Object> claims = new HashMap<>();
	        return createToken(claims, userDetails.getUsername(),10L);
	    }

		/**
		 * It creates a token for the user with the username and the expiration time of 24 hours.
		 * 
		 * @param userDetails The user details object that contains the username and password.
		 * @return A token
		 */
		public String generateNewPasswordToken(UserDetails userDetails) {
	        Map<String, Object> claims = new HashMap<>();
	        return createToken(claims, userDetails.getUsername(),60L*24);
	    }

	    /**
		 * It creates a token with the claims, subject, and expiration time.
		 * 
		 * @param claims A map of claims that will be added to the JWT.
		 * @param subject The subject of the token.
		 * @param min The time in minutes for which the token is valid.
		 * @return A JWT token
		 */
		private String createToken(Map<String, Object> claims, String subject,long min) {

	        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
	                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * min))
	                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	         
	    }

	    /**
		 * If the username in the token matches the username in the userDetails object, and the token is
		 * not expired, then the token is valid
		 * 
		 * @param token The JWT token to validate
		 * @param userDetails The user details object that contains the user's credentials.
		 * @return A boolean value.
		 */
		public boolean validateToken(String token, UserDetails userDetails) {
	        final String username = extractUsername(token);
			
	        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	    }
}
