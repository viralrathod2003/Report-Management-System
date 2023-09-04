package com.iris.springboot.configuration.service;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private ComboPooledDataSource dataSource;
   
    @Override
    public UserDetails loadUserByUsername(String username) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement userStatement = connection.prepareStatement("SELECT * FROM USERS WHERE USERNAME = ?");
             PreparedStatement authoritiesStatement = connection.prepareStatement("SELECT * FROM AUTHORITIES WHERE USER_ID = ?")) {

            userStatement.setString(1, username);
            try (ResultSet userResultSet = userStatement.executeQuery()) {
                if (userResultSet.next()) {
                    String password = userResultSet.getString("PASSWORD");
                    boolean enabled = userResultSet.getBoolean("ENABLED");
                    int userId = userResultSet.getInt("USER_ID");

                    authoritiesStatement.setInt(1, userId);
                    try (ResultSet authoritiesResultSet = authoritiesStatement.executeQuery()) {
                        List<GrantedAuthority> authorities = new ArrayList<>();
                        while (authoritiesResultSet.next()) {
                            String authority = authoritiesResultSet.getString("AUTHORITY");
                            authorities.add(new SimpleGrantedAuthority(authority));
                        }
                        return new User(username, password, enabled, true, true, true, authorities);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while accessing the database", e);
        }
        throw new UsernameNotFoundException("Not found " + username);
    }
}
