package com.cassa.cassasmartbackend.model.daos;
import com.cassa.cassasmartbackend.model.entities.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdottoDao extends JpaRepository<Prodotto, Long> {
}
