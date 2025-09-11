package com.cassa.cassasmartbackend.model.daos;

import com.cassa.cassasmartbackend.model.entities.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrdineDao extends JpaRepository<Ordine, Long>
{
	@Query("SELECT o FROM Ordine o WHERE o.dataCreazione BETWEEN :start AND :end")
	List<Ordine> findByDataCreazioneBetween(LocalDateTime start, LocalDateTime end);
}
