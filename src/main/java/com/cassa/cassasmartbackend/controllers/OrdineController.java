package com.cassa.cassasmartbackend.controllers;


import com.cassa.cassasmartbackend.model.daos.OrdineDao;
import com.cassa.cassasmartbackend.model.entities.Ordine;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordini")
public class OrdineController
{

	@Autowired
	private OrdineDao ordineDao;

	@GetMapping
	public List<Ordine> getAllOrdini()
	{
		return ordineDao.findAll();
	}

	@GetMapping("/{id}")
	public Ordine getOrdineById(@PathVariable Long id)
	{
		return ordineDao.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Ordine non trovato con id " + id));
	}

	@PostMapping
	public Ordine creaOrdine(@RequestBody Ordine ordine)
	{
		return ordineDao.save(ordine);
	}

	@PutMapping("/{id}")
	public Ordine aggiornaOrdine(@PathVariable Long id, @RequestBody Ordine ordine)
	{
		Ordine esistente = ordineDao.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Ordine non trovato con id " + id));
		esistente.setDataCreazione(ordine.getDataCreazione());
		esistente.setTotale(ordine.getTotale());
		esistente.setItem(ordine.getItem());
		return ordineDao.save(esistente);
	}

	@DeleteMapping("/{id}")
	public void eliminaOrdine(@PathVariable Long id)
	{
		if (!ordineDao.existsById(id))
		{
			throw new EntityNotFoundException("Ordine non trovato con id " + id);
		}
		ordineDao.deleteById(id);
	}
}
