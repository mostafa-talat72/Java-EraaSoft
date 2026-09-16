package com.task1springrest.controller;

import com.task1springrest.model.Player;
import com.task1springrest.service.PlayerService;
import jakarta.transaction.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    //in postman method POST --> localhost:8085/players and send json in body
    @PostMapping
    public Player addPlayer(@RequestBody Player player) throws SystemException {
        return playerService.addPlayer(player);
    }

    //in postman method PUT --> localhost:8085/players and send json in body
    @PutMapping
    public Player updatePlayer( @RequestBody Player player) throws SystemException {
        return playerService.updatePlayer(player);
    }

    //in postman method GET --> localhost:8085/players/id
    @GetMapping("/{id}")
    public Player getPlayer(@PathVariable long id) throws SystemException {
        return playerService.getPlayerById(id);
    }

    //in postman method DELETE --> localhost:8085/players/id
    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable long id){
        playerService.deletePlayer(id);
    }

    //in postman method GET --> localhost:8085/players
    @GetMapping
    public List<Player> getAllPlayers(){
        return playerService.getAllPlayers();
    }
}

