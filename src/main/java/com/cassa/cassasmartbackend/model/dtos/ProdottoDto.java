package com.cassa.cassasmartbackend.model.dtos;

public record ProdottoDto(
		Long id,
		String nome,
		Double prezzo,
		int quantita
)
{
}
