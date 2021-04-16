package com.example.demo.redis

import org.slf4j.LoggerFactory
import java.util.*

class CacheExceptionHandlerExtension<KeyType, Type>(
        private val findFunction: (chave: KeyType) -> Optional<Type>,
        private val saveFunction: (objeto: Type) -> Type
) {

    private val LOGGER = LoggerFactory.getLogger(CacheExceptionHandlerExtension::class.java)

    var customMessage = ""
    fun find(chave: KeyType): Optional<Type> {
        return try {
            findFunction(chave)
        } catch (erro: Exception) {
            LOGGER.error("Exception ao consultar o cache [${chave.toString()}] - $customMessage - ${erro.message}")
            Optional.empty()
        }
    }

    fun save(objeto: Type) {
        try {
            saveFunction(objeto)
        } catch (erro: Exception) {
            LOGGER.error("Exception ao salvar no cache [${objeto.toString()}] - $customMessage - ${erro.message}")
        }
    }

    fun delete(deleteFunction: (chave: KeyType) -> Unit, chave: KeyType) {
        try {
            deleteFunction(chave)
        } catch (erro: Exception) {
            LOGGER.error("Exception ao excluir cache [${chave.toString()}] - $customMessage - ${erro.message}")
        }
    }
}