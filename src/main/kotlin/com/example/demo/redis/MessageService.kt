package com.example.demo.redis

import com.example.demo.dto.ChatMessagePojo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList

@Service
class MessageService @Autowired
constructor(messageRedisRepository: IMessageRedisRepository)  {
    private val messageHandle = CacheExceptionHandlerExtension(
            messageRedisRepository::findById,
            messageRedisRepository::save
    )

    fun listar(idProposta: Long): ArrayList<ChatMessagePojo> {
        val handlerExtension = messageHandle.find(idProposta)
        if (handlerExtension.isPresent) {
            //TODO emtir mensagens
            return handlerExtension.get().messages
        }
        return ArrayList()
    }

    fun salvar(message: ChatMessagePojo){
        val mensagens = listar(message.idProposta)
        mensagens.add(message)

        val messageRedis = MessageRedisHash(idProposta = message.idProposta, mensagens)

        messageHandle.save(messageRedis)
        //TODO EMITIR PARA O WEBSOCKET
    }
}
