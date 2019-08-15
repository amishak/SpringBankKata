package org.acme.retail.bank.account.service;

import org.springframework.stereotype.Service;

@Service
public class IdentityService {

    public boolean isVerified(String token) {
        String[] tokenPairs = token.split("=");
        return tokenPairs.length > 1 && !tokenPairs[1].isEmpty();
    }
}
