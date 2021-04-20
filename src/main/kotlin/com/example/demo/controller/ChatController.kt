package com.example.demo.controller

import com.example.demo.dto.ChatMessagePojo
import com.example.demo.redis.ChatJedisPubSub
import com.example.demo.redis.MessageService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Controller


@Controller
class ChatController(
        private val messageService: MessageService,
        private val messagingTemplate: SimpMessageSendingOperations,
) {

    val chatJedisPubSub = ChatJedisPubSub()

    @MessageMapping("/chat.sendMessage/{idProposta}")
//    @SendTo("/topic/public/{idProposta}")
    fun sendMessage(@Payload chatMessagePojo: ChatMessagePojo?): ChatMessagePojo? {
        if (chatMessagePojo != null) {
//            messageService.salvar(chatMessagePojo)
//            messageService.listar(chatMessagePojo.idProposta)
//            messagingTemplate?.convertAndSend("/topic/public/${chatMessagePojo?.idProposta}", chatMessagePojo)
        }

        chatJedisPubSub.onMessage(chatMessagePojo?.idProposta.toString(), chatMessagePojo.toString())
        return chatMessagePojo
    }

    @MessageMapping("/chat.addUser/{idProposta}")
    @SendTo("/topic/public/{idProposta}")
    fun addUser(
            @Payload chatMessagePojo: ChatMessagePojo,
            headerAccessor: SimpMessageHeaderAccessor,
    ): ChatMessagePojo? {
        // Add username in web socket session
        headerAccessor.sessionAttributes!!["username"] = chatMessagePojo.username
        headerAccessor.sessionAttributes!!["idProposta"] = chatMessagePojo.idProposta
//        messageService.salvar(chatMessagePojo)
//        messageService.listar(chatMessagePojo.idProposta)


        chatJedisPubSub.onSubscribe(chatMessagePojo.idProposta.toString(), 0)

        return chatMessagePojo
    }
}