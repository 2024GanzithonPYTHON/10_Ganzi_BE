package com.example.APT.service;

import com.example.APT.dto.MemberLoginRequestDto;
import com.example.APT.dto.MemberLoginResponseDto;
import com.example.APT.dto.MemberSignupRequestDto;
import com.example.APT.dto.MemberSignupResponseDto;

import com.example.APT.entity.Category;
import com.example.APT.entity.Member;
import com.example.APT.entity.UserCategory;
import com.example.APT.repository.CategoryRepository;
import com.example.APT.repository.MemberRepository;
import com.example.APT.repository.UserCategoryRepository;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final MemberRepository userRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserCategoryRepository userCategoryRepository;

    private final BCryptPasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;

    @Transactional
    public MemberSignupResponseDto create(MemberSignupRequestDto request) {
        // 1. 중복 사용자 체크
        if (userRepository.existsByLoginId(request.getLoginId())) {
            throw new DuplicateKeyException("User already exists with loginId: " + request.getLoginId());
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 3. 사용자 엔티티 생성
        Member newUser = Member.newInstance(
                request.getLoginId(),
                encodedPassword, // 암호화된 비밀번호
                request.getAddress(),
                request.getAge(),
                request.getName(),
                request.getChildName()
        );

        // 4. 사용자 저장
        userRepository.save(newUser);

        // 5. 카테고리 처리
        List<Category> categories = categoryRepository.findByCategoryNameIn(request.getCategoryNames());
        categories.forEach(category -> {
            UserCategory userCategory = new UserCategory();
            userCategory.setUser(newUser);
            userCategory.setCategory(category);
            userCategoryRepository.save(userCategory);
        });

        // 6. 응답 생성
        return MemberSignupResponseDto.of(
                newUser.getId(),
                newUser.getLoginId(),
                request.getCategoryNames() // 사용자가 선택한 카테고리 포함
        );
    }


    public MemberLoginResponseDto login(MemberLoginRequestDto request) {
        // 사용자 인증 로직 구현
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLoginId(), request.getPassword())
        );

        // 토큰 생성
        TokenDTO tokenDTO = tokenProvider.generateTokenDTO(authentication);

        return new MemberLoginResponseDto(tokenDTO.getAccessToken(), tokenDTO.getRefreshToken());
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
