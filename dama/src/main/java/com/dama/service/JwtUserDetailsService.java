package com.dama.service;

import com.dama.model.entity.Member;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("user_id".equals(username)) {
            /*return new Member("user_id", "$2a$10$m/enYHaLsCwH2dKMUAtQp.ksGOA6lq7Fd2pnMb4L.yT4GyeAPRPyS",
                    new ArrayList<>());*/
            return null;
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
