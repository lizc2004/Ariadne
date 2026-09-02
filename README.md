# Ariadne — Backend

API REST per Ariadne, un'app di supporto allo studio. Riscrittura in Spring Boot di un progetto personale nato originariamente come app vanilla JS.

Frontend: [repository separata](https://github.com/lizc2004/Ariadne_frontend), React + Vite.

---

## Cosa fa

- **Task** — materia, scadenza, priorità, completamento.
- **Flashcard con ripetizione spaziata** — mazzi e carte con algoritmo SM-2 personalizzato, import in blocco (`fronte|retro` riga per riga).
- **Sessioni di studio / Timer Pomodoro** — minuti calcolati come differenza reale tra inizio e fine.
- **Condivisione con consenso** — un utente può richiedere di vedere i progressi di un altro; il proprietario dei dati deve accettare esplicitamente (`RICHIESTO → ACCETTATO/RIFIUTATO`, revocabile).

---

## Stack tecnologico

- Java 26, Spring Boot 4.1.0
- Spring Security 7.1.0 + JJWT 0.12.6 (JWT)
- Hibernate 7.4.1 / Spring Data JPA
- PostgreSQL
- Maven

---

## Autenticazione

JWT stateless a due token: access token (10 minuti) per le richieste, refresh token (7 giorni, salvato come hash) per rinnovarlo.

| Metodo | Endpoint | Descrizione |
|---|---|---|
| POST | `/api/auth/register` | Registrazione (email + password) |
| POST | `/api/auth/login` | Login, restituisce access + refresh token |
| POST | `/api/auth/refresh` | Rinnova l'access token |
| POST | `/api/auth/logout` | Revoca il refresh token |

---

## Algoritmo SM-2 (variante personalizzata)

Non è l'SM-2 da manuale: è una variante a 4 livelli di valutazione (`NON_RICORDO`, `DIFFICILE`, `GIUSTO`, `FACILE`) progettata per la versione originale dell'app e già validata con uso reale. Logica in `CardService.valutaCarta`.

---

## Roadmap

- Mappe concettuali AI-assistite (entità `MappaConcettuale` già presente, endpoint da implementare)
- Deploy in produzione
