package br.com.pix.rest

import br.com.pix.ChavePIXServiceGrpc
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

}