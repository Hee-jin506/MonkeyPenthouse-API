package com.monkeypenthouse.core.security;

import com.monkeypenthouse.core.dao.User;
import com.monkeypenthouse.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // DB에서 이메일로 찾은 User 객체를 UserDetails 객체로 만들어 리턴
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = userRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일을 가진 회원이 존재하지 않습니다."));
        return user;
    }

    // DB에 User 값이 존재한다면 UserDetails 객체로 만들어 리턴
    private UserDetails createUserDetails(User user) {
        return new PrincipalDetails(user);
    }

}
