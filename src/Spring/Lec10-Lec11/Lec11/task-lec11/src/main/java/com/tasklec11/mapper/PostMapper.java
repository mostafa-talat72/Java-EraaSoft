package com.tasklec11.mapper;

import com.tasklec11.dto.PostDTO;
import com.tasklec11.dto.PostSimpleDTO;
import com.tasklec11.model.Post;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    public Post convertFromPostDtoToPost(PostDTO postDTO);

    public PostDTO convertFromPostToPostDto(Post post);

    public List<Post> convertFromPostDtoListToPostList(List<PostDTO> postDTOList);

    public List<PostDTO> convertFromPostListToPostDtoList(List<Post> posts);

    public Post convertFromPostSimpleDtoToPost(PostSimpleDTO postSimpleDTO);

    public PostSimpleDTO convertFromPostToPostSimpleDto(Post post);

    public List<Post> convertFromPostSimpleDtoListToPostList(List<PostSimpleDTO> postSimpleDTOList);

    public List<PostSimpleDTO> convertFromPostListToPostSimpleDtoList(List<Post> posts);

}
