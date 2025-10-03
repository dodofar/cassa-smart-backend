package com.cassa.cassasmartbackend.model.dtos;
// DTO per passare i dati degli item dell'ordine
public record OrdineItemDto(
		Long id,
		Long prodottoId,
		String nomeProdotto,
		Integer quantita,
		Double prezzoTotale
)
{
}
