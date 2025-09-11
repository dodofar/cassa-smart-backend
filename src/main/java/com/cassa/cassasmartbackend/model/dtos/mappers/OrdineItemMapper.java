package com.cassa.cassasmartbackend.model.dtos.mappers;

import com.cassa.cassasmartbackend.model.dtos.OrdineItemDto;
import com.cassa.cassasmartbackend.model.entities.OrdineItem;
import com.cassa.cassasmartbackend.model.entities.Prodotto;

public class OrdineItemMapper
{
	public static OrdineItemDto toDTO(OrdineItem entity)
	{
		return new OrdineItemDto(
				entity.getId(),
				entity.getProdotto().getId(),
				entity.getProdotto().getNome(),
				entity.getQuantita(),
				entity.getPrezzoTotale()
		);
	}

	public static OrdineItem toEntity(OrdineItemDto dto)
	{
		OrdineItem entity = new OrdineItem();
		entity.setId(dto.id());
		entity.setQuantita(dto.quantita());
		entity.setPrezzoTotale(dto.prezzoTotale());
		Prodotto prodotto = new Prodotto();
		prodotto.setId(dto.prodottoId());
		prodotto.setNome(dto.nomeProdotto());
		entity.setProdotto(prodotto);
		return entity;
	}
}
