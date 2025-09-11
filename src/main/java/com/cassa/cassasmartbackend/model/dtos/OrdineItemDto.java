package com.cassa.cassasmartbackend.model.dtos;

public record OrdineItemDto(
		Long id,
		Long prodottoId,
		String nomeProdotto,
		Integer quantita,
		Double prezzoTotale
)
{
}
