package com.cassa.cassasmartbackend.model.dtos.mappers;

import com.cassa.cassasmartbackend.model.dtos.OrdineDto;
import com.cassa.cassasmartbackend.model.dtos.OrdineItemDto;
import com.cassa.cassasmartbackend.model.entities.Ordine;

import java.util.List;
import java.util.stream.Collectors;

public class OrdineMapper
{
	// converto da entity Ordine -> OrdineDto
	public static OrdineDto toDTO(Ordine ordine)
	{
		// trasformo ogni OrdineItem in OrdineItemDto
		List<OrdineItemDto> items = ordine.getItem().stream()
				.map(r -> new OrdineItemDto(
						r.getId(),
						r.getProdotto().getId(),
						r.getProdotto().getNome(),
						r.getQuantita(),
						r.getPrezzoTotale()
				))
				.collect(Collectors.toList());

		// creo il dto finale con lista di item dentro
		return new OrdineDto(
				ordine.getId(),
				ordine.getDataCreazione(),
				ordine.getTotale(),
				items
		);
	}
}
