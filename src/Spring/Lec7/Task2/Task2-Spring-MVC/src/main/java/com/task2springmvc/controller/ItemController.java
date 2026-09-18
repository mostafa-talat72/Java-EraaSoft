package com.task2springmvc.controller;

import com.task2springmvc.moedel.Item;
import com.task2springmvc.service.ItemService;
import jakarta.transaction.SystemException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/item")
public class ItemController {

    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    @GetMapping("/add")
    public String addForm(Model model){
        model.addAttribute("item", new Item());
        return "item-form";
    }

    @PostMapping("/save")
    public String saveItem(@Valid @ModelAttribute("item") Item item, BindingResult bindingResult) throws SystemException {
        if(bindingResult.hasErrors()){
            return "item-form";
        }
        itemService.saveItem(item);
        return "redirect:/item/list";
    }

    @GetMapping("/list")
    public String showAllItems(Model model){
        List<Item> items = itemService.showAllItems();
        model.addAttribute("items", items);
        return "item-list";
    }

    @DeleteMapping("/delete")
    public String deleteItem(@RequestParam("id") long id){
        itemService.deleteItemById(id);
        return "redirect:/item/list";
    }

    @GetMapping("/show")
    public String showItemDetails(@RequestParam("id") long id, Model model){
        Item item = itemService.showItemById(id);
        model.addAttribute("item", item);
        return "item-details";
    }

    @GetMapping("/update")
    public String showUpdateForm(@RequestParam("id") long id, Model model){
        Item item = itemService.showItemById(id);
        model.addAttribute("item", item);
        return "item-update-form";
    }

    @PutMapping("/update")
    public String updateItem(@Valid @ModelAttribute("item") Item item, BindingResult bindingResult) throws SystemException {
        if(bindingResult.hasErrors()){
            return "item-update-form";
        }
        itemService.updateItem(item);
        return "redirect:/item/list";
    }

}
