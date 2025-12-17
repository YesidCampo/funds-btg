package com.btgpactual.fundsbtg.mapper;

import com.btgpactual.fundsbtg.dto.request.UserRegistrationRequest;
import com.btgpactual.fundsbtg.dto.response.UserResponse;
import com.btgpactual.fundsbtg.model.User;

public class UserMapper {
    
    private UserMapper() {
        throw new UnsupportedOperationException("Esta es una clase de utilidad y no puede ser instanciada");
    }
    
    public static UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        
        return UserResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .nit(user.getNit())
                .balance(user.getBalance())
                .role(user.getRole())
                .preferredNotification(user.getPreferredNotification())
                .phoneNumber(user.getPhoneNumber())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
    
    public static User toEntity(UserRegistrationRequest request, String encodedPassword) {
        if (request == null) {
            return null;
        }
        
        return User.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .nit(request.getNit())
                .password(encodedPassword)
                .preferredNotification(request.getPreferredNotification())
                .phoneNumber(request.getPhoneNumber())
                .build();
    }
}
