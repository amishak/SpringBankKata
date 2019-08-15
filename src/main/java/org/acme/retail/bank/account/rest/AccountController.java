package org.acme.retail.bank.account.rest;

import org.acme.retail.bank.account.model.Account;
import org.acme.retail.bank.account.service.AccountService;
import org.acme.retail.bank.account.service.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private IdentityService tokenService;

    @GetMapping("/{id}")
    public ResponseEntity getAccount(@PathVariable long id, @RequestHeader("Authorization-X") @NotNull String token) {
        if (!tokenService.isVerified(token)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        Account account = accountService.get(id);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @NotNull Account account, @RequestHeader("Authorization-X") @NotNull String token) {
        if (!tokenService.isVerified(token)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        Account createdAccount = accountService.register(account);
        if (createdAccount == null) {
            return ResponseEntity.unprocessableEntity().build();
        }
        return ResponseEntity.ok(createdAccount);
    }

}
