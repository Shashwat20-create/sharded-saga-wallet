package com.example.shardedsagawallet.services.saga.steps;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.shardedsagawallet.entities.Wallet;
import com.example.shardedsagawallet.repositories.WalletRepository;
import com.example.shardedsagawallet.services.saga.SagaContext;
import com.example.shardedsagawallet.services.saga.SagaStep;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditDestinationWalletStep implements SagaStep{

    private final WalletRepository walletRepository;

    @Override
    @Transactional
    public boolean execute(SagaContext context){
        Long toWalletId = context.getLong("toWalletId");
        BigDecimal amount = context.getBigDecimal("amount");

        log.info("Crediting Destination Wallet {} with amount {}", toWalletId, amount);

        Wallet wallet = walletRepository.findByIdWithLock(toWalletId)
                         .orElseThrow(() -> new RuntimeException("Wallet not found"));

        log.info("Wallet fetched with balance {}", wallet.getBalance());
        context.put("originalToWalletBalance", wallet.getBalance());

        wallet.credit(amount);
        walletRepository.save(wallet);

        log.info("Wallet saved with balance {}", wallet.getBalance());
        context.put("toWalletBalanceAfterCredit", wallet.getBalance());

        log.info("Credit destination wallet step executed successfully");
        return true;
    }

    @Override
    @Transactional
    public boolean compensate(SagaContext context){
        Long toWalletId = context.getLong("toWalletId");
        BigDecimal amount = context.getBigDecimal("amount");

        log.info("Compensation credit of destination wallet {} with amount {}", toWalletId, amount);

        Wallet wallet = walletRepository.findByIdWithLock(toWalletId)
                         .orElseThrow(() -> new RuntimeException("Wallet not found"));

        log.info("Wallet fetched with balance {}", wallet.getBalance());

        wallet.debit(amount);
        walletRepository.save(wallet);

        log.info("Wallet saved with balance {}", wallet.getBalance());
        context.put("toWalletBalanceAfterCreditCompensation", wallet.getBalance());

        log.info("Credit destination wallet step executed successfully");
        return true;
    }

    @Override
    public String getStepName() {
        return "CreditDestinationWalletStep";
    }

}
