package com.cassa.cassasmartbackend.model.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record OrdineDto(
		Long id,
		LocalDateTime dataCreazione,
		Double totale,
		List<OrdineItemDto> item
) {}
