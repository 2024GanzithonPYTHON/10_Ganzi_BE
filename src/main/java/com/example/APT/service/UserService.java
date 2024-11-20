package com.example.APT.service;

import com.example.APT.controller.dto.MemberLoginRequestDto;
import com.example.APT.controller.dto.MemberLoginResponseDto;
import com.example.APT.controller.dto.MemberSignupRequestDto;
import com.example.APT.controller.dto.MemberSignupResponseDto;
import com.example.APT.entity.Member;
import com.example.APT.repository.MemberRepository;
import com.example.APT.utils.jwt.TokenDTO;
import com.example.APT.utils.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final MemberRepository userRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;

//    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public MemberSignupResponseDto create(MemberSignupRequestDto request) {
        if (userRepository.existsByLoginId(request.getLoginId())) {
            throw new DuplicateKeyException("User already exists");
        }

        // 비밀번호를 BCrypt로 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member newUser = Member.newInstance(
                request.getLoginId(),
                encodedPassword, // 암호화된 비밀번호 저장
                request.getAddress(),
                request.getAge(),
                request.getName()
        );

        userRepository.save(newUser);
        return MemberSignupResponseDto.of(newUser.getId(), newUser.getLoginId());
    }


    public MemberLoginResponseDto login(MemberLoginRequestDto request) {
        try {
            // 디버깅 로그 추가
            System.out.println("Attempting to authenticate user with loginId: " + request.getLoginId());

            // 사용자 인증
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getLoginId(), request.getPassword())
            );

            // 인증 성공 시 로그
            System.out.println("Authentication successful for user: " + authentication.getName());

            // 토큰 생성
            TokenDTO tokenDTO = tokenProvider.generateTokenDTO(authentication);
            return new MemberLoginResponseDto(tokenDTO.getAccessToken(), tokenDTO.getRefreshToken());

        } catch (Exception ex) {
            // 인증 실패 시 로그
            System.out.println("Authentication failed for user: " + request.getLoginId());
            ex.printStackTrace(); // 예외 로그 출력
            throw ex; // 예외를 다시 던져서 처리
        }
    }

    private String generateAccessToken(Integer userId) {
        // 액세스 토큰 생성 로직 구현
        return "access_token_for_user_" + userId; // 예시
    }

    private String generateRefreshToken(Integer userId) {
        // 리프레시 토큰 생성 로직 구현
        return "refresh_token_for_user_" + userId; // 예시
    }


    private boolean authenticateUser(Integer userId, String loginId) {
        // 실제 인증 로직 구현 (예: 데이터베이스 조회)
        return true; // 인증 성공 여부 반환
    }
}
