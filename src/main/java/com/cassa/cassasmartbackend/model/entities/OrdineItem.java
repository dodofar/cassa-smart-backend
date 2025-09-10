package com.cassa.cassasmartbackend.model.entities;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdineItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "ordine_id")
	private Ordine ordine;
	@ManyToOne
	@JoinColumn(name = "prodotto_id")
	private Prodotto prodotto;
	private Integer quantita;
	private Double prezzoTotale;
}
