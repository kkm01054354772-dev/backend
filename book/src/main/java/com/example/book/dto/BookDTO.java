package com.example.book.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookDTO {

    private Long id;

    @NotBlank(message = "필수 입력 사항입니다.")
    private String isbn;

    @NotBlank(message = "필수 입력 사항입니다.")
    private String title;

    @Max(value = 9999999, message = "유효한 값인지 확인해주세요.")
    @Min(value = 0, message = "유효한 값인지 확인해주세요.")
    @NotNull(message = "필수 입력 사항입니다.")
    private Integer price;

    @NotBlank(message = "필수 입력 사항입니다.")
    private String author;

    private String description;

}
