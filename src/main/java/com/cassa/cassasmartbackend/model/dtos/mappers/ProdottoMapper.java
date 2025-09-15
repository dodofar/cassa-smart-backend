package com.cassa.cassasmartbackend.model.dtos.mappers;

import com.cassa.cassasmartbackend.model.dtos.ProdottoDto;
import com.cassa.cassasmartbackend.model.entities.Prodotto;

public class ProdottoMapper
{

	public static ProdottoDto toDTO(Prodotto prodotto)
	{
		return new ProdottoDto(
				prodotto.getId(),
				prodotto.getNome(),
				prodotto.getPrezzo(),
				prodotto.getQuantita()
		);
	}

	public static Prodotto toEntity(ProdottoDto dto)
	{
		Prodotto p = new Prodotto();
		p.setId(dto.id());
		p.setNome(dto.nome());
		p.setPrezzo(dto.prezzo());
		p.setQuantita(dto.quantita());
		return p;
	}
}
