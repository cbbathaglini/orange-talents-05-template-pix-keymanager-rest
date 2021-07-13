package br.com.pix.rest.chave.remocao

import br.com.pix.*
import br.com.pix.rest.chave.registro.RegistraChaveDTORequest
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.TransactionMode
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(
    rollback = false, //default = true
    transactionMode = TransactionMode.SINGLE_TRANSACTION, //default = TransactionMode.SEPARATE_TRANSACTIONS
    transactional = false //default = true
)
internal class RemocaoChaveControllerTest{

    @field:Inject
    lateinit var grpcClient : RemocaoChavePixServiceGrpc.RemocaoChavePixServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var clientHttp: HttpClient


    @Singleton
    @Replaces(bean = RemocaoChavePixServiceGrpc.RemocaoChavePixServiceBlockingStub::class)
    fun criaMock() = Mockito.mock(RemocaoChavePixServiceGrpc.RemocaoChavePixServiceBlockingStub::class.java)



    @Test
    fun `aquele que remove uma nova chave pix`(){
        val idCliente : String = "de95a228-1f27-4ad2-907e-e5a2d816e9bc"
        val idPix : String = "0af114a1-c647-44d3-9eb8-321e0e4daca0"
        val removerChavePixRequest = RemoverChavePixRequest.newBuilder()
            .setIdcliente(idCliente)
            .setPixId( idPix)
            .build()

        val remocaoChaveDTORequest = RemocaoChaveDTORequest(
            pixId = idPix,
            clienteId = idCliente)

        val respostaGrpc = RemoverChavePixResponse.newBuilder()
            .setIdcliente(idCliente)
            .setPixId( idPix)
            .build()


        BDDMockito.given(grpcClient.remover(Mockito.any())).willReturn(respostaGrpc)

        val request = HttpRequest.DELETE("/api/v1/pix/keys", remocaoChaveDTORequest)
        val response = clientHttp.toBlocking().exchange(request, RemocaoChaveDTORequest::class.java)

        assertEquals(HttpStatus.OK, response.status)
    }




}