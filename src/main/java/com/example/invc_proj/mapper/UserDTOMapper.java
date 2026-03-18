package com.example.invc_proj.mapper;

import com.example.invc_proj.dto.UserDTO;
import com.example.invc_proj.model.User;
import com.example.invc_proj.repository.AppRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDTOMapper {

    @Autowired
    private AppRoleRepo appRoleRepo;

        public User toEntity(UserDTO dto) {
            if (dto == null) {
                return null;
            }

            User user = new User();

            user.setId(dto.getId());
            user.setUsername(dto.getUsername());
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setEmailId(dto.getEmailId());
            user.setRole(appRoleRepo.findById(dto.getRole_id()).orElseThrow(()->new RuntimeException("Role Not Found")));
            user.setStatus(dto.getStatus());
            user.setPassword(dto.getPassword());
            user.setPasswordSalt(dto.getPasswordSalt());
            return user;
        }

        public void updateEntityFromDto(UserDTO dto, User entity) {
            if (dto == null || entity == null) {
                return;
            }

            if (dto.getUsername() != null) {
                entity.setUsername(dto.getUsername());
            }
            if (dto.getFirstName() != null) {
                entity.setFirstName(dto.getFirstName());
            }
            if (dto.getLastName() != null) {
                entity.setLastName(dto.getLastName());
            }
            if (dto.getEmailId() != null) {
                entity.setEmailId(dto.getEmailId());
            }
            if (dto.getRole_id() != null) {
                entity.setRole(appRoleRepo.findById(dto.getRole_id()).orElseThrow(()->new RuntimeException("Role Not Found")));
            }
            if (dto.getStatus() != null) {
                entity.setStatus(dto.getStatus());
            }

        }

        public UserDTO toDto(User entity) {
            if (entity == null) {
                return null;
            }

            UserDTO dto = new UserDTO();

            dto.setId(entity.getId());
            dto.setUsername(entity.getUsername());
            dto.setFirstName(entity.getFirstName());
            dto.setLastName(entity.getLastName());
            dto.setEmailId(entity.getEmailId());
            dto.setRole_id(entity.getRole().getId());
            dto.setStatus(entity.getStatus());
            return dto;
        }

}
