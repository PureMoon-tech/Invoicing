package com.example.invc_proj.mapper;

import com.example.invc_proj.dto.ServicesDropdownDTO;
import com.example.invc_proj.dto.UserDropownDTO;
import com.example.invc_proj.model.Services;
import com.example.invc_proj.model.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserDropdownDTOMapper {

    public static UserDropownDTO toDTO(User user)
    {
        if(user==null)
            return null;

        UserDropownDTO dto = new UserDropownDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUsername(user.getUsername());
        dto.setEmailId(user.getEmailId());
        dto.setRole_id(user.getRole().getId());
        dto.setStatus(user.getStatus());
        return dto;

    }

    public static List<UserDropownDTO> toDTOList(List<User> users) {
        if (users == null) {
            return Collections.emptyList();
        }
        return users.stream()
                .map(UserDropdownDTOMapper::toDTO)
                .collect(Collectors.toList());
    }
}
