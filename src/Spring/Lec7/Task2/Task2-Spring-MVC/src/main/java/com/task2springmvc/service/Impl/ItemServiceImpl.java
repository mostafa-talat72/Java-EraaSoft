package com.task2springmvc.service.Impl;

import com.task2springmvc.moedel.Item;
import com.task2springmvc.repo.ItemRepo;
import com.task2springmvc.service.ItemService;
import jakarta.transaction.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {
    private ItemRepo itemRepo;

    @Autowired
    public ItemServiceImpl(ItemRepo itemRepo) {
        this.itemRepo = itemRepo;
    }

    @Override
    public Item saveItem(Item item) throws SystemException {
        if (Objects.nonNull(item.getId())) {
            throw new SystemException("id must be null");
        }
        return itemRepo.save(item);
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

    @Override
    public Item updateItem(Item item) throws SystemException {
        if (Objects.isNull(item.getId())) {
            throw new SystemException("is must be not null");
        }

        Optional<Item> itemOptional = itemRepo.findById(item.getId());

        if (itemOptional.isEmpty()) {
            throw new SystemException("Item not found with id: " + item.getId());
        }

        return itemRepo.save(item);
    }
}
