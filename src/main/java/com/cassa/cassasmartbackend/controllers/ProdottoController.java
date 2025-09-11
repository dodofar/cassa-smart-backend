package com.cassa.cassasmartbackend.controllers;

import com.cassa.cassasmartbackend.model.dtos.ProdottoDto;
import com.cassa.cassasmartbackend.model.dtos.mappers.ProdottoMapper;
import com.cassa.cassasmartbackend.model.entities.Prodotto;
import com.cassa.cassasmartbackend.model.dtos.service.ProdottoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prodotti")
@RequiredArgsConstructor
public class ProdottoController
{

	private final ProdottoService prodottoService;

	@GetMapping
	public List<ProdottoDto> getAllProdotti()
	{
		return prodottoService.listaProdotti().stream()
				.map(ProdottoMapper::toDTO)
				.toList();
	}

	@GetMapping("/{id}")
	public ProdottoDto getProdottoById(@PathVariable Long id)
	{
		Prodotto prodotto = prodottoService.getProdottoById(id);
		return ProdottoMapper.toDTO(prodotto);
	}

	@PostMapping
	public ProdottoDto creaProdotto(@RequestBody ProdottoDto dto)
	{
		Prodotto prodotto = ProdottoMapper.toEntity(dto);
		return ProdottoMapper.toDTO(prodottoService.creaProdotto(prodotto));
	}

	@PutMapping("/{id}")
	public ProdottoDto aggiornaProdotto(@PathVariable Long id, @RequestBody ProdottoDto dto)
	{
		Prodotto prodotto = ProdottoMapper.toEntity(dto);
		return ProdottoMapper.toDTO(prodottoService.aggiornaProdotto(id, prodotto));
	}

	@DeleteMapping("/{id}")
	public void eliminaProdotto(@PathVariable Long id)
	{
		prodottoService.eliminaProdotto(id);
	}
}
