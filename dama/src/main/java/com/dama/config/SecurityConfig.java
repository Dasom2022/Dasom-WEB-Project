package com.dama.config;

/*
import com.dama.service.MemberService;
*/
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /*private final MemberService memberService;*/
    //커밋용주석
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

       /* http.logout()
                .logoutUrl("/doLogout");*/

        http.authorizeRequests()
                .antMatchers("/")
                .permitAll()
                .anyRequest()
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/member/signin")
                .loginProcessingUrl("/member/signin")
                .defaultSuccessUrl("/")
                .usernameParameter("username")
                .passwordParameter("password");
    }

    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("$2a$10$GJMsKvIHN4t8Qifnchc5e.EFCUq7m49uie3l5ZdSZwnJR6SiuC0bK")
                .roles("USER","MEMBER","ADMIN");

        auth.userDetailsService(memberService).passwordEncoder(encoder());

    }*/
}

