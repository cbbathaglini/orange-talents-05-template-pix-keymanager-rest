package br.com.pix.rest

import br.com.pix.ChavePIXServiceGrpc
import br.com.pix.ConsultaChavePixServiceGrpc
import br.com.pix.ListaChavePixServiceGrpc
import br.com.pix.RemocaoChavePixServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton


@Factory
class GrpcClientFactory(@GrpcChannel("desafioPixrest") val channel: ManagedChannel) {

    @Singleton
    fun registraChaveStub() : ChavePIXServiceGrpc.ChavePIXServiceBlockingStub {
        return ChavePIXServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun removeChaveStub() : RemocaoChavePixServiceGrpc.RemocaoChavePixServiceBlockingStub {
        return RemocaoChavePixServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun consultaChaveStub() : ConsultaChavePixServiceGrpc.ConsultaChavePixServiceBlockingStub {
        return ConsultaChavePixServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun listaChaveStub() : ListaChavePixServiceGrpc.ListaChavePixServiceBlockingStub {
        return ListaChavePixServiceGrpc.newBlockingStub(channel)
    }

}