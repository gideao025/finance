package com.homelab.app.dto.response;

import com.homelab.app.model.Role;
import com.homelab.app.model.Usuario;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class UsuarioResponse {
    private Long id;
    private String nome;
    private String email;
    private Set<Role> roles;
    private LocalDateTime criadoEm;
    
    public static UsuarioResponse fromEntity(Usuario usuario) {
        return UsuarioResponse.builder()
            .id(usuario.getId())
            .nome(usuario.getNome())
            .email(usuario.getEmail())
            .roles(usuario.getRoles())
            .criadoEm(usuario.getCriadoEm())
            .build();
    }
}