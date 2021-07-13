package br.com.pix.rest.chave.remocao

import br.com.pix.RemocaoChavePixServiceGrpc
import br.com.pix.RemoverChavePixRequest
import com.google.protobuf.Any
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.StatusProto
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.validation.Validated
import java.util.*
import javax.validation.Valid

@Controller("/api/v1/pix/keys")
@Validated
class RemocaoChaveController(private val gRPCClient: RemocaoChavePixServiceGrpc.RemocaoChavePixServiceBlockingStub) {

    @Delete
    fun removerChave(@Body @Valid request : RemocaoChaveDTORequest) : HttpResponse<Any> {

        val request : RemoverChavePixRequest = request.converterRequestGrpc()

        try {
            var response = gRPCClient.remover(request)

            //Em caso de sucesso, a chave Pix deve ser registrada e armazenada no sistema;
            return HttpResponse.ok()
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
