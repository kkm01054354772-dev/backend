package com.example.mart.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.mart.entity.constant.OrderStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "member", "orderItems" })
@Entity
@Table(name = "orders") // order by 키워드 때문에 order 테이블은 만들 수 없음
public class Order extends BaseEntity {
    // id, OrderStatus
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "order") // 기본적으로 fetch = Fetchtype.LAZY
    private List<OrderItem> orderItems = new ArrayList<>();

}
