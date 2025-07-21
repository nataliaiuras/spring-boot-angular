package com.example.utils;

import com.example.dtos.request.RegisterRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        RegisterRequestDto user = (RegisterRequestDto) obj;
       // return user.getPassword().equals(user.getMatchingPassword());
        return false;
    }
}
