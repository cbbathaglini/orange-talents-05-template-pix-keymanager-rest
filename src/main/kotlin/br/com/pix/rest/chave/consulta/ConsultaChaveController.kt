package br.com.pix.rest.chave.consulta

import br.com.pix.*
import br.com.pix.rest.chave.remocao.RemocaoChaveDTORequest
import com.google.protobuf.Any
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.validation.Validated
import java.util.*
import javax.validation.Valid


@Controller("/api/v1/clientes/{idCliente}/pix/keys/")
@Validated
class ConsultaChaveController(private val gRPCClient: ConsultaChavePixServiceGrpc.ConsultaChavePixServiceBlockingStub) {

    //precisamos somente consultar uma chave Pix pelo Pix ID e identificador do cliente
    @Get(value = "/{pixId}")
    fun consultar(@PathVariable(value="idCliente") idCliente : String,
                  @PathVariable(value="pixId") pixId : String) : HttpResponse<ConsultaChaveDTOResponse> {

        val request : ConsultarChavePixRequest = ConsultarChavePixRequest.newBuilder()
            .setPixId(pixId)
            .setIdcliente(idCliente)
            .build()

        try {
            var response = gRPCClient.consultar(request)

            //Em caso de sucesso, a chave Pix deve ser registrada e armazenada no sistema;
//            return HttpResponse.ok(mapOf(
//                "pixId" to response.pixId,
//                "idCliente" to response.idcliente
//            ))
            return HttpResponse.ok( ConsultaChaveDTOResponse(response))

        }catch (e: StatusRuntimeException) {
            val statusCode = e.status.code
            val description = e.status.description

            //Em caso de chave não encontrada, deve-se retornar status de erro 404-NOT_FOUND com uma mensagem amigável para o usuário;
            if (statusCode == Status.Code.NOT_FOUND) {
                throw HttpStatusException(HttpStatus.NOT_FOUND, description)
            }

            //caso contrário é um erro inesperado
            throw HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message) //code + description
        }

    }


}