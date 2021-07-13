package br.com.pix.rest.chave.lista

import br.com.pix.ListarChavesPixResponse
import br.com.pix.TipoConta
import br.com.pix.rest.chave.TipoChave
import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Introspected
class ChaveResponse(
    chave: ListarChavesPixResponse.PixKey
) {
    val criadaEm = chave.registradoEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }
    val id = chave.idPix
    val tipoConta = chave.tipoConta
    val valorChave = chave.chave
    val tipoChave = chave.tipoChave


}