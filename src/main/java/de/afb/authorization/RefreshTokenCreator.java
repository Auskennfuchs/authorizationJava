package de.afb.authorization;

import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.UUID;

@Service
public class RefreshTokenCreator {

    public String create() {
        return Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()).toString();
    }
}
