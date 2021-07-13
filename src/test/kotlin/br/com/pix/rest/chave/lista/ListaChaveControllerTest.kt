package br.com.pix.rest.chave.lista

import br.com.pix.*
import br.com.pix.rest.chave.registro.RegistraChaveDTORequest
import com.google.protobuf.Timestamp
import io.grpc.Status
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
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import org.junit.jupiter.api.assertThrows

@MicronautTest(
    rollback = false, //default = true
    transactionMode = TransactionMode.SINGLE_TRANSACTION, //default = TransactionMode.SEPARATE_TRANSACTIONS
    transactional = false //default = true
)
internal class ListaChaveControllerTest{

    @field:Inject
    lateinit var grpcClient : ListaChavePixServiceGrpc.ListaChavePixServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var clientHttp: HttpClient


    @Singleton
    @Replaces(bean = ListaChavePixServiceGrpc.ListaChavePixServiceBlockingStub::class)
    fun criaMock() = Mockito.mock(ListaChavePixServiceGrpc.ListaChavePixServiceBlockingStub::class.java)



    @Test
    fun `aquele que lista as chaves`(){
        val idCliente : UUID = UUID.fromString("de95a228-1f27-4ad2-907e-e5a2d816e9bc")
        val listarChavesPixRequest = ListarChavesPixRequest.newBuilder()
            .setClienteId(idCliente.toString())
            .build()

        val respostaGrpc = ListarChavesPixResponse.newBuilder()
            .setIdCliente(idCliente.toString())
            .addAllChave(listaChaves())
            .build()

        BDDMockito.given(grpcClient.listar(Mockito.any())).willReturn(respostaGrpc)

        val request = HttpRequest.GET<Any>("/api/v1/clientes/${idCliente}/pix/keys")
        val response = clientHttp.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(respostaGrpc.chaveList.size, 3)
    }


    @Test
    fun `aquele que retorna uma collection vazia`(){
        val idCliente : UUID = UUID.fromString("ae93a61c-0642-43b3-bb8e-a17072295955")
        val listarChavesPixRequest = ListarChavesPixRequest.newBuilder()
            .setClienteId(idCliente.toString())
            .build()

        val respostaGrpc = ListarChavesPixResponse.newBuilder().build()

        BDDMockito.given(grpcClient.listar(Mockito.any())).willReturn(respostaGrpc)

        val request = HttpRequest.GET<Any>("/api/v1/clientes/${idCliente}/pix/keys")
        val response = clientHttp.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
    }




    private fun listaChaves(): List<ListarChavesPixResponse.PixKey>{
        val chave1 = ListarChavesPixResponse.PixKey.newBuilder()
            .setIdPix(UUID.randomUUID().toString())
            .setTipoChave(TipoChave.RANDOM)
            .setChave(UUID.randomUUID().toString())
            .setTipoConta(TipoConta.CONTA_POUPANCA)
            .setRegistradoEm(LocalDateTime.now().let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            })
            .build()

        val chave2 = ListarChavesPixResponse.PixKey.newBuilder()
            .setIdPix(UUID.randomUUID().toString())
            .setTipoChave(TipoChave.EMAIL)
            .setChave("email@email.com")
            .setTipoConta(TipoConta.CONTA_CORRENTE)
            .setRegistradoEm(LocalDateTime.now().let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            })
            .build()

        val chave3 = ListarChavesPixResponse.PixKey.newBuilder()
            .setIdPix(UUID.randomUUID().toString())
            .setTipoChave(TipoChave.PHONE)
            .setChave("51996489965")
            .setTipoConta(TipoConta.CONTA_CORRENTE)
            .setRegistradoEm(LocalDateTime.now().let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            })
            .build()


        return listOf(chave1,chave2,chave3)
    }


}