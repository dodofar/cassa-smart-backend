package com.cassa.cassasmartbackend.model.dtos.service;

import com.cassa.cassasmartbackend.model.daos.OrdineDao;
import com.cassa.cassasmartbackend.model.daos.ProdottoDao;
import com.cassa.cassasmartbackend.model.dtos.OrdineItemDto;
import com.cassa.cassasmartbackend.model.entities.Ordine;
import com.cassa.cassasmartbackend.model.entities.OrdineItem;
import com.cassa.cassasmartbackend.model.entities.Prodotto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrdineService
{
	private final OrdineDao ordineDao;
	private final ProdottoDao prodottoDao;

	//Lista ordini
	public List<Ordine> listaOrdini()
	{
		return ordineDao.findAll();
	}

	//Crea ordine
	public Ordine creaOrdine(List<OrdineItemDto> itemDtos)
	{
		if (itemDtos == null || itemDtos.isEmpty())
		{
			throw new IllegalArgumentException("L'ordine deve contenere almeno una riga");
		}
		Ordine ordine = new Ordine();
		ordine.setDataCreazione(LocalDateTime.now());

		List<OrdineItem> items = new ArrayList<>();
		double totale = 0.0;

		for (OrdineItemDto dto : itemDtos)
		{
			if (dto.quantita() == null || dto.quantita() <= 0)
			{
				throw new IllegalArgumentException("La quantità deve essere maggiore di zero.");
			}

			Prodotto prodotto = prodottoDao.findById(dto.prodottoId())
					.orElseThrow(() -> new EntityNotFoundException("Prodotto con id " + dto.prodottoId() + "non trovato."));

			OrdineItem item = new OrdineItem();
			item.setOrdine(ordine);
			item.setProdotto(prodotto);
			item.setQuantita(dto.quantita());
			item.setPrezzoTotale(prodotto.getPrezzo() * dto.quantita());

			totale += item.getPrezzoTotale();
			items.add(item);
		}
		if (totale <= 0)
		{
			throw new IllegalArgumentException("Il totale dell’ordine deve essere positivo.");
		}
		ordine.setItem(items);
		ordine.setTotale(totale);
		return ordineDao.save(ordine);
	}

	//Aggiorna ordine
	public Ordine aggiornaOrdine(Long id, List<OrdineItemDto> itemDtos)
	{
		Ordine ordine = ordineDao.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Ordine con id" + id + " non trovato"));
		if (itemDtos == null || itemDtos.isEmpty())
		{
			throw new IllegalArgumentException("Ordine non contenente item");
		}
		ordine.getItem().clear();

		List<OrdineItem> nuovoItem = new ArrayList<>();
		double totale = 0.0;

		for (OrdineItemDto dto : itemDtos)
		{
			if (dto.quantita() == null || dto.quantita() <= 0)
			{
				throw new IllegalArgumentException("Quantità minima: 1");
			}

			Prodotto prodotto = prodottoDao.findById(dto.prodottoId())
					.orElseThrow(() -> new EntityNotFoundException("Prodotto con id " + dto.prodottoId() + "non trovato"));

			OrdineItem item = new OrdineItem();
			item.setOrdine(ordine);
			item.setProdotto(prodotto);
			item.setQuantita(dto.quantita());
			item.setPrezzoTotale(prodotto.getPrezzo() * dto.quantita());

			totale += item.getPrezzoTotale();
			nuovoItem.add(item);
		}
		if (totale <= 0)
		{
			throw new IllegalArgumentException("Il totale minimo: >= 0");
		}
		ordine.setItem(nuovoItem);
		ordine.setTotale(totale);
		return ordineDao.save(ordine);
	}

	//Cancellazione
	public void eliminaOrdine(Long id)
	{
		if (!ordineDao.existsById(id))
		{
			throw new EntityNotFoundException("Ordine con id" + id + " non trovato");
		}
		ordineDao.deleteById(id);
	}

	//Calcolo statistiche
	private Map<String, Object> calcolaStatistiche(List<Ordine> ordini)
	{
		double totale = ordini.stream().mapToDouble(Ordine::getTotale).sum();
		long numeroOrdini = ordini.size();
		double media = numeroOrdini > 0 ? totale / numeroOrdini : 0.0;
		Map<String, Object> stats = new HashMap<>();
		stats.put("totaleIncassato", totale);
		stats.put("numeroOrdini", numeroOrdini);
		stats.put("mediaOrdini", media);
		return stats;
	}

	//Calclo statistiche giornaliere
	public Map<String, Object> statisticheGiornaliere(LocalDate giorno)
	{
		if (giorno == null)
		{
			throw new IllegalArgumentException("Il giorno non può essere nullo.");
		}
		List<Ordine> ordini = ordineDao.findAll();
		List<Ordine> filtrati = ordini.stream()
				.filter(o -> o.getDataCreazione().toLocalDate().equals(giorno))
				.toList();
		return calcolaStatistiche(filtrati);
	}

	//Calcolo statistiche mensili
	public Map<String, Object> statisticheMensili(int anno, int mese)
	{
		if (mese < 1 || mese > 12)
		{
			throw new IllegalArgumentException("Il mese deve essere compreso tra 1 e 12.");
		}
		List<Ordine> ordini = ordineDao.findAll();
		List<Ordine> filtrati = ordini.stream()
				.filter(o -> o.getDataCreazione().getYear() == anno
						&& o.getDataCreazione().getMonthValue() == mese)
				.toList();
		return calcolaStatistiche(filtrati);
	}

	//Calcolo statistiche annuali
	public Map<String, Object> statisticheAnnuali(int anno)
	{
		List<Ordine> ordini = ordineDao.findAll();
		List<Ordine> filtrati = ordini.stream()
				.filter(o -> o.getDataCreazione().getYear() == anno)
				.toList();
		return calcolaStatistiche(filtrati);
	}
}
