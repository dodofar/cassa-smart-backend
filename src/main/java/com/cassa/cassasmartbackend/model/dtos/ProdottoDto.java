package com.cassa.cassasmartbackend.model.dtos;
//DTO per passare i dati dei prodotti
public record ProdottoDto(
		Long id,
		String nome,
		Double prezzo,
		int quantita
)
{
}
