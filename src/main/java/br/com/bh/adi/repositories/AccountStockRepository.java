package br.com.bh.adi.repositories;

import br.com.bh.adi.entities.AccountStock;
import br.com.bh.adi.entities.AccountStockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountStockRepository extends JpaRepository<AccountStock, AccountStockId> {
}
