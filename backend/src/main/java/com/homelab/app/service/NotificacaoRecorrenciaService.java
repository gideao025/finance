package com.homelab.app.service;

import com.homelab.app.model.Notificacao;
import com.homelab.app.model.TransacaoRecorrente;
import com.homelab.app.repository.NotificacaoRepository;
import com.homelab.app.repository.TransacaoRecorrenteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacaoRecorrenciaService {

    private final TransacaoRecorrenteRepository recorrenciaRepository;
    private final NotificacaoRepository notificacaoRepository;

    /**
     * Verificar recorrências próximas do vencimento
     * Executa diariamente às 08:00
     */
    @Scheduled(cron = "0 0 8 * * *")
    public void verificarRecorrenciasProximas() {
        log.info("Verificando recorrências próximas do vencimento...");

        LocalDate daqui3Dias = LocalDate.now().plusDays(3);

        List<TransacaoRecorrente> recorrenciasProximas = recorrenciaRepository
                .findRecorrenciasParaProcessar(daqui3Dias);

        for (TransacaoRecorrente recorrencia : recorrenciasProximas) {
            criarNotificacaoRecorrencia(recorrencia);
        }

        log.info("Verificação de recorrências concluída");
    }

    private void criarNotificacaoRecorrencia(TransacaoRecorrente recorrencia) {
        // Verificar se já existe notificação para esta recorrência
        boolean jaNotificado = notificacaoRepository
                .existsByReferenceIdAndTipo(recorrencia.getId(), "RECORRENCIA_PROXIMA");

        if (!jaNotificado) {
            Notificacao notificacao = new Notificacao();
            notificacao.setTitulo("Transação Recorrente Próxima");
            notificacao.setMensagem(String.format(
                    "A transação '%s' de R$ %.2f será processada em %s",
                    recorrencia.getDescricao(),
                    recorrencia.getValor(),
                    recorrencia.getProximaExecucao()
            ));
            notificacao.setUsuario(recorrencia.getUsuario());
            notificacao.setTipo("RECORRENCIA_PROXIMA");
            notificacao.setReferenceId(recorrencia.getId());

            notificacaoRepository.save(notificacao);

            log.info("Notificação criada para recorrência: {}",
                    recorrencia.getDescricao());
        }
    }
}