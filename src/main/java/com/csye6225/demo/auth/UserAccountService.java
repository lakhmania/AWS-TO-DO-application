package com.csye6225.demo.auth;

import com.csye6225.demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Arrays;

@Service
@Transactional
public class UserAccountService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        com.csye6225.demo.pojo.User user = userRepo.findByUserName(username);

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("User");
        UserDetails springUserRecord = new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), Arrays.asList(grantedAuthority));

        return springUserRecord;
    }
}
