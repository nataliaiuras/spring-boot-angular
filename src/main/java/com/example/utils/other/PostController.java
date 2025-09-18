package com.example.utils.other;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = {"/", ""})
 //   @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<PostDto>> allPosts() {
        return ResponseEntity.ok(postService.allPosts());
    }

    @PostMapping("create")
  //  @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PostDto> create(@RequestBody PostRequestDto dto, Authentication auth) {
        PostDto result = postService.create(dto, auth.getName());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("mine")
 //   @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<PostDto>> myPosts(Authentication auth) {
        List<PostDto> result = postService.myPosts(auth.getName());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("{id}")
  //  @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody PostRequestDto req, Authentication auth) {
        try {
            postService.update(id, req, auth.getName());
            return new ResponseEntity<>("updated", HttpStatus.OK);
        } catch (AccessDeniedException ade) {
            return new ResponseEntity<>(ade.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("{id}")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth) {
        try {
            boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            postService.delete(id, isAdmin, auth.getName());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (AccessDeniedException ade) {
            return new ResponseEntity<>(ade.getMessage(), HttpStatus.FORBIDDEN);
        } catch (NoSuchElementException nse) {
            return new ResponseEntity<>(nse.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}