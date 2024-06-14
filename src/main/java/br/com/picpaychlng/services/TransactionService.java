package br.com.picpaychlng.services;

import br.com.picpaychlng.entities.UserType;
import br.com.picpaychlng.repositories.UserRepo;
import org.springframework.stereotype.Service;

import br.com.picpaychlng.entities.Transaction;
import br.com.picpaychlng.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import br.com.picpaychlng.entities.Wallet;
import br.com.picpaychlng.services.WalletService;
import br.com.picpaychlng.services.NotificationService;
import br.com.picpaychlng.services.AuthorizationService;
import br.com.picpaychlng.exceptions.*;
import br.com.picpaychlng.repositories.UserRepo;
import br.com.picpaychlng.entities.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletService walletService;
    private final NotificationService notificationService;
    private final AuthorizationService authorizationService;
    private final UserRepo userRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, WalletService walletService, NotificationService notificationService, AuthorizationService authorizationService, UserRepo userRepository) {
        this.transactionRepository = transactionRepository;
        this.walletService = walletService;
        this.notificationService = notificationService;
        this.authorizationService = authorizationService;
        this.userRepository = userRepository;
    }

    public Optional<Transaction> findTransactionById(Long id) {
        return transactionRepository.findById(id);
    }


    public List<Transaction> findTransactionsByPayer(Long payer) {
        return transactionRepository.findByPayerId(payer);
    }

    public List<Transaction> findTransactionsByReceiver(Long receiver) {
        return transactionRepository.findByReceiverId(receiver);
    }

    @Transactional
    public Transaction performTransaction(Long payerId, Long receiverId, BigDecimal amount) throws Exception {
        Optional<User>  payerOpt = userRepository.findById(payerId);
        Optional<User> receiverOpt = userRepository.findById(receiverId);

        if (payerOpt.isEmpty() || receiverOpt.isEmpty()) {
            throw new Exception("Payer or receiver not found");
        }

        User payer = payerOpt.get();
        User receiver = receiverOpt.get();

        if (payer.getUserType() == UserType.LOJISTA) {
            throw new Exception("Lojistas não podem enviar transações");
        }

        Wallet payerWallet = walletService.findWalletByUserId(payerId)
                .orElseThrow(() -> new RuntimeException("Carteira do pagador não encontrada"));

        if (payerWallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Sado insuficiente para realizar transferência");
        }

        if (!authorizationService.isAuthorized()) {
            throw  new UnauthorizedTransactionException("Transação não autorizada por serviço externo");
        }

        payerWallet.setBalance(payerWallet.getBalance().subtract(amount));
        Wallet receiverWallet = walletService.findWalletByUserId(receiverId)
                .orElseThrow(() -> new RuntimeException("Carteira do recebedor não encontrada"));

        receiverWallet.setBalance(receiverWallet.getBalance().add(amount));

        walletService.saveWallet(payerWallet);
        walletService.saveWallet(receiverWallet);

        Transaction transaction = new Transaction();
        transaction.setPayer(payerWallet.getUser());
        transaction.setReceiver(receiverWallet.getUser());
        transaction.setAmount(amount);
        transaction.setCreatedAt(LocalDateTime.now());
        Transaction savedTransaction = transactionRepository.save(transaction);
        notificationService.notifyUser(receiverId, "Você recebeu uma transferência de " + amount);

        return savedTransaction;
    }

    @Transactional
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
