package com.task2springmvc.service.Impl;

import com.task2springmvc.moedel.Item;
import com.task2springmvc.repo.ItemRepo;
import com.task2springmvc.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {
    private ItemRepo itemRepo;

    @Autowired
    public ItemServiceImpl(ItemRepo itemRepo) {
        this.itemRepo = itemRepo;
    }

    @Override
    public boolean saveItem(Item item) {
        try {
            itemRepo.save(item);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public List<Item> showAllItems() {
        return itemRepo.findAll();
    }

    @Override
    public Item showItemById(long id) {
        return itemRepo.getById(id);
    }

    @Override
    public void deleteItemById(long id) {
        itemRepo.deleteById(id);
    }
}
