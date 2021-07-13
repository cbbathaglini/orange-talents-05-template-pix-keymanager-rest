package br.com.pix.rest.chave.registro

import br.com.pix.*
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.annotation.TransactionMode
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import org.mockito.BDDMockito.given
import javax.inject.Singleton

@MicronautTest(
    rollback = false, //default = true
    transactionMode = TransactionMode.SINGLE_TRANSACTION, //default = TransactionMode.SEPARATE_TRANSACTIONS
    transactional = false //default = true
)
internal class RegistraChaveControllerTest{

    @field:Inject
    lateinit var grpcClient : ChavePIXServiceGrpc.ChavePIXServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var clientHttp: HttpClient


    @Singleton
    @Replaces(bean = ChavePIXServiceGrpc.ChavePIXServiceBlockingStub::class)
    fun criaMock() = Mockito.mock(ChavePIXServiceGrpc.ChavePIXServiceBlockingStub::class.java)



    @Test
    fun `aquele que cadastra uma nova chave pix`(){
        val idCliente : UUID = UUID.fromString("de95a228-1f27-4ad2-907e-e5a2d816e9bc")
        val registraChaveRequestGrpc = RegistraChavePixRequest.newBuilder()
            .setChave(UUID.randomUUID().toString())
            .setTipoDeChave(TipoChave.RANDOM)
            .setClientId(idCliente.toString())
            .setTipoDeConta(TipoConta.CONTA_CORRENTE)
            .build()


        val registraChaveDTORequest = RegistraChaveDTORequest(
            tipoChave = br.com.pix.rest.chave.TipoChave.RANDOM,
            valorChave =registraChaveRequestGrpc.chave,
            tipoConta = br.com.pix.rest.chave.TipoConta.CONTA_CORRENTE)

        val respostaGrpc = RegistraChavePixResponse.newBuilder()
            .setChave(registraChaveDTORequest.valorChave)
            .setPixId( UUID.randomUUID().toString())
            .build()

        try {
            given(grpcClient.salvarChave(Mockito.any())).willReturn(respostaGrpc)
//            Mockito.`when`(grpcClient.salvarChave(registraChaveRequestGrpc))
//                .thenReturn(respostaGrpc)
            //given(grpcClient.salvarChave(Mockito.any())).willReturn(respostaGrpc)
            //given(grpcClient.salvarChave(registraChaveRequestGrpc)).willReturn()
            //Mockito.doReturn(respostaGrpc).`when`(grpcClient).salvarChave(registraChaveRequestGrpc)
        }catch (e:StatusRuntimeException){
            println(e)
        }
        val request = HttpRequest.POST("/api/v1/clientes/${idCliente}/registra/pix", registraChaveDTORequest)
        val response = clientHttp.toBlocking().exchange(request, RegistraChaveDTORequest::class.java)

        assertEquals(HttpStatus.OK, response.status)
    }




}