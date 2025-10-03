package com.cassa.cassasmartbackend.controllers;

import com.cassa.cassasmartbackend.model.dtos.ProdottoDto;
import com.cassa.cassasmartbackend.model.dtos.mappers.ProdottoMapper;
import com.cassa.cassasmartbackend.model.entities.Prodotto;
import com.cassa.cassasmartbackend.model.dtos.service.ProdottoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // controller rest
@RequestMapping("/prodotti") // rotta base
@RequiredArgsConstructor // costruttore con service
public class ProdottoController
{

	private final ProdottoService prodottoService; // service prodotti

	@GetMapping // prendi tutti i prodotti
	public List<ProdottoDto> getAllProdotti()
	{
		return prodottoService.listaProdotti().stream()
				.map(ProdottoMapper::toDTO) // entity -> dto
				.toList();
	}

	@GetMapping("/{id}") // prendi un prodotto per id
	public ProdottoDto getProdottoById(@PathVariable Long id)
	{
		Prodotto prodotto = prodottoService.getProdottoById(id);
		return ProdottoMapper.toDTO(prodotto);
	}

	@PostMapping // crea prodotto
	public ProdottoDto creaProdotto(@RequestBody ProdottoDto dto)
	{
		Prodotto prodotto = ProdottoMapper.toEntity(dto);
		return ProdottoMapper.toDTO(prodottoService.creaProdotto(prodotto));
	}

	@PutMapping("/{id}") // aggiorna prodotto
	public ProdottoDto aggiornaProdotto(@PathVariable Long id, @RequestBody ProdottoDto dto)
	{
		Prodotto prodotto = ProdottoMapper.toEntity(dto);
		return ProdottoMapper.toDTO(prodottoService.aggiornaProdotto(id, prodotto));
	}

	@DeleteMapping("/{id}") // elimina prodotto
	public void eliminaProdotto(@PathVariable Long id)
	{
		prodottoService.eliminaProdotto(id);
	}
}
