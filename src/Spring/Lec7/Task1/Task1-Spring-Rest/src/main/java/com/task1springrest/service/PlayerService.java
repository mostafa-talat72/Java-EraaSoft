package com.task1springrest.service;

import com.task1springrest.model.Player;
import jakarta.transaction.SystemException;

import java.util.List;

public interface PlayerService {

    public Player addPlayer(Player player) throws SystemException;

    public Player updatePlayer(Player player) throws SystemException;

    public Player getPlayerById(long id) throws SystemException;

    public void deletePlayer(long id);

    public List<Player> getAllPlayers();
}
