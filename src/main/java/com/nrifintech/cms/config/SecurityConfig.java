package com.nrifintech.cms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.nrifintech.cms.config.guard.AccessDecisionManagerAuthorizationManagerAdapter;
import com.nrifintech.cms.errorcontroller.ErrorController;
import com.nrifintech.cms.routes.Route;
import com.nrifintech.cms.types.Role;


@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    
	@Autowired  @Lazy
	JwtAuthenticationFilter jwtAuthenticationFilter;
	@Autowired
	private ErrorController handlerExceptionResolver;
	@Autowired
	private LogoutHandler logoutHandler;

	@Autowired
	private AccessDecisionManagerAuthorizationManagerAdapter aManagerAdapter;
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
			.antMatchers(HttpMethod.POST,Route.Authentication.prefix+"set-new-password*").hasAnyAuthority(Role.Admin.toString())
			.antMatchers(Route.Authentication.prefix+"**").permitAll()
			.antMatchers(HttpMethod.OPTIONS).permitAll()
			
			.antMatchers(HttpMethod.POST,Route.User.prefix+Route.User.addUser,Route.User.prefix+Route.User.removeUser).hasAnyAuthority(Role.Admin.toString())
			.antMatchers(Route.User.prefix+Route.User.getUsers).hasAnyAuthority(Role.Admin.toString())
			.antMatchers(Route.User.prefix+Route.User.updateStatus).hasAnyAuthority(Role.Admin.toString())
			
			.antMatchers(Route.Menu.prefix+Route.Menu.getMenu+"/{id}").hasAnyAuthority(Role.Admin.toString(),Role.Canteen.toString(),Role.User.toString())
			.antMatchers(HttpMethod.POST,Route.Menu.prefix+Route.Menu.addMenu).hasAnyAuthority(Role.Canteen.toString())
			.antMatchers(HttpMethod.POST,Route.Menu.prefix+Route.Menu.removeFromMenu+"/**").hasAnyAuthority(Role.Canteen.toString())
			.antMatchers(HttpMethod.POST,Route.Menu.prefix+Route.Menu.approveMenu+"/**").hasAnyAuthority(Role.Admin.toString())
			.antMatchers(Route.Menu.prefix+Route.Menu.getByDate+"/*").hasAnyAuthority(Role.Admin.toString(),Role.Canteen.toString(),Role.User.toString())
			.antMatchers(Route.Menu.prefix+Route.Menu.getMonthMenu).hasAnyAuthority(Role.Admin.toString(),Role.Canteen.toString(),Role.User.toString())

			.antMatchers(HttpMethod.POST,Route.Item.prefix+Route.Item.addItem).hasAnyAuthority(Role.Canteen.toString())
			.antMatchers(Route.Item.prefix+Route.Item.getItems).hasAnyAuthority(Role.Canteen.toString(),Role.Admin.toString())
			.antMatchers(Route.Item.prefix+Route.Item.getItem+"/*").hasAnyAuthority(Role.Canteen.toString(),Role.Admin.toString())
			.antMatchers(HttpMethod.POST,Route.Item.prefix+Route.Item.addItems).hasAnyAuthority(Role.Canteen.toString())

			.antMatchers(Route.Order.prefix+Route.Order.getOrders+"/*").hasAnyAuthority(Role.Admin.toString(),Role.Canteen.toString())
			.antMatchers(Route.User.prefix+Route.User.getOrders+"/{id}").access((authentication, object) -> aManagerAdapter.preCheckUserWithId(authentication, object,Role.Admin.toString(),Role.Canteen.toString()))
			//.hasAnyAuthority(Role.Admin.toString(),Role.Canteen.toString(),Role.User.toString())
			.antMatchers(HttpMethod.POST, Route.Order.prefix+Route.Order.updateStatus+"/**").hasAnyAuthority(Role.Canteen.toString())
			.antMatchers(HttpMethod.POST,Route.Order.prefix+Route.Order.placeOrder+"/{id}/{mealId}").access((authentication, object) -> aManagerAdapter.preCheckUserWithId(authentication, object))
						
			
			.antMatchers(Route.Cart.prefix + Route.Cart.getCart+"/{cartId}").access((authentication, object) -> aManagerAdapter.preCheckUserCartId(authentication, object))
                .antMatchers(HttpMethod.POST, Route.Cart.prefix + Route.Cart.addToCart + "/{id}").access((authentication, object) -> aManagerAdapter.preCheckUserWithId(authentication, object))
                // .hasAnyAuthority(Role.User.toString())
                .antMatchers(HttpMethod.POST, Route.Cart.prefix + Route.Cart.updateQuantity + "/inc/{itemId}/{factor}").access((authentication, object) -> aManagerAdapter.preCheckHasUserCartitem(authentication, object)) 
                .antMatchers(HttpMethod.POST, Route.Cart.prefix + Route.Cart.updateQuantity + "/dec/{itemId}/{factor}").access((authentication, object) -> aManagerAdapter.preCheckHasUserCartitem(authentication, object))  
               // .hasAnyAuthority(Role.User.toString())
                .antMatchers(HttpMethod.POST, Route.Cart.prefix + Route.Cart.remove + "/**")
                .hasAnyAuthority(Role.User.toString())
                .antMatchers(HttpMethod.POST, Route.Cart.prefix + Route.Cart.clear + "/*")
                .hasAnyAuthority(Role.User.toString())


			.antMatchers(HttpMethod.POST,Route.FeedBack.prefix+Route.FeedBack.addFeedback+"/{orderId}").access((authentication, object) -> aManagerAdapter.preCheckUserOrderId(authentication, object)) 
			
			// .antMatchers(HttpMethod.POST,Route.Bill.prefix+"/**").hasAnyAuthority(Role.User.toString())

			// .antMatchers(Route.User.prefix+Route.User.getUsers).hasAnyAuthority(Role.Admin.toString())
			// .antMatchers(Route.User.prefix+Route.User.getUsers).hasAnyAuthority(Role.Admin.toString())
			// .antMatchers(Route.User.prefix+Route.User.getUsers).hasAnyAuthority(Role.Admin.toString())
			// .antMatchers(Route.User.prefix+Route.User.getUsers).hasAnyAuthority(Role.Admin.toString())
			// .antMatchers(Route.User.prefix+Route.User.getUsers).hasAnyAuthority(Role.Admin.toString())
			// .antMatchers(Route.User.prefix+Route.User.getUsers).hasAnyAuthority(Role.Admin.toString())
  			


			.anyRequest().authenticated()
		     .and()
             .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http
		.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
		.logout()
        .logoutUrl("/auth/logout")
        .addLogoutHandler(logoutHandler)
        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());
	}

    @Bean
	public PasswordEncoder passwordEncoder() {
		// System.out.println("Fetching Password encoder");
	    // return NoOpPasswordEncoder.getInstance();
		return new BCryptPasswordEncoder();
	}
}
