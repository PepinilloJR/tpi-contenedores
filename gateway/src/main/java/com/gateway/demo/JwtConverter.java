package com.gateway.demo;

// es necesario crear un jwt converter para poder extraer datos como los roles

import java.util.ArrayList;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;


public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken>
{//AbstractAuthenticationToken
   @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        String roles = jwt.getClaim("realm_access");

        

        return new JwtAuthenticationToken(jwt, new ArrayList<GrantedAuthority>());
    }

}