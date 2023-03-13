package com.nrifintech.cms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nrifintech.cms.errorcontroller.ErrorController;
import com.nrifintech.cms.errorhandler.ForbiddenAccessHandler;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.types.Role;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	@Lazy
	JwtAuthenticationFilter jwtAuthenticationFilter;
	@Autowired
	private ErrorController handlerExceptionResolver;

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {

		return super.authenticationManagerBean();
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new ForbiddenAccessHandler();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		
		
		 
		http
		.cors().and().csrf().disable().exceptionHandling().accessDeniedHandler(accessDeniedHandler()).and()
				.authorizeHttpRequests()
				.antMatchers(Route.Authentication.prefix + "**").permitAll()			
				.antMatchers(HttpMethod.OPTIONS).permitAll()

				.antMatchers(HttpMethod.POST, Route.User.prefix + Route.User.addUser,Route.User.prefix + Route.User.removeUser)
				.hasAnyAuthority(Role.Admin.toString()).antMatchers(Route.User.prefix + Route.User.getUsers)
				.hasAnyAuthority(Role.Admin.toString()).antMatchers(Route.User.prefix + Route.User.updateStatus)
				.hasAnyAuthority(Role.Admin.toString())

				.antMatchers(Route.Menu.prefix + Route.Menu.getMenu + "/*")
				.hasAnyAuthority(Role.Admin.toString(), Role.Canteen.toString(), Role.User.toString())
				.antMatchers(HttpMethod.POST, Route.Menu.prefix + Route.Menu.addMenu)
				.hasAnyAuthority(Role.Canteen.toString())
				.antMatchers(HttpMethod.POST, Route.Menu.prefix + Route.Menu.removeFromMenu + "/**")
				.hasAnyAuthority(Role.Canteen.toString())
				.antMatchers(HttpMethod.POST, Route.Menu.prefix + Route.Menu.approveMenu + "/**")
				.hasAnyAuthority(Role.Admin.toString()).antMatchers(Route.Menu.prefix + Route.Menu.getByDate + "/*")
				.hasAnyAuthority(Role.Admin.toString(), Role.Canteen.toString(), Role.User.toString())
				.antMatchers(Route.Menu.prefix + Route.Menu.getMonthMenu)
				.hasAnyAuthority(Role.Admin.toString(), Role.Canteen.toString(), Role.User.toString())

				.antMatchers(HttpMethod.POST, Route.Item.prefix + Route.Item.addItem)
				.hasAnyAuthority(Role.Canteen.toString()).antMatchers(Route.Item.prefix + Route.Item.getItems)
				.hasAnyAuthority(Role.Canteen.toString(), Role.Admin.toString())
				.antMatchers(Route.Item.prefix + Route.Item.getItem + "/*")
				.hasAnyAuthority(Role.Canteen.toString(), Role.Admin.toString())
				.antMatchers(HttpMethod.POST, Route.Item.prefix + Route.Item.addItems)
				.hasAnyAuthority(Role.Canteen.toString())

				.antMatchers(Route.Order.prefix + Route.Order.getOrders + "/*")
				.hasAnyAuthority(Role.Admin.toString(), Role.Canteen.toString())
				.antMatchers(Route.User.prefix + Route.User.getOrders + "/*")
				.hasAnyAuthority(Role.Admin.toString(), Role.Canteen.toString(), Role.User.toString())
				.antMatchers(HttpMethod.POST, Route.Order.prefix + Route.Order.updateStatus + "/**")
				.hasAnyAuthority(Role.Canteen.toString())
				.antMatchers(HttpMethod.POST, Route.Order.prefix + Route.Order.placeOrder + "/**")
				.hasAnyAuthority(Role.User.toString())
				.antMatchers(HttpMethod.POST, Route.Order.prefix + Route.FeedBack.addFeedback + "/*")
				.hasAnyAuthority(Role.User.toString())

				.antMatchers(Route.Cart.prefix + Route.Cart.getCart).hasAnyAuthority(Role.User.toString())
				.antMatchers(HttpMethod.POST, Route.Cart.prefix + Route.Cart.addToCart + "/*")
				.hasAnyAuthority(Role.User.toString())
				.antMatchers(HttpMethod.POST, Route.Cart.prefix + Route.Cart.updateQuantity + "/**")
				.hasAnyAuthority(Role.User.toString())
				.antMatchers(HttpMethod.POST, Route.Cart.prefix + Route.Cart.remove + "/**")
				.hasAnyAuthority(Role.User.toString())
				.antMatchers(HttpMethod.POST, Route.Cart.prefix + Route.Cart.clear + "/*")
				.hasAnyAuthority(Role.User.toString())

				// .antMatchers(HttpMethod.POST,Route.Bill.prefix+"/**").hasAnyAuthority(Role.User.toString())

				// .antMatchers(Route.User.prefix+Route.User.getUsers).hasAnyAuthority(Role.Admin.toString())
				// .antMatchers(Route.User.prefix+Route.User.getUsers).hasAnyAuthority(Role.Admin.toString())
				// .antMatchers(Route.User.prefix+Route.User.getUsers).hasAnyAuthority(Role.Admin.toString())
				// .antMatchers(Route.User.prefix+Route.User.getUsers).hasAnyAuthority(Role.Admin.toString())
				// .antMatchers(Route.User.prefix+Route.User.getUsers).hasAnyAuthority(Role.Admin.toString())
				// .antMatchers(Route.User.prefix+Route.User.getUsers).hasAnyAuthority(Role.Admin.toString())

				.anyRequest().authenticated().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		// System.out.println("Fetching Password encoder");
		return NoOpPasswordEncoder.getInstance();
	}
}
