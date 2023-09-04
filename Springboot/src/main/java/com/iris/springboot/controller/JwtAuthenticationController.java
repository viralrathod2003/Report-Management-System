package com.iris.springboot.controller;

import com.iris.springboot.configuration.jwt.JwtTokenUtil;
import com.iris.springboot.model.JwtResponse;
import com.iris.springboot.configuration.service.JwtUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JwtAuthenticationController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @GetMapping("/authenticate")
    public JwtResponse createAuthenticationToken(HttpServletRequest request) throws Exception {
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(request.getRemoteUser());
        final String token = jwtTokenUtil.generateToken(userDetails);
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return new JwtResponse(token, roles);
    }
}
