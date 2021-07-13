package br.com.pix.rest.chave.consulta

import br.com.pix.*
import com.google.protobuf.Timestamp
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
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(
    rollback = false, //default = true
    transactionMode = TransactionMode.SINGLE_TRANSACTION, //default = TransactionMode.SEPARATE_TRANSACTIONS
    transactional = false //default = true
)
internal class ConsultaChaveControllerTest{

    @field:Inject
    lateinit var grpcClient : ConsultaChavePixServiceGrpc.ConsultaChavePixServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var clientHttp: HttpClient


    @Singleton
    @Replaces(bean = ConsultaChavePixServiceGrpc.ConsultaChavePixServiceBlockingStub::class)
    fun criaMock() = Mockito.mock(ConsultaChavePixServiceGrpc.ConsultaChavePixServiceBlockingStub::class.java)



    @Test
    fun `aquele que consulta corretamente`(){
        val idCliente : String = "de95a228-1f27-4ad2-907e-e5a2d816e9bc"
        val idPix : String = "121b91af-0154-446d-a491-a42028244e1c"

        val respostaGrpc = montarResposta(idPix,idCliente)
        BDDMockito.given(grpcClient.consultar(Mockito.any())).willReturn(respostaGrpc)


        val request = HttpRequest.GET<Any>("/api/v1/clientes/${idCliente}/pix/keys/${idPix}")
        val response = clientHttp.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
    }


    private fun montarResposta(idPix:String, idCliente:String):ConsultarChavePixResponse{
        val localDateTime = LocalDateTime.now() //this can be any date


        val instant = localDateTime.toInstant(ZoneOffset.UTC)

        val timestamp: Timestamp = Timestamp.newBuilder()
            .setSeconds(instant.epochSecond)
            .setNanos(instant.nano)
            .build()
        return  ConsultarChavePixResponse.newBuilder()
            .setAgencia("0001")
            .setNumero("084329")
            .setTipoConta(TipoConta.CONTA_CORRENTE)
            .setTipoChave(TipoChave.RANDOM)
            .setChave("f7740c72-4c1f-4d6e-a967-27bb094ffb2a")
            .setNomeTitular("Cassio Almeida")
            .setCpfTitular("31643468081")
            .setNomeInstituicao("ITAU")
            .setPixId(idPix)
            .setIdcliente(idCliente)
            .setCriadaEm(timestamp)
            .build()
    }

    /*
    {
  "pixId": "121b91af-0154-446d-a491-a42028244e1c",
  "tipoChave": "RANDOM",
  "chave": "f7740c72-4c1f-4d6e-a967-27bb094ffb2a",
  "tipoConta": "CONTA_CORRENTE",
  "criadaEm": [
    2021,
    7,
    12,
    20,
    36,
    10,
    601525000
  ],
  "agencia": "0001",
  "numeroConta": "084329",
  "cpfTitular": "31643468081",
  "nomeTitular": "Cassio Almeida",
  "nomeInstituicao": "60701190"
}
     */



}