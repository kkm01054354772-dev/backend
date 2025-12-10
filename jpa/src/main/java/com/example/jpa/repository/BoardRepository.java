package com.example.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.jpa.entity.Board;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // title = ?
    List<Board> findByTitle(String title);

    // content = ?
    List<Board> findByContent(String content);

    // title like %?
    List<Board> findByTitleEndingWith(String title);

    // title like %?% and id > 0 order by id desc
    List<Board> findByTitleContainingAndIdGreaterThanOrderByIdDesc(String title, Long id);

    // writer like %?%
    List<Board> findByWriterContaining(String writer);

    // title like %?% or content like %?%
    List<Board> findByTitleContainingOrContentContaining(String writer, String content);

    // @Query 사용 : entity 기준(대문자로 시작)
    @Query("select b from Board b where b.title= ?1 ")
    // @Query("select b from Board b where b.title= :title ")
    List<Board> findByTitle2(String title); // @Query 사용시 이름은 중요치 않음

    @Query("select b.title, b.writer from Board b where b.title like %:title% ") // 일부만 찾고 싶을 때
    List<Object[]> findByTitle3(@Param("title") String title);

    @Query("select b from Board b where b.content= ?1")
    List<Board> findByContent2(String content);

    @Query("select b from Board b where b.title like %?1")
    List<Board> findByTitleEndingWith2(String title);

    @Query("select b from Board b where b.title like %?1% and b.id > ?2 order by b.id desc")
    List<Board> findByTitleAndId(String title, Long id);

    @Query("select b from Board b where b.writer like %?1%")
    List<Board> findByWriterContaining2(String writer);

    @Query("select b from Board b where b.writer like %?1% or b.content like %?2%")
    List<Board> findByTitleOrContent(String writer, String content);

    // @Query + nativeQuery(sql 구문 형식)
    // @Query(value = "select b.* from boardtbl b where b.title like
    // concat('%',:title,'%') and b.id > :id order by b.id desc", nativeQuery =
    // true)
    @NativeQuery(value = "select b.* from boardtbl b where b.title like concat('%',:title,'%') and b.id > :id order by b.id desc")
    List<Board> findByTitleAndId2(@Param("title") String title, @Param("id") Long id);
}
