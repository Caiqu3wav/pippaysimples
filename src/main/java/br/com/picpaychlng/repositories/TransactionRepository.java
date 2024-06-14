package br.com.picpaychlng.repositories;

import br.com.picpaychlng.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByPayerId(Long payer);

    List<Transaction> findByReceiverId(Long receiver);
}
