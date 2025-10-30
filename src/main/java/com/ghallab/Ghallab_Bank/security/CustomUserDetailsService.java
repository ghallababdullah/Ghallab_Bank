package com.ghallab.Ghallab_Bank.security;

import com.ghallab.Ghallab_Bank.auth_users.entity.User;
import com.ghallab.Ghallab_Bank.auth_users.repo.UserRepo;
import com.ghallab.Ghallab_Bank.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepo userRepo ;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =userRepo.findByEmail(username)
                .orElseThrow(()-> new NotFoundException("Email Not Found"));

        return AuthUser.builder()
                .user(user)
                .build() ;
    }
}
