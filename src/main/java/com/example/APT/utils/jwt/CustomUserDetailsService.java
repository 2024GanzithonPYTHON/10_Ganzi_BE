package com.example.APT.utils.jwt;

import com.example.APT.entity.Member;
import com.example.APT.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
// import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository userRepository;

  // 충돌로 인한 주석 처리
//     private final DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration;

//     @Override
//     @Transactional
//     public UserDetails loadUserByUsername(String  LoginId) throws UsernameNotFoundException {
//         return userRepository.findByLoginId(LoginId)
//                 .map(this::createUserDetails)
//                 .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
//     }

//     // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
//     private UserDetails createUserDetails(Member member) {
//         GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthorities().toString());

//         return new User(
//                 String.valueOf(member.getLoginId()),
//                 member.getPassword(),
//                 Collections.singleton(grantedAuthority)
//         );
//     }

// }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        System.out.println("Trying to load user by loginId: " + loginId);

        return userRepository.findByLoginId(loginId)
                .map(member -> {
                    System.out.println("User found: " + member.getLoginId());
                    return createUserDetails(member);
                })
                .orElseThrow(() -> {
                    System.out.println("User not found with loginId: " + loginId);
                    return new CustomException(ErrorCode.MEMBER_NOT_FOUND);
                });
    }

    private UserDetails createUserDetails(Member member) {
        Collection<? extends GrantedAuthority> authorities = member.getAuthorities();
        System.out.println("User authorities: " + authorities);

        return new User(
                member.getLoginId(),
                member.getPassword(),
                authorities
        );
    }
}

