package com.example.util.other;


import com.example.entity.User;
import com.example.service.impl.UserServiceImpl;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserServiceImpl userService;

    public PostService(PostRepository postRepository, UserServiceImpl userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public PostDto create(PostRequestDto req, String username) {
        User user = userService.getUserByName(username);
        Post post = new Post();
        post.setTitle(req.getTitle());
        post.setContent(req.getContent());
        post.setUser(user);
        return toDto(postRepository.save(post));
    }

    public void update(Long id, PostRequestDto dto, String username) {
        Post post = postRepository.findById(id).orElseThrow();
        if (!post.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You can only edit your own posts");
        }
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        postRepository.save(post);
    }

    public void delete(Long id, boolean isAdmin, String username) {
        Post post = postRepository.findById(id).orElseThrow();
        if (!isAdmin && !post.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You can only delete your own posts");
        }
        postRepository.delete(post);
    }

    public List<PostDto> myPosts(String username) {
        User user = userService.getUserByName(username);
        return postRepository.findByUser(user).stream().map(this::toDto).toList();
    }

    private PostDto toDto(Post post) {
        return new PostDto(post.getId(), post.getTitle(), post.getContent(), post.getUser().getUsername());
    }

    public List<PostDto> allPosts() {
        return postRepository.findAll().stream().map(this::toDto).toList();
    }
}