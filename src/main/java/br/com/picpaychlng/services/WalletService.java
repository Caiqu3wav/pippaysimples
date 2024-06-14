package br.com.picpaychlng.services;

import br.com.picpaychlng.entities.Wallet;
import br.com.picpaychlng.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Optional<Wallet> findWalledById(Long id) {
        return walletRepository.findById(id);
    }

    public Optional<Wallet> findWalletByUserId(Long user_id) {
        return walletRepository.findByUserId(user_id);
    }


    public Wallet saveWallet(Wallet wallet) {

        return  walletRepository.save(wallet);
    }

    public void deleteWallet(Long id) {
        walletRepository.deleteById(id);
    }
}