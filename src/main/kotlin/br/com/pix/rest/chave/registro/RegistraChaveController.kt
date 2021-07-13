package br.com.pix.rest.chave.registro

import br.com.pix.ChavePIXServiceGrpc
import br.com.pix.RegistraChavePixRequest
import br.com.pix.RegistraChavePixResponse
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
import javax.inject.Inject
import javax.validation.Valid


@Controller("/api/v1/clientes/{idCliente}")
@Validated
class RegistraChaveController(private val gRPCClient: ChavePIXServiceGrpc.ChavePIXServiceBlockingStub) {

    @Post("/registra/pix")
    fun salvarChave(@PathVariable(value="idCliente") idCliente : UUID,
                 @Body @Valid request : RegistraChaveDTORequest
    ) : HttpResponse<Any> {

        val request : RegistraChavePixRequest = request.converterRequestGrpc(idCliente)

        try {
            var response = gRPCClient.salvarChave(request)

            //Em caso de sucesso, a chave Pix deve ser registrada e armazenada no sistema;
            return HttpResponse.ok()
        }catch (e: StatusRuntimeException) {
            val statusCode = e.status.code
            val description = e.status.description

            //Em caso de chave já existente, deve-se retornar o status de erro 422-UNPROCESSABLE_ENTITY com uma mensagem amigável para o usuário;
            if (statusCode == Status.Code.ALREADY_EXISTS) {
                throw HttpStatusException(HttpStatus.UNPROCESSABLE_ENTITY, description)
            }

            if (statusCode == Status.Code.INVALID_ARGUMENT) {
                throw HttpStatusException(HttpStatus.BAD_REQUEST, description)
            }

            if (statusCode == Status.Code.PERMISSION_DENIED) {
                val statusProto = StatusProto.fromThrowable(e)
                if (statusProto == null) {
                    throw HttpStatusException(HttpStatus.FORBIDDEN, description)
                }
            }

            //caso contrário é um erro inesperado
            throw HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message) //code + description
        }

    }


}
