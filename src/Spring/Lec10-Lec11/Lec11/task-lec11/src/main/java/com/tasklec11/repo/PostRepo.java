package com.tasklec11.repo;

import com.tasklec11.model.Post;
import com.tasklec11.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {

    public List<Post> findAllByUser(User user);
}
