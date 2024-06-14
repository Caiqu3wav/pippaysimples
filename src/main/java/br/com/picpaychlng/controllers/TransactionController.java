package br.com.picpaychlng.controllers;

import br.com.picpaychlng.entities.Transaction;
import br.com.picpaychlng.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;


    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Optional<Transaction> transaction = transactionService.findTransactionById(id);
        return transaction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/payer/{payerId}")
    public ResponseEntity<List<Transaction>> getTransactionsByPayerId(@PathVariable Long payer) {
        List<Transaction> transactions = transactionService.findTransactionsByPayer(payer);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<List<Transaction>> getTransactionsByPayeeId(@PathVariable Long receiver) {
        List<Transaction> transactions = transactionService.findTransactionsByReceiver(receiver);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction savedTransaction = transactionService.saveTransaction(transaction);
        return ResponseEntity.ok(savedTransaction);
    }

    public static class TransferRequest {
        private Long payerId;
        private Long receiverId;
        private BigDecimal amount;

        public Long getPayerId() {
            return payerId;
        }

        public void setPayerId(Long payerId) {
            this.payerId = payerId;
        }

        public Long getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(Long receiverId) {
            this.receiverId = receiverId;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
