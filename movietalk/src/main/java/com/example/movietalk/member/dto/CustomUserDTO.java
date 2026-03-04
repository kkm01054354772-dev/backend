package com.example.movietalk.member.dto;

import com.example.movietalk.member.entity.constant.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CustomUserDTO {

    private Long mid;

    // <div class="text-danger" th:if="${#fields.hasErrors('isbn')}"
    // th:errors="*{isbn}"></div>
    @Email(message = "이메일 형식인지 확인해 주세요.")
    @NotBlank(message = "필수 입력 요소입니다.")
    private String email;

    @NotBlank(message = "필수 입력 요소입니다.")
    private String password;

    @NotBlank(message = "필수 입력 요소입니다.")
    private String nickname;

    private Role role;
}
