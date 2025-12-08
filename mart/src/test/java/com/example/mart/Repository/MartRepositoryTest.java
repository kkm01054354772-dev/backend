package com.example.mart.Repository;

import static org.mockito.ArgumentMatchers.isNull;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.mart.entity.Item;
import com.example.mart.entity.Member;
import com.example.mart.entity.Order;
import com.example.mart.entity.OrderItem;
import com.example.mart.entity.constant.OrderStatus;
import com.example.mart.repository.ItemRepository;
import com.example.mart.repository.MemberRepository;
import com.example.mart.repository.OrderItemRepository;
import com.example.mart.repository.OrderRepository;

@SpringBootTest
public class MartRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void insertMemberTest() {
        // 5명의 회원 등록
        IntStream.rangeClosed(1, 5).forEach(i -> {
            Member member = Member.builder()
                    .name("user" + i)
                    .city("서울")
                    .street("724-1" + i)
                    .zipcode("1650" + i)
                    .build();

            memberRepository.save(member);
        });

    }

    @Test
    public void insertItemTest() {
        // 5개의 물품 입력
        IntStream.rangeClosed(1, 5).forEach(i -> {
            Item item = Item.builder()
                    .name("item" + i)
                    .price(250000)
                    .quantity(i * 5)
                    .build();
            itemRepository.save(item);
        });
    }

    @Test
    public void orderTest() {
        // 주문 1번 상품을 2번 고객이 주문
        Item item = itemRepository.findById(1L).get();
        Member member = memberRepository.findById(2L).get();

        Order order = Order.builder()
                .member(member)
                .orderStatus(OrderStatus.ORDER)
                .build();
        orderRepository.save(order);

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .order(order)
                .orderPrice(200000)
                .count(1)
                .build();
        orderItemRepository.save(orderItem);
    }

    @Transactional(readOnly = true)
    @Test
    public void orderReadTest() {
        // 2번 회원의 주문 내역 조회
        Member member = Member.builder().id(2L).build();
        Order order = orderRepository.findByMember(member).get();
        System.out.println(order);

        order.getOrderItems().forEach(i -> {
            // 주문 상품 정보 OrderItem(id=1, orderPrice=200000, count=1)
            System.out.println(i);
            // 주문 상품 상세 정보 조회 Item(id=1, name=item1, price=250000, quantity=5)
            System.out.println(i.getItem());

        });

        // 고객 상세 정보 Member(id=2, name=user2, city=서울, street=724-12, zipcode=16502)
        System.out.println(order.getMember());
    }
}
