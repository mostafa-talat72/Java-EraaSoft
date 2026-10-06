package com.tasklec11.helper;

import com.tasklec11.dto.PostSimpleDTO;
import com.tasklec11.dto.UserSimpleDTO;
import com.tasklec11.exception.PostException;
import com.tasklec11.exception.UserException;
import com.tasklec11.mapper.PostMapper;
import com.tasklec11.mapper.UserMapper;
import com.tasklec11.model.Post;
import com.tasklec11.model.User;
import com.tasklec11.repo.PostRepo;
import com.tasklec11.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserPostHelper {

    private UserRepo userRepo;
    private UserMapper userMapper;

    private PostRepo postRepo;
    private PostMapper postMapper;

    @Autowired
    public UserPostHelper(UserRepo userRepo, UserMapper userMapper, PostRepo postRepo, PostMapper postMapper) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.postRepo = postRepo;
        this.postMapper = postMapper;
    }


    public List<PostSimpleDTO> getAllPostWithUser(long id){
        List<Post> posts = postRepo.findAllByUser(
                userMapper.convertFromUserSimpleDtoToUser(
                        getUserById(id)
                )
        );
        if(posts.isEmpty()){
            throw new PostException(
                    "user",
                    "Post does not exist with user"
            );
        }

        return postMapper.convertFromPostListToPostSimpleDtoList(posts);
    }

    public UserSimpleDTO getUserById(Long id){
        Optional<User> user = userRepo.findById(id);
        if(user.isEmpty()){
            throw new UserException(
                    "id",
                    "User does not exist with ID: " + id
            );
        }

        return userMapper.convertFromUserToUserSimpleDto(user.get());
    }
}
