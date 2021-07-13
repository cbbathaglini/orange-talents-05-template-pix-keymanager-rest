package br.com.pix.rest.chave.remocao

import br.com.pix.RegistraChavePixRequest
import br.com.pix.RemoverChavePixRequest
import br.com.pix.RemoverChavePixResponse
import br.com.pix.validation.PixKeyValidator
import br.com.pix.validation.UUIDValidator
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotNull


@Introspected
class RemocaoChaveDTORequest(
    @field:NotNull @field:UUIDValidator val pixId : String?,
    @field:NotNull @field:UUIDValidator val clienteId : String?
) {

    fun converterRequestGrpc(): RemoverChavePixRequest {
        return RemoverChavePixRequest.newBuilder()
            .setIdcliente(this.clienteId.toString() ?: "")
            .setPixId(this.pixId.toString() ?: "")
            .build()
    }
}