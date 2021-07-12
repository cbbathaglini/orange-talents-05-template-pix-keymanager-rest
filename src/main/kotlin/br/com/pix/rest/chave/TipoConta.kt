package br.com.pix.rest.chave

enum class TipoConta {
    CONTA_POUPANCA {
        override fun converteIngles(): String{
          return  "SVGS"
        }

    }, // SVGS=Conta Poupança
    CONTA_CORRENTE{
        override fun converteIngles(): String{
            return  "CACC"
        }


    }, // CACC=Conta Corrente;
    INVALID_ACCOUNT_TYPE{
        override fun converteIngles(): String{
            return  ""
        }


    };

    abstract fun converteIngles(): String
}