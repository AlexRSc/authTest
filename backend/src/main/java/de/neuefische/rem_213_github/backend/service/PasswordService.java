package de.neuefische.rem_213_github.backend.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    public String getNewPassword(){
        return RandomStringUtils.randomAlphanumeric(12);
    }

}
