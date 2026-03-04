package com.example.mart.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.mart.entity.constant.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@ToString(exclude = { "member", "orderItems", "delivery" })
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
    @OneToMany(mappedBy = "order", cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    // 조인컬럼명 생성시 테이블명_pk명(기본값) 이므로 이름을 따로 지정(기본값: delivery_delivery_id)
    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinColumn(name = "delivery_id", unique = true)
    private Delivery delivery;

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
