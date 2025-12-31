package com.example.movietalk.movie.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movietalk.member.entity.Member;
import com.example.movietalk.movie.dto.ReviewDTO;
import com.example.movietalk.movie.entity.Movie;
import com.example.movietalk.movie.entity.Review;
import com.example.movietalk.movie.repository.MovieRepository;
import com.example.movietalk.movie.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Transactional
@RequiredArgsConstructor
@Log4j2
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;

    // 리뷰 생성
    public Long insertRow(ReviewDTO dto) {
        // dto => entity
        Review review = dtoToEntity(dto);

        return reviewRepository.save(review).getRno();
    }

    // 리뷰 삭제
    public void deleteRow(Long rno) {
        reviewRepository.deleteById(rno);
    }

    // 리뷰 업데이트
    public Long updateRow(ReviewDTO dto) {
        // 업데이트 대상 찾기
        Review review = reviewRepository.findById(dto.getRno()).get();
        // 변경사항 적용 (grade, text)
        review.changeGrade(dto.getGrade());
        review.changeText(dto.getText());
        // save() 호출 안하는 이유 : dirty checking

        return review.getRno();
    }

    // rno로 리뷰 하나 가져오기
    @Transactional(readOnly = true)
    public ReviewDTO getRow(Long rno) {
        return entityToDto(reviewRepository.findById(rno).get());
    }

    // 리뷰 목록 가져오기
    @Transactional(readOnly = true)
    public List<ReviewDTO> getList(Long mno) {
        Movie movie = movieRepository.findById(mno).get();

        List<Review> reviews = reviewRepository.findByMovie(movie);

        // Review => ReviewDTO

        // 방법 1 ==================================
        // List<ReviewDTO> list = new ArrayList<>();
        // reviews.forEach(review -> {
        // ReviewDTO dto = entityToDto(review);
        // list.add(dto);
        // });

        // 방법 2
        // ==============================================================================================
        List<ReviewDTO> list = reviews.stream().map(review -> entityToDto(review)).collect(Collectors.toList());

        return list;
    }

    private ReviewDTO entityToDto(Review review) {
        ReviewDTO reviewDTO = ReviewDTO.builder()
                .rno(review.getRno())
                .grade(review.getGrade())
                .text(review.getText())
                .mid(review.getMember().getMid())
                .email(review.getMember().getEmail())
                .nickname(review.getMember().getNickname())
                .mno(review.getMovie().getMno())
                .createDate(review.getCreateDate())
                .updateDate(review.getUpdateDate())
                .build();

        return reviewDTO;
    }

    private Review dtoToEntity(ReviewDTO dto) {
        // ReviewDTO => Review
        Review review = Review.builder()
                .rno(dto.getRno())
                .grade(dto.getGrade())
                .text(dto.getText())
                .movie(Movie.builder().mno(dto.getMno()).build())
                .member(Member.builder().mid(dto.getMid()).build())
                .build();

        return review;
    }
}
