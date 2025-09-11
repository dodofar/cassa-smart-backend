package com.cassa.cassasmartbackend.model.dtos.service;

import com.cassa.cassasmartbackend.model.daos.ProdottoDao;
import com.cassa.cassasmartbackend.model.entities.Prodotto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdottoService
{

	private final ProdottoDao prodottoDao;

	//Lista prodotti
	public List<Prodotto> listaProdotti()
	{
		return prodottoDao.findAll();
	}

	//Ricerca prodotti tramite id
	public Prodotto getProdottoById(Long id)
	{
		return prodottoDao.findById(id)
				.orElseThrow(() -> new RuntimeException("Prodotto con id: " + id + "non trovato"));
	}

	//Creazione prodotto
	public Prodotto creaProdotto(Prodotto prodotto)
	{
		return prodottoDao.save(prodotto);
	}

	//Aggiorna prodotto
	public Prodotto aggiornaProdotto(Long id, Prodotto prodotto)
	{
		Prodotto esistente = prodottoDao.findById(id)
				.orElseThrow(() -> new RuntimeException("Prodotto con id: " + id + "non trovato"));

		esistente.setNome(prodotto.getNome());
		esistente.setPrezzo(prodotto.getPrezzo());
		esistente.setQuantita(prodotto.getQuantita());

		return prodottoDao.save(esistente);
	}

	//Elimina prodotto
	public void eliminaProdotto(Long id)
	{
		if (!prodottoDao.existsById(id))
		{
			throw new RuntimeException("Prodotto con id: " + id + "non trovato");
		}
		prodottoDao.deleteById(id);
	}
}
