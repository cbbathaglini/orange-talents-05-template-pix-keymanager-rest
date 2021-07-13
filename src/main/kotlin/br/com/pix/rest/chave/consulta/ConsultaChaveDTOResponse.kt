package br.com.pix.rest.chave.consulta

import br.com.pix.ConsultarChavePixResponse
import br.com.pix.rest.chave.TipoConta
import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Introspected
class ConsultaChaveDTOResponse(response:ConsultarChavePixResponse ) {

    val pixId = response.pixId
    val tipoChave = response.tipoChave.toString()
    val chave = response.chave
    val tipoConta = response.tipoConta.toString()
    val criadaEm = response.criadaEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }
    val agencia = response.agencia
    val numeroConta = response.numero
    val cpfTitular = response.cpfTitular
    val nomeTitular = response.nomeTitular
    val nomeInstituicao = response.nomeInstituicao


}