package com.task2springmvc.controller;

import com.task2springmvc.moedel.Item;
import com.task2springmvc.service.ItemService;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public String saveItem(@ModelAttribute("item") Item item){
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
    public String updateItem(@ModelAttribute("item") Item item){
        itemService.saveItem(item);
        return "redirect:/item/list";
    }

}

//@Controller
//@RequestMapping("/item")
//public class ItemController {
//
//    private ItemService itemService;
//
//    @Autowired
//    public ItemController(ItemService itemService) {
//        this.itemService = itemService;
//    }
//
//    // Add Item
//    @GetMapping("/form")
//    public String showForm(Model model) {
//        model.addAttribute("item", new Item());
//        return "item-form";
//    }
//
//    @PostMapping("/save")
//    public String saveItem(@ModelAttribute("item") Item item) {
//        itemService.saveItem(item);
//        return "redirect:/item/list";
//    }
//
//    // Show All Items
//    @GetMapping("/list")
//    public String showAllItems(Model model) {
//        model.addAttribute("items", itemService.getAllItems());
//        return "item-list";
//    }
//
//    // Delete Item
//    @GetMapping("/delete")
//    public String deleteItem(@RequestParam("id") long id) {
//        itemService.deleteItem(id);
//        return "redirect:/item/list";
//    }
//
//    // Open Update Form
//    @GetMapping("/update")
//    public String showUpdateForm(
//            @RequestParam("id") long id,
//            Model model) {
//
//        Item item = itemService.getItemById(id);
//
//        model.addAttribute("item", item);
//
//        return "item-update-form";
//    }
//
//    // Update Item
//    @PutMapping("/update")
//    public String updateItem(@ModelAttribute("item") Item item) {
//
//        itemService.updateItem(item);
//
//        return "redirect:/item/list";
//    }
//
//    // Show Item By ID
//    @GetMapping("/show")
//    public String showItemById(
//            @RequestParam("id") long id,
//            Model model) {
//
//        Item item = itemService.getItemById(id);
//
//        model.addAttribute("item", item);
//
//        return "item-details";
//    }
//}