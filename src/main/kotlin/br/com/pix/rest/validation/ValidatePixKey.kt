package br.com.pix.validation


import br.com.pix.rest.chave.registro.RegistraChaveDTORequest
import javax.inject.Singleton

@Singleton
class ValidatePixKey: javax.validation.ConstraintValidator<PixKeyValidator, RegistraChaveDTORequest> {

    override fun isValid(pix: RegistraChaveDTORequest?, context: javax.validation.ConstraintValidatorContext): Boolean {

        if (pix?.tipoChave == null) {
            return true
        }
        val ret = pix.tipoChave.valida(pix.valorChave!!)
        return pix.tipoChave.valida(pix.valorChave!!)
    }
}