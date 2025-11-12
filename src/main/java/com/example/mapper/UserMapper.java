package com.example.mapper;

import com.example.dto.response.user.UserDetailResponse;
import com.example.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDetailResponse toUserDetailResponse(User user);
}
