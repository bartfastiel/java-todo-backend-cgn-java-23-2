package de.neuefische.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongoUserService {

    private final MongoUserRepository mongoUserRepository;


    public MongoUser findUserByUsername(String username) {
        return mongoUserRepository.findByUsername(username).orElseThrow();
    }
}
