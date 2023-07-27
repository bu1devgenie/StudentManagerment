package com.app.studentManagerment.configuration;

import com.app.studentManagerment.configuration.jwt.JwtTokenFilter;
import com.app.studentManagerment.dao.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
		prePostEnabled = true,
		jsr250Enabled = true,
		proxyTargetClass = true
)
public class SecurityConfig {
	private final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);
	private final AccountRepository accountRepository;
	private final JwtTokenFilter jwtTokenFilter;


	public SecurityConfig(AccountRepository accountRepository, JwtTokenFilter jwtTokenFilter) {
		this.accountRepository = accountRepository;
		this.jwtTokenFilter = jwtTokenFilter;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	UserDetailsService userDetailsService() {
		return new CustomUserDetailsService(accountRepository);
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("http://localhost:3000");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.cors(customizer -> {
		});

		httpSecurity.csrf(AbstractHttpConfigurer::disable);
		httpSecurity
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers("/login").permitAll()
						.requestMatchers("/checkAccessToken").permitAll()
						//teacher
						.requestMatchers("/teacher/getAllTeacher").hasAnyAuthority("Hr", "Admin", "Principal", "Teacher")
						.requestMatchers("/teacher/searchTeacher").hasAnyAuthority("Hr", "Admin", "Principal", "Teacher")
						.requestMatchers("/teacher/deleteTeacher").hasAnyAuthority("Admin", "Principal")
						.requestMatchers("/teacher/getMSGV").hasAnyAuthority("Admin", "Principal")
						.requestMatchers("/teacher/addTeacher").hasAnyAuthority("Admin", "Principal")

						//student
						.requestMatchers("/student/findAll").hasAnyAuthority("Hr", "Admin", "Principal", "Teacher")
						.requestMatchers("/schedule/autoGenerateSchedule").hasAnyAuthority("Principal")
						// course
						.requestMatchers("/course/getAllCourseName").hasAnyAuthority("Hr", "Admin", "Principal", "Teacher")
						.requestMatchers("/course/searchCourse").hasAnyAuthority("Hr", "Admin", "Principal", "Teacher", "Student")
						// account
						.requestMatchers("/account/searchEmailNoConnected").hasAnyAuthority("Hr", "Admin", "Principal")

						.anyRequest().authenticated()
				);
		httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
		return httpSecurity.build();
	}
}
