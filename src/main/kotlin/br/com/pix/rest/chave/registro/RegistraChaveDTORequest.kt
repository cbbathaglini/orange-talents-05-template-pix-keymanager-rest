package br.com.pix.rest.chave.registro

import br.com.pix.RegistraChavePixRequest
import br.com.pix.rest.chave.TipoChave
import br.com.pix.rest.chave.TipoConta
import br.com.pix.validation.PixKeyValidator
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@PixKeyValidator
@Introspected
class RegistraChaveDTORequest(@field:NotNull val tipoChave: TipoChave?,
                              @field:Size(max = 77) val valorChave: String?,
                              @field:NotNull val tipoConta: TipoConta?) {

    fun converterRequestGrpc(idCliente: UUID): RegistraChavePixRequest {
        var tipoChave = br.com.pix.TipoChave.valueOf(this.tipoChave.toString())
        if(this.tipoChave == null ){
           tipoChave = br.com.pix.TipoChave.INVALID_KEY_TYPE
        }
        var tipoConta = br.com.pix.TipoConta.valueOf(this.tipoConta.toString())
        if(this.tipoChave == null ){
            tipoConta = br.com.pix.TipoConta.INVALID_ACCOUNT_TYPE
        }
        return RegistraChavePixRequest.newBuilder()
            .setChave(this.valorChave ?: "")
            .setClientId(idCliente.toString())
            .setTipoDeChave(tipoChave)
            .setTipoDeConta(tipoConta)
            .build()
    }
}

