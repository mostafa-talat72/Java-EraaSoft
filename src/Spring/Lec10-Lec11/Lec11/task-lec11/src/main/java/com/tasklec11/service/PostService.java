package com.tasklec11.service;

import com.tasklec11.dto.PostDTO;
import com.tasklec11.dto.PostSimpleDTO;
import com.tasklec11.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface PostService {

    public PostDTO createPost(PostDTO postDTO);

    public PostSimpleDTO getPostById(Long id);

    public  List<PostSimpleDTO> getAllPosts();

    public PostSimpleDTO updatePost(Long id, PostSimpleDTO postSimpleDTO);

    public void deletePost(Long id);


    public List<PostDTO> getAllPostsWithUsers();

    public PostDTO getPostWithUsersById(Long id);

    public List<PostSimpleDTO> getAllPostWithUser(User user);
}
