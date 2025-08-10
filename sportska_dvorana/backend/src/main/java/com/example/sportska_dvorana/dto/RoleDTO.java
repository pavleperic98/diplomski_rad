package com.example.sportska_dvorana.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class RoleDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long roleId;

    @Schema(example = "test role")
    @NotBlank(message = "Role is required")
    private String role;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
