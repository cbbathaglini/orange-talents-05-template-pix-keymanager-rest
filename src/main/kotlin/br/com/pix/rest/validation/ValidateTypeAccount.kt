package br.com.pix.validation

import br.com.pix.rest.chave.registro.RegistraChaveDTORequest
import br.com.pix.rest.chave.TipoConta
import javax.inject.Singleton


@Singleton
class ValidateTypeAccount: javax.validation.ConstraintValidator<TypeAccountValidator, RegistraChaveDTORequest> {

    override fun isValid(pix: RegistraChaveDTORequest?, context: javax.validation.ConstraintValidatorContext): Boolean {

        if (pix?.tipoConta == null) {
            return false
        }

        val ret : Boolean = pix.tipoConta != TipoConta.INVALID_ACCOUNT_TYPE
        return pix.tipoConta != TipoConta.INVALID_ACCOUNT_TYPE
    }
}