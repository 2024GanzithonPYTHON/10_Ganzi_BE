package com.example.APT.dto;

import com.example.APT.entity.Member;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberSignupRequestDto {

    @NotNull
    private String loginId;

    @NotNull
    @Pattern(
            regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다"
    )
    private String password;

    private String address;

    private int age;

    private String name;

    private String childName;

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .loginId(this.loginId)
                .password(passwordEncoder.encode(this.password)) // 비밀번호 암호화
                .address(this.address)
                .age(this.age)
                .name(this.name)
                .build();
    }
}
