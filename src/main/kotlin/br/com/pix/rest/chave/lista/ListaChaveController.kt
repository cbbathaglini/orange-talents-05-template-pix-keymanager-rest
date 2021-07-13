package br.com.pix.rest.chave.lista

import br.com.pix.ConsultaChavePixServiceGrpc
import br.com.pix.ConsultarChavePixRequest
import br.com.pix.ListaChavePixServiceGrpc
import br.com.pix.ListarChavesPixRequest
import br.com.pix.rest.chave.consulta.ConsultaChaveDTOResponse
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.validation.Validated

@Controller("/api/v1/clientes/{idCliente}/pix/keys")
@Validated
class ListaChaveController(private val gRPCClient: ListaChavePixServiceGrpc.ListaChavePixServiceBlockingStub) {


    @Get
    fun listar(@PathVariable(value="idCliente") idCliente : String) : HttpResponse<Any> {

        val request : ListarChavesPixRequest = ListarChavesPixRequest.newBuilder()
            .setClienteId(idCliente)
            .build()

        try {
            var response = gRPCClient.listar(request)
            return HttpResponse.ok(response.chaveList.map{ChaveResponse(it)})

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