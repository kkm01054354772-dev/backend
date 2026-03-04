package com.example.mart.Repository;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.example.mart.entity.Category;
import com.example.mart.entity.CategoryItem;
import com.example.mart.entity.Delivery;
import com.example.mart.entity.Item;
import com.example.mart.entity.Member;
import com.example.mart.entity.Order;
import com.example.mart.entity.OrderItem;
import com.example.mart.entity.constant.DeliveryStatus;
import com.example.mart.entity.constant.OrderStatus;
import com.example.mart.repository.CategoryItemRepository;
import com.example.mart.repository.CategoryRepository;
import com.example.mart.repository.DeliveryRepository;
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
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryItemRepository categoryItemRepository;

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
        // 1번 상품을 2번 고객이 주문
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

    @Commit
    @Transactional
    @Test
    public void orderCascadeTest() {
        // 2번 상품을 3번 고객이 주문
        Item item = itemRepository.findById(2L).get();
        Member member = memberRepository.findById(3L).get();

        Order order = Order.builder()
                .member(member)
                .orderStatus(OrderStatus.ORDER)
                .build();

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                // .order(order)
                .orderPrice(230000)
                .count(1)
                .build();

        // order.getOrderItems().add(orderItem);
        order.addOrderItem(orderItem);
        orderRepository.save(order);

        // orderItemRepository.save(orderItem);
    }

    @Commit
    @Transactional
    @Test
    public void testUpdate() {
        // 3번 고객의 city 변경
        Member member = memberRepository.findById(3L).get();
        member.changeCity("부산"); // managed entity : 변경사항 감지 기능(dirty checking)
        // memberRepository.save(member) 안해도 됨

        // 3번 item 수량 변경 15->10
        Item item = itemRepository.findById(3L).get();
        item.changeQuantity(10);

        // 4번 Order 주문상태 CANCEL로 변경
        Order order = orderRepository.findById(4L).get();
        order.changeOrderStatus(OrderStatus.CANCEL);
    }

    @Commit
    @Transactional
    @Test
    public void testDelete() {
        // 주문 제거
        // order, orderItem 제거

        // 방법 1 : 개별로 제거 / 자식 -> 부모 순으로 제거
        orderItemRepository.deleteById(1L);
        orderRepository.deleteById(1L);
    }

    @Commit
    @Transactional
    @Test
    public void testCascadeDelete() {
        // 주문 제거
        // order, orderItem 제거

        // 방법 2 : 부모 삭제 시 자식 같이 삭제( SQL => ON DELETE CASCADE )
        orderRepository.deleteById(4L);
    }

    @Commit
    @Transactional
    @Test
    public void testOrphanDelete() {

        // 주문 조회
        Order order = orderRepository.findById(5L).get();
        System.out.println(order); // Order(id=5, orderStatus=ORDER)

        // 주문 아이템 조회
        System.out.println(order.getOrderItems()); // [OrderItem(id=5, orderPrice=230000, count=1)]

        // 고아 객체 자동 삭제
        order.getOrderItems().remove(0);
        System.out.println("삭제 후 : " + order.getOrderItems());
        // orderRepository.save(order);
    }

    @Commit
    @Transactional
    @Test
    public void testDelivery() {
        // 2번 상품을 3번 고객이 주문
        Item item = itemRepository.findById(2L).get();
        Member member = memberRepository.findById(3L).get();

        Delivery delivery = Delivery.builder()
                .city("서울")
                .street("114")
                .zipcode("11061")
                .DeliveryStatus(DeliveryStatus.COMP)
                .build();

        deliveryRepository.save(delivery);

        Order order = Order.builder()
                .member(member)
                .orderStatus(OrderStatus.ORDER)
                .delivery(delivery)
                .build();

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                // .order(order)
                .orderPrice(230000)
                .count(1)
                .build();

        // order.getOrderItems().add(orderItem);
        order.addOrderItem(orderItem);
        orderRepository.save(order);

        // orderItemRepository.save(orderItem);
    }

    @Commit
    @Transactional
    @Test
    public void testCascadeDelivery() {
        // 2번 상품을 3번 고객이 주문
        Item item = itemRepository.findById(2L).get();
        Member member = memberRepository.findById(3L).get();

        Delivery delivery = Delivery.builder()
                .city("서울")
                .street("114")
                .zipcode("11061")
                .DeliveryStatus(DeliveryStatus.COMP)
                .build();
        // deliveryRepository.save(delivery);

        Order order = Order.builder()
                .member(member)
                .orderStatus(OrderStatus.ORDER)
                .delivery(delivery)
                .build();

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                // .order(order)
                .orderPrice(230000)
                .count(1)
                .build();

        // order.getOrderItems().add(orderItem);
        order.addOrderItem(orderItem);
        orderRepository.save(order);

        // orderItemRepository.save(orderItem);
    }

    @Transactional(readOnly = true)
    @Test
    public void orderReadTest2() {
        Order order = orderRepository.findById(7L).get();

        // order 조회
        System.out.println(order);

        // 고객 조회
        System.out.println(order.getMember());
        System.out.println(order.getMember().getName());

        // 주문 제품 조회
        System.out.println(order.getOrderItems());
        System.out.println(order.getOrderItems().get(0));

        // 배송 조회
        System.out.println(order.getDelivery());

    }

    @Transactional(readOnly = true)
    @Test
    public void memberReadTest() {
        Member member = memberRepository.findById(3L).get();

        // member 조회
        System.out.println(member);

        // 주문 조회
        System.out.println(member.getOrders());

        // 주문으로부터 데이터 가져오기 가능
        List<Order> orders = member.getOrders();
        orders.forEach(order -> {
            System.out.println(order.getMember());
            System.out.println(order.getDelivery());
            System.out.println(order.getOrderItems());
        });

    }

    @Transactional(readOnly = true)
    @Test
    public void orderItemReadTest() {
        OrderItem orderItem = orderItemRepository.findById(7L).get();

        // member 조회
        System.out.println(orderItem);

        // 주문 조회
        System.out.println(orderItem.getOrder());

        Order order = orderItem.getOrder();
        // 배송 조회
        System.out.println(order.getDelivery());
        // 고객 조회
        System.out.println(order.getMember());
        // 주문 상품 조회
        System.out.println(order.getOrderItems());

        // 물품 조회
        System.out.println(orderItem.getItem());

    }
    // ------------------------------------
    // @ManyToMany 설정을 JPA에게 시킨 경우
    // ------------------------------------

    @Test
    public void categoryTest() {
        Item item = itemRepository.findById(3L).get();
        Category category = Category.builder().name("가전제품").build();
        // category.getItems().add(item);
        categoryRepository.save(category);

        category = Category.builder().name("생활용품").build();
        // category.getItems().add(item);
        categoryRepository.save(category);

    }

    @Transactional(readOnly = true)
    @Test
    public void categoryReadTest() {
        Category category = categoryRepository.findById(1L).get();

        // 카테고리 조회
        System.out.println(category);
        // 카테고리에 속한 아이템 조회
        // System.out.println(category.getItems());
    }

    @Transactional(readOnly = true)
    @Test
    public void itemReadTest() {
        Item item = itemRepository.findById(3L).get();
        // 아이템 조회
        System.out.println(item);
        // 카테고리 조회
        // System.out.println(item.getCategories());
    }

    // ----------------------------------------
    // @ManyToMany 설정을 ManyToOne 관계로 작성
    // ----------------------------------------

    @Test
    public void categoryTest2() {
        Item item = itemRepository.findById(4L).get();

        // 카테고리 생성
        Category category = Category.builder().name("신혼용품").build();
        categoryRepository.save(category);

        CategoryItem categoryItem = CategoryItem.builder()
                .category(category)
                .item(item)
                .build();

        categoryItemRepository.save(categoryItem);

        // 기존 카테고리에 아이템 추가
        category = categoryRepository.findById(1L).get();
        categoryItem = CategoryItem.builder()
                .category(category)
                .item(item)
                .build();

        categoryItemRepository.save(categoryItem);

    }

    @Transactional(readOnly = true)
    @Test
    public void categoryItemReadTest() {
        CategoryItem categoryItem = categoryItemRepository.findById(1L).get();

        System.out.println(categoryItem);

        System.out.println(categoryItem.getCategory());
        System.out.println(categoryItem.getItem());

        // 양방향 연 뒤
        Category category = categoryRepository.findById(1L).get();
        System.out.println(category);
        System.out.println(category.getCategoryItems());

        Item item = itemRepository.findById(3L).get();
        System.out.println(item);
        System.out.println(item.getCategoryItems());
    }
}
