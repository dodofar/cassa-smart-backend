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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service // servizio che gestisce la logica degli ordini
@RequiredArgsConstructor // genera costruttore con i final
public class OrdineService
{
	private final OrdineDao ordineDao; // dao per gli ordini
	private final ProdottoDao prodottoDao; // dao per i prodotti

	//Lista ordini
	public List<Ordine> listaOrdini()
	{
		return ordineDao.findAll(); // prendo tutti gli ordini
	}

	//Crea ordine
	@Transactional // così salva tutto o niente
	public Ordine creaOrdine(List<OrdineItemDto> itemDtos)
	{
		if (itemDtos == null || itemDtos.isEmpty())
		{
			throw new IllegalArgumentException("L'ordine deve contenere almeno una riga");
		}
		Ordine ordine = new Ordine();
		ordine.setDataCreazione(LocalDateTime.now()); // data e ora ordine

		List<OrdineItem> items = new ArrayList<>();
		double totale = 0.0;

		for (OrdineItemDto dto : itemDtos)
		{
			if (dto.quantita() == null || dto.quantita() <= 0)
			{
				throw new IllegalArgumentException("La quantità deve essere maggiore di zero.");
			}

			Prodotto prodotto = prodottoDao.findById(dto.prodottoId())
					.orElseThrow(() -> new EntityNotFoundException("Prodotto con id " + dto.prodottoId() + " non trovato."));

			// controllo disponibilità prodotto
			Integer disponibile = prodotto.getQuantita();
			int richiesta = dto.quantita();
			if (disponibile == null || disponibile < richiesta)
			{
				throw new IllegalArgumentException("Quantità insufficiente per il prodotto '" + prodotto.getNome() + "': disponibili " + (disponibile == null ? 0 : disponibile) + ", richiesti " + richiesta + ".");
			}

			// aggiorno magazzino
			prodotto.setQuantita(disponibile - richiesta);
			prodottoDao.save(prodotto);

			// controllo prezzo
			Double prezzoUnitario = prodotto.getPrezzo();
			if (prezzoUnitario == null || prezzoUnitario <= 0)
			{
				throw new IllegalArgumentException("Prezzo non valido per il prodotto '" + prodotto.getNome() + "'. Impostare un prezzo > 0.");
			}

			// creo item dell'ordine
			OrdineItem item = new OrdineItem();
			item.setOrdine(ordine);
			item.setProdotto(prodotto);
			item.setQuantita(dto.quantita());
			item.setPrezzoTotale(prezzoUnitario * dto.quantita());

			totale += item.getPrezzoTotale(); // sommo al totale
			items.add(item);
		}
		if (totale <= 0)
		{
			throw new IllegalArgumentException("Il totale dell’ordine deve essere positivo.");
		}
		ordine.setItem(items); // associo gli item
		ordine.setTotale(totale); // imposto totale
		return ordineDao.save(ordine); // salvo ordine
	}

	//Aggiorna ordine
	public Ordine aggiornaOrdine(Long id, List<OrdineItemDto> itemDtos)
	{
		// non si può aggiornare perché c'è controllo giacenze
		throw new UnsupportedOperationException("Aggiornamento ordine non supportato quando è attivo il controllo delle giacenze. Annulla e ricrea l'ordine.");
	}

	//Cancellazione
	public void eliminaOrdine(Long id)
	{
		if (!ordineDao.existsById(id))
		{
			throw new EntityNotFoundException("Ordine con id" + id + " non trovato");
		}
		ordineDao.deleteById(id); // elimino ordine
	}

	//Calcolo statistiche
	private Map<String, Object> calcolaStatistiche(List<Ordine> ordini)
	{
		double totale = ordini.stream().mapToDouble(Ordine::getTotale).sum(); // somma dei totali
		long numeroOrdini = ordini.size(); // numero ordini
		double media = numeroOrdini > 0 ? totale / numeroOrdini : 0.0; // media ordini
		Map<String, Object> stats = new HashMap<>();
		stats.put("totaleIncassato", totale);
		stats.put("numeroOrdini", numeroOrdini);
		stats.put("mediaOrdini", media);
		return stats;
	}

	//Calcolo statistiche giornaliere
	public Map<String, Object> statisticheGiornaliere(LocalDate giorno)
	{
		if (giorno == null)
		{
			throw new IllegalArgumentException("Il giorno non può essere nullo.");
		}
		List<Ordine> ordini = ordineDao.findAll();
		List<Ordine> filtrati = ordini.stream()
				.filter(o -> o.getDataCreazione().toLocalDate().equals(giorno)) // filtro per data
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
						&& o.getDataCreazione().getMonthValue() == mese) // filtro anno+mese
				.toList();
		return calcolaStatistiche(filtrati);
	}

	//Calcolo statistiche annuali
	public Map<String, Object> statisticheAnnuali(int anno)
	{
		List<Ordine> ordini = ordineDao.findAll();
		List<Ordine> filtrati = ordini.stream()
				.filter(o -> o.getDataCreazione().getYear() == anno) // filtro anno
				.toList();
		return calcolaStatistiche(filtrati);
	}
}
