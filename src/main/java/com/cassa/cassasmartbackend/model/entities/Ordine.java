package com.cassa.cassasmartbackend.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ordine
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime dataCreazione;
	@OneToMany(mappedBy = "ordine", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrdineItem> item;
	private Double totale;
}
