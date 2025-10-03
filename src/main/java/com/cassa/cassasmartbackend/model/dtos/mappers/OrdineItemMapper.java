package com.cassa.cassasmartbackend.model.dtos.mappers;

import com.cassa.cassasmartbackend.model.dtos.OrdineItemDto;
import com.cassa.cassasmartbackend.model.entities.OrdineItem;
import com.cassa.cassasmartbackend.model.entities.Prodotto;

public class OrdineItemMapper
{
	// converto da entity -> dto
	public static OrdineItemDto toDTO(OrdineItem entity)
	{
		return new OrdineItemDto(
				entity.getId(),
				entity.getProdotto().getId(),   // prendo id prodotto
				entity.getProdotto().getNome(), // prendo nome prodotto
				entity.getQuantita(),
				entity.getPrezzoTotale()
		);
	}

	// converto da dto -> entity
	public static OrdineItem toEntity(OrdineItemDto dto)
	{
		OrdineItem entity = new OrdineItem();
		entity.setId(dto.id());
		entity.setQuantita(dto.quantita());
		entity.setPrezzoTotale(dto.prezzoTotale());

		// creo un prodotto solo con i dati minimi (id + nome)
		Prodotto prodotto = new Prodotto();
		prodotto.setId(dto.prodottoId());
		prodotto.setNome(dto.nomeProdotto());

		// collego il prodotto all'item
		entity.setProdotto(prodotto);

		return entity;
	}
}
