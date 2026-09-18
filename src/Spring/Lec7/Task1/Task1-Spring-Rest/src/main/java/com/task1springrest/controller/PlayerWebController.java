package com.task1springrest.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task1springrest.model.Player;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Thymeleaf pages that talk to the REST API via HTTP (server-side),
 * instead of calling PlayerService directly.
 *
 * REST base: http://localhost:8085/players (PlayerController)
 */
@Controller
@RequestMapping("/web/players")
public class PlayerWebController {

    private static final String API_BASE = "http://localhost:8085/players";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public PlayerWebController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // GET /web/players -> players/list.html (data from GET /players)
    @GetMapping
    public String listPlayers(Model model) {
        try {
            Player[] arr = restTemplate.getForObject(API_BASE, Player[].class);
            List<Player> players = arr == null ? Collections.emptyList() : Arrays.asList(arr);
            model.addAttribute("players", players);
        } catch (Exception e) {
            model.addAttribute("players", Collections.emptyList());
            model.addAttribute("loadError", "Cannot load players from REST API: " + e.getMessage());
        }
        return "players/list";
    }

    // GET /web/players/new -> players/form.html (empty)
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        if (!model.containsAttribute("player")) {
            model.addAttribute("player", new Player());
        }
        return "players/form";
    }

    // POST /web/players -> create via POST /players
    // (form sends POST, no _method here)
    @PostMapping
    public String createPlayer(@Valid @ModelAttribute("player") Player player,
                               BindingResult result) {
        if (result.hasErrors()) {
            return "players/form";
        }
        try {
            restTemplate.postForObject(API_BASE, player, Player.class);
        } catch (RestClientResponseException e) {
            mapRestErrors(e, result);
            return "players/form";
        }
        return "redirect:/web/players";
    }

    // GET /web/players/edit/{id} -> players/form.html (filled from GET /players/{id})
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable long id, Model model) {
        try {
            Player player = restTemplate.getForObject(API_BASE + "/" + id, Player.class);
            model.addAttribute("player", player);
        } catch (RestClientResponseException e) {
            return "redirect:/web/players";
        }
        return "players/form";
    }

    // GET /web/players/{id} -> players/details.html (profile from GET /players/{id})
    @GetMapping("/{id}")
    public String showProfile(@PathVariable long id, Model model) {
        try {
            Player player = restTemplate.getForObject(API_BASE + "/" + id, Player.class);
            model.addAttribute("player", player);
        } catch (RestClientResponseException e) {
            return "redirect:/web/players";
        }
        return "players/details";
    }

    // PUT /web/players/{id} -> update via PUT /players/{id}
    // (form sends POST + hidden _method=PUT, filter converts it)
    @PutMapping("/{id}")
    public String updatePlayer(@PathVariable long id,
                               @Valid @ModelAttribute("player") Player player,
                               BindingResult result) {
        if (result.hasErrors()) {
            return "players/form";
        }
        player.setId(id);
        try {
            restTemplate.put(API_BASE + "/" + id, player);
        } catch (RestClientResponseException e) {
            mapRestErrors(e, result);
            return "players/form";
        }
        return "redirect:/web/players";
    }

    // DELETE /web/players/{id} -> delete via DELETE /players/{id}
    // (form sends POST + hidden _method=DELETE, filter converts it)
    @DeleteMapping("/{id}")
    public String deletePlayer(@PathVariable long id) {
        restTemplate.delete(API_BASE + "/" + id);
        return "redirect:/web/players";
    }

    /**
     * Maps REST error responses back to BindingResult so th:errors can display them.
     * Without a custom handler, Spring returns its default error JSON,
     * so we fall back to a generic message if parsing fails.
     */
    private void mapRestErrors(RestClientResponseException e, BindingResult result) {
        try {
            Map<String, String> body = objectMapper.readValue(
                    e.getResponseBodyAsString(), new TypeReference<>() {});
            body.forEach((field, msg) -> {
                if ("error".equals(field)) {
                    result.reject("global", msg);
                } else {
                    try {
                        result.rejectValue(field, "", msg);
                    } catch (Exception ex) {
                        result.reject("global", field + ": " + msg);
                    }
                }
            });
            if (body.isEmpty()) {
                result.reject("global", "REST API error: " + e.getStatusCode());
            }
        } catch (Exception parseEx) {
            result.reject("global", "REST API error: " + e.getStatusCode());
        }
    }
}
