package com.example.other;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long id;
    private String title;
    private String content;
    private String username;

}