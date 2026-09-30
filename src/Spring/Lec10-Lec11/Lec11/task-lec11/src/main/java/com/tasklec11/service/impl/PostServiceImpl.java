package com.tasklec11.service.impl;

import com.tasklec11.dto.PostDTO;
import com.tasklec11.dto.PostSimpleDTO;
import com.tasklec11.dto.UserSimpleDTO;
import com.tasklec11.exception.PostException;
import com.tasklec11.mapper.PostMapper;
import com.tasklec11.mapper.UserMapper;
import com.tasklec11.model.Post;
import com.tasklec11.repo.PostRepo;
import com.tasklec11.service.PostService;
import com.tasklec11.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PostServiceImpl implements PostService {

    private PostRepo postRepo;
    private PostMapper postMapper;

    private UserService userService;
    private UserMapper userMapper;

    @Autowired
    public PostServiceImpl(PostRepo postRepo, PostMapper postMapper, UserService userService, UserMapper userMapper) {
        this.postRepo = postRepo;
        this.postMapper = postMapper;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        if(Objects.nonNull(postDTO.getId())){
            throw new PostException(
                    "id",
                    "Post ID must be null when creating a new post"
            );
        }

        if(Objects.isNull(postDTO.getUser().getId())){
            throw new PostException(
                    "userId",
                    "User ID must be not null when creating a new post"
            );
        }

        UserSimpleDTO user = userService.getUserById(postDTO.getUser().getId());
        if(Objects.isNull(user)){
            throw new PostException(
                    "user",
                    "User does not exist with ID: " + postDTO.getUser().getId()
            );
        }
        postDTO.setUser(user);

        return postMapper.convertFromPostToPostDto(
                postRepo.save(
                        postMapper.convertFromPostDtoToPost(
                                postDTO
                        )
                )
        );
    }

    @Override
    public PostSimpleDTO getPostById(Long id) {

        if(!postRepo.existsById(id)){
            throw new PostException(
                    "id",
                    "Post does not exist with ID: " + id
            );
        }

        return postMapper.convertFromPostToPostSimpleDto(
                postRepo.findById(id).get()
        );
    }

    @Override
    public List<PostSimpleDTO> getAllPosts() {
       List<Post> posts = postRepo.findAll();
       if(posts.isEmpty()){
           throw new PostException(
                   "posts",
                   "No posts found"
           );
       }
       return postMapper.convertFromPostListToPostSimpleDtoList(posts);
    }

    @Override
    public PostSimpleDTO updatePost(Long id, PostSimpleDTO postSimpleDTO) {
       if(!postRepo.existsById(id)){
           throw new PostException(
                   "id",
                   "Post does not exist with ID: " + id
           );
       }

       if(Objects.nonNull(postSimpleDTO.getId()) && !postSimpleDTO.getId().equals(id)){
           throw new PostException(
                   "id",
                   "Post id in api link: " + id + " dose not equal postt id in object you send: " + postSimpleDTO.getId()
           );
       }

       postSimpleDTO.setId(id);

       return postMapper.convertFromPostToPostSimpleDto(
               postRepo.save(
                       postMapper.convertFromPostSimpleDtoToPost(postSimpleDTO)
               )
       );
    }

    @Override
    public void deletePost(Long id) {

        if(!postRepo.existsById(id)){
            throw new PostException(
                    "id",
                    "Post does not exist with ID: " + id
            );
        }

        postRepo.deleteById(id);
    }

    @Override
    public List<PostDTO> getPostsByParticularUserId(Long userId) {
        UserSimpleDTO user = userService.getUserById(userId);
        if(Objects.isNull(user)){
            throw new PostException(
                    "userId",
                    "User ID must be not null when creating a new post"
            );
        }

        List<Post> posts = postRepo.findAllByUser(
                userMapper.convertFromUserSimpleDtoToUser(user)
        );

        return postMapper.convertFromPostListToPostDtoList(posts);
    }

    @Override
    public List<PostDTO> getAllPostsWithUsers() {
        List<Post> posts = postRepo.findAll();

        if(posts.isEmpty()){
            throw new PostException(
                    "posts",
                    "No posts found"
            );
        }

        return postMapper.convertFromPostListToPostDtoList(posts);
    }

    @Override
    public PostDTO getPostWithUsersById(Long id) {
       if(!postRepo.existsById(id)){
           throw new PostException(
                   "id",
                   "Post does not exist with ID: " + id
           );
       }

       return postMapper.convertFromPostToPostDto(
               postRepo.findById(id).get()
       );
    }
}
