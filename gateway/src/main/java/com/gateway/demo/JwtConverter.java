package com.gateway.demo;

// es necesario crear un jwt converter para poder extraer datos como los roles

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;


public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken>
{//AbstractAuthenticationToken
   @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        Map<String, List<String>> realmAccess = jwt.getClaim("realm_access");

        //simplegranted Authority es la implementacion de grantedAuthority
        ArrayList<GrantedAuthority> roles = realmAccess.get("roles").stream().map(r -> "ROLE_" + r.toUpperCase()).map(g -> new SimpleGrantedAuthority(g)).collect(Collectors.toCollection(ArrayList::new));

        System.out.println(realmAccess.toString());
        //ArrayList<GrantedAuthority> autoridades = roles.stream().map()
        return new JwtAuthenticationToken(jwt, roles);
    }

}