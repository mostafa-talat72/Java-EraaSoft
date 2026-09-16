package com.task1springrest.service.Impl;

import com.task1springrest.model.Player;
import com.task1springrest.repo.PlayerRepo;
import com.task1springrest.service.PlayerService;
import jakarta.transaction.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    private PlayerRepo playerRepo;

    @Autowired
    public PlayerServiceImpl(PlayerRepo playerRepo) {
        this.playerRepo = playerRepo;
    }

    @Override
    public Player addPlayer(Player player) throws SystemException {
        if(Objects.nonNull(player.getId()))
        {
            throw new SystemException("id must be null");
        }

        return playerRepo.save(player);
    }

    @Override
    public Player updatePlayer(Player player) throws SystemException {
        if(Objects.isNull(player.getId()))
        {
            throw new SystemException("is must be not null");
        }

        Optional<Player> playerOptional = playerRepo.findById(player.getId());
        if(playerOptional.isEmpty()){
            throw new SystemException("player not found with id: " + player.getId());
        }
        return playerRepo.save(player);
    }

    @Override
    public Player getPlayerById(long id) throws SystemException {
        return playerRepo.findById(id).orElseThrow(() -> new SystemException("Player not found with id " + id));
    }

    @Override
    public void deletePlayer(long id) {
        playerRepo.deleteById(id);
    }

    @Override
    public List<Player> getAllPlayers() {
        return playerRepo.findAll();
    }
}
