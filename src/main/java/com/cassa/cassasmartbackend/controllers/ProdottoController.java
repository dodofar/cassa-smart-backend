package com.cassa.cassasmartbackend.controllers;

import com.cassa.cassasmartbackend.model.daos.ProdottoDao;
import com.cassa.cassasmartbackend.model.entities.Prodotto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prodotti")
public class ProdottoController {

	@Autowired
	private ProdottoDao prodottoDao;

	@GetMapping
	public List<Prodotto> getAllProdotti() {
		return prodottoDao.findAll();
	}

	@GetMapping("/{id}")
	public Prodotto getProdottoById(@PathVariable Long id) {
		return prodottoDao.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato con id " + id));
	}

	@PostMapping
	public Prodotto creaProdotto(@RequestBody Prodotto prodotto) {
		return prodottoDao.save(prodotto);
	}

	@PutMapping("/{id}")
	public Prodotto aggiornaProdotto(@PathVariable Long id, @RequestBody Prodotto prodotto) {
		Prodotto esistente = prodottoDao.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato con id " + id));
		esistente.setNome(prodotto.getNome());
		esistente.setPrezzo(prodotto.getPrezzo());
		esistente.setQuantita(prodotto.getQuantita());
		return prodottoDao.save(esistente);
	}

	@DeleteMapping("/{id}")
	public void eliminaProdotto(@PathVariable Long id) {
		if (!prodottoDao.existsById(id)) {
			throw new EntityNotFoundException("Prodotto non trovato con id " + id);
		}
		prodottoDao.deleteById(id);
	}
}
