package noemicoppotelli.ariadne.service;

import noemicoppotelli.ariadne.entities.MappaConcettuale;
import noemicoppotelli.ariadne.entities.Utente;
import noemicoppotelli.ariadne.payloads.MappaResponse;
import noemicoppotelli.ariadne.repositories.MappaConcettualeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import noemicoppotelli.ariadne.exceptions.BadRequestException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class MappaServiceTest {

    @Mock
    private AnthropicClient anthropicClient;

    @Mock
    private MappaConcettualeRepository mappaRepository;

    @InjectMocks
    private MappaService mappaService;

    @Test
    void generaConRispostaPulitaSalvaLaMappaCorrettamente() {
        String mermaidPulito = "mindmap\n  root((Fotosintesi))\n    Luce solare\n    Acqua";
        when(anthropicClient.generaTesto(any(), any())).thenReturn(mermaidPulito);
        when(mappaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Utente utente = new Utente(1L, "test@example.com", "hash-finto", LocalDateTime.now());

        MappaResponse response = mappaService.genera("La fotosintesi...", utente);

        assertThat(response.getContenutoMermaid()).isEqualTo(mermaidPulito);
        verify(mappaRepository, times(1)).save(any(MappaConcettuale.class));
    }
    @Test
    void generaConCodeFenceMarkdownLoRipulisceCorrettamente() {
        String rispostaConFence = "```mermaid\nmindmap\n  root((Fotosintesi))\n    Luce solare\n```";
        String mermaidAtteso = "mindmap\n  root((Fotosintesi))\n    Luce solare";

        when(anthropicClient.generaTesto(any(), any())).thenReturn(rispostaConFence);
        when(mappaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Utente utente = new Utente(1L, "test@example.com", "hash-finto", LocalDateTime.now());

        MappaResponse response = mappaService.genera("La fotosintesi...", utente);

        assertThat(response.getContenutoMermaid()).isEqualTo(mermaidAtteso);
    }
    @Test
    void generaConRispostaNonMermaidLanciaEccezioneENonSalvaNulla() {
        String rispostaNonValida = "Mi dispiace, non posso aiutarti con questa richiesta.";

        when(anthropicClient.generaTesto(any(), any())).thenReturn(rispostaNonValida);

        Utente utente = new Utente(1L, "test@example.com", "hash-finto", LocalDateTime.now());

        assertThatThrownBy(() -> mappaService.genera("testo qualsiasi", utente))
                .isInstanceOf(BadRequestException.class);

        verify(mappaRepository, never()).save(any());
    }
    @Test
    void generaConEccezioneDiReteLaPropagaENonSalvaNulla() {
        when(anthropicClient.generaTesto(any(), any()))
                .thenThrow(new BadRequestException("Generazione mappa fallita: connessione rifiutata"));

        Utente utente = new Utente(1L, "test@example.com", "hash-finto", LocalDateTime.now());

        assertThatThrownBy(() -> mappaService.genera("testo qualsiasi", utente))
                .isInstanceOf(BadRequestException.class);

        verify(mappaRepository, never()).save(any());
    }
}

