package com.task2springmvc.service;

import com.task2springmvc.moedel.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    public boolean saveItem(Item item);

    public List<Item> showAllItems();

    public Item showItemById(long id);

    public void deleteItemById(long id);

}
