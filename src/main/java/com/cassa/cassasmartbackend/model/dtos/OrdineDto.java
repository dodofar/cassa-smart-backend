package com.cassa.cassasmartbackend.model.dtos;

import java.time.LocalDateTime;
import java.util.List;
// DTO per passare i dati dell'ordine
public record OrdineDto(
		Long id,
		LocalDateTime dataCreazione,
		Double totale,
		List<OrdineItemDto> item
)
{
}
