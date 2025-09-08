package com.homelab.app.service;

import com.homelab.app.dto.request.LoginRequest;
import com.homelab.app.dto.request.UsuarioRequest;
import com.homelab.app.dto.response.AuthResponse;
import com.homelab.app.dto.response.UsuarioResponse;
import com.homelab.app.model.Role;
import com.homelab.app.model.Usuario;
import com.homelab.app.repository.UsuarioRepository;
import com.homelab.app.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    
    public AuthResponse authenticate(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );
        
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
        String token = jwtTokenProvider.generateToken(authentication);
        
        return AuthResponse.builder()
            .token(token)
            .tipo("Bearer")
            .usuario(UsuarioResponse.fromEntity(usuario))
            .build();
    }
    
    public UsuarioResponse cadastrarUsuario(UsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }
        
        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setRoles(Set.of(Role.USER));
        
        usuario = usuarioRepository.save(usuario);
        
        return UsuarioResponse.fromEntity(usuario);
    }
}