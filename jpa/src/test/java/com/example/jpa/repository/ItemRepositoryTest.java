package com.example.jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jpa.entity.Item;
import com.example.jpa.entity.constant.ItemSellStatus;

@SpringBootTest
public class ItemRepositoryTest {
    
    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void insertTest(){
        for (int i = 1; i < 10; i++) {
            Item item = Item.builder()
            .code("P00"+i)
            .itemPrice(1000*i)
            .stockNumber(10)
            .itemDetail("Item Detail"+i)
            .itemSellStatus(ItemSellStatus.SELL)
            .itemNm("Item"+i)
            .build();
            itemRepository.save(item);
        }
    }

    
    @Test
    public void updateTest(){
        // item 상태
        Item item = itemRepository.findById("P005").get();
        item.changeItemSellStatus(ItemSellStatus.SOLDOUT);
        item.changeStockNumber(0);
        itemRepository.save(item);
    }

    @Test
    public void updateTest2(){
        // 재고수량 변경
        Item item = itemRepository.findById("P006").get();
        item.changeStockNumber(5);
        itemRepository.save(item);
    }
    @Test
    public void deleteTest(){
        itemRepository.deleteById("P008");
    }
    @Test
    public void readTest(){
        System.out.println(itemRepository.findById("P009").get());
    }
    @Test
    public void readTest2(){
        itemRepository.findAll().forEach(item -> System.out.println(item));
    }
}
