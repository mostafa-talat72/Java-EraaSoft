package com.tasklec11.controller;

import com.tasklec11.dto.PostDTO;
import com.tasklec11.dto.PostSimpleDTO;
import com.tasklec11.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO postDTO){
        postDTO = postService.createPost(postDTO);
        return ResponseEntity.created(URI.create("/posts/" + postDTO.getId())).body(postDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostSimpleDTO> getPostById(@PathVariable Long id){
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping
    public  ResponseEntity<List<PostSimpleDTO>> getAllPosts(){
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostSimpleDTO> updatePost(@PathVariable Long id,@Valid  @RequestBody PostSimpleDTO postSimpleDTO){
        return ResponseEntity.ok(postService.updatePost(id, postSimpleDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<PostDTO>> getPostsByParticularUserId(@PathVariable Long userId){
        return ResponseEntity.ok(postService.getPostsByParticularUserId(userId));
    }

    @GetMapping("/postsWithUsers")
    public ResponseEntity<List<PostDTO>> getAllPostsWithUsers(){
        return ResponseEntity.ok(postService.getAllPostsWithUsers());
    }

    @GetMapping("/postsWithUsers/{id}")
    public ResponseEntity<PostDTO> getPostWithUsersById(@PathVariable Long id){
        return ResponseEntity.ok(postService.getPostWithUsersById(id));
    }

}