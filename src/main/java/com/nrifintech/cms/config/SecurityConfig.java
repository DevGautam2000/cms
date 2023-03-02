package com.nrifintech.cms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.types.Role;


@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    
	@Autowired  @Lazy
	JwtAuthenticationFilter jwtAuthenticationFilter;
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        
        return super.authenticationManagerBean();
    }
    
    @Override
	protected void configure(HttpSecurity http) throws Exception {

		
		http
            .csrf().disable()
            .cors().disable()
			.authorizeHttpRequests()
			.antMatchers(Route.Authentication.prefix+"**").permitAll()
			.antMatchers(HttpMethod.OPTIONS).permitAll()
			
			.antMatchers(HttpMethod.POST,Route.User.prefix+Route.User.addUser,Route.User.prefix+Route.User.removeUser).hasAnyAuthority(Role.Admin.toString())
			.antMatchers(Route.User.prefix+Route.User.getUser).hasAnyAuthority(Role.Admin.toString(),Role.Canteen.toString())


			.antMatchers(HttpMethod.POST,"/items").hasAnyRole("ADMIN")
			.antMatchers("/items").hasAnyRole("ADMIN","USER")
			.anyRequest().authenticated()
		     .and()
             .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// http
        //     .csrf().disable()
        //     .cors().disable()
		// 	.authorizeHttpRequests()
		// 	.antMatchers("/hi","/generate-token","/user/").permitAll()
		// 	.antMatchers(HttpMethod.OPTIONS).permitAll()
		// 	.antMatchers("/admin","/test")/* .hasAnyRole("ADMIN",Role.Admin.toString())   /*/.hasAnyAuthority(Role.Admin.toString())// 
		// 	.antMatchers(HttpMethod.POST,"/items").hasAnyRole("ADMIN")
		// 	.antMatchers("/items").hasAnyRole("ADMIN","USER")
		// 	.anyRequest().authenticated()
		//      .and()
        //      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//			.antMatchers("/auth/**").permitAll()
//			.antMatchers("/admin").hasRole("ADMIN")
//			.antMatchers("/**").hasAnyRole("USER","ADMIN")
//			.anyRequest().authenticated()
//			.and()
//			.formLogin()
			;
		http.addFilterBefore(jwtAuthenticationFilter, 
				UsernamePasswordAuthenticationFilter.class);
	}

    @Bean
	public PasswordEncoder passwordEncoder() {
		System.out.println("Fetching Password encoder");
	    return NoOpPasswordEncoder.getInstance();
	}
}
