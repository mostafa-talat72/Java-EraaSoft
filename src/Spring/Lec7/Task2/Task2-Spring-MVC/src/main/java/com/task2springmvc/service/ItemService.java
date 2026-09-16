package com.task2springmvc.service;

import com.task2springmvc.moedel.Item;
import jakarta.transaction.SystemException;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    public Item saveItem(Item item) throws SystemException;

    public List<Item> showAllItems();

    public Item showItemById(long id);

    public void deleteItemById(long id);

    public Item updateItem(Item item) throws SystemException;
}
