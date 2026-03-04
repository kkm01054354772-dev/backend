package com.example.novels.novel.dto;

import java.time.LocalDate;

import com.example.novels.novel.entity.Genre;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NovelDTO {

    private Long id;

    private String author;

    private String title;

    private boolean available;

    private LocalDate publishedDate;

    private String summary;

    private String description;

    private Long gid; // 장르 아이디

    private String genreName; // 장르명

    private Integer rating; // 평점

    private String email;
}
