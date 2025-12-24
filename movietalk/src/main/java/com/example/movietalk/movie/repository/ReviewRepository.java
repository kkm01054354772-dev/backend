package com.example.movietalk.movie.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.query.Param;

import com.example.movietalk.member.entity.Member;
import com.example.movietalk.movie.entity.Movie;
import com.example.movietalk.movie.entity.Review;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // mno(영화번호) 기준으로 리뷰 조회
    // type = EntityGraphType.FETCH => FetchType.EAGER로 처리하고 나머지는 LAZY로 처리
    // type = EntityGraphType.LOAD => FetchType.EAGER로 처리하고 나머지는 Entity 클래스에 명시된 방법
    @EntityGraph(attributePaths = { "member" }, type = EntityGraphType.FETCH)
    List<Review> findByMovie(Movie movie);

    // 리뷰작성자 기준 리뷰 삭제
    @Modifying
    @Query("delete from Review r where r.member = :member")
    void deleteByMember(@Param("member") Member member);
}
