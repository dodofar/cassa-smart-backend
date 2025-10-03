package com.cassa.cassasmartbackend.controllers;

import com.cassa.cassasmartbackend.model.dtos.OrdineDto;
import com.cassa.cassasmartbackend.model.dtos.OrdineItemDto;
import com.cassa.cassasmartbackend.model.dtos.mappers.OrdineMapper;
import com.cassa.cassasmartbackend.model.entities.Ordine;
import com.cassa.cassasmartbackend.model.dtos.service.OrdineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordini")
@RequiredArgsConstructor
public class OrdineController
{

	private final OrdineService ordineService; // uso il service per la logica

	@GetMapping
	public List<OrdineDto> getAllOrdini()
	{
		// prendo tutti gli ordini e li converto in dto
		return ordineService.listaOrdini().stream()
				.map(OrdineMapper::toDTO)
				.toList();
	}

	@GetMapping("/{id}")
	public OrdineDto getOrdineById(@PathVariable Long id)
	{
		// cerco ordine per id (al momento filtrando da lista)
		Ordine ordine = ordineService.listaOrdini().stream()
				.filter(o -> o.getId().equals(id))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("Ordine non trovato con id " + id));
		return OrdineMapper.toDTO(ordine);
	}

	@PostMapping
	public OrdineDto creaOrdine(@RequestBody List<OrdineItemDto> itemDtos)
	{
		// creo un ordine nuovo partendo dalla lista di item
		Ordine ordine = ordineService.creaOrdine(itemDtos);
		return OrdineMapper.toDTO(ordine);
	}

	@PutMapping("/{id}")
	public OrdineDto aggiornaOrdine(@PathVariable Long id, @RequestBody List<OrdineItemDto> itemDtos)
	{
		// aggiorno l’ordine (per ora in service non supportato)
		Ordine ordine = ordineService.aggiornaOrdine(id, itemDtos);
		return OrdineMapper.toDTO(ordine);
	}

	@DeleteMapping("/{id}")
	public void eliminaOrdine(@PathVariable Long id)
	{
		// elimino ordine con id dato
		ordineService.eliminaOrdine(id);
	}

	@GetMapping("/statistiche/giornaliere")
	public Map<String, Object> statisticheGiornaliere(@RequestParam String giorno)
	{
		// statistiche filtrate per giorno
		return ordineService.statisticheGiornaliere(LocalDate.parse(giorno));
	}

	@GetMapping("/statistiche/mensili")
	public Map<String, Object> statisticheMensili(@RequestParam int anno, @RequestParam int mese)
	{
		// statistiche per anno + mese
		return ordineService.statisticheMensili(anno, mese);
	}

	@GetMapping("/statistiche/annuali")
	public Map<String, Object> statisticheAnnuali(@RequestParam int anno)
	{
		// statistiche solo per anno
		return ordineService.statisticheAnnuali(anno);
	}
}
