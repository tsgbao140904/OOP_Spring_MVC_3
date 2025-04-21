package com.abc.services;

import java.util.List;
import com.abc.entities.Post;

public interface PostService {
    List<Post> getAllPost(Long id);
    List<Post> getPostById(Long id);
    boolean createdPost(Post post);
}