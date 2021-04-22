package com.example.demo.dto

class ChatMessagePojo {
        var username: String? = null
        var mensagem: String? = null
        var idProposta: Long = 0

        constructor() {}
        constructor(username: String?, mensagem: String?, idPropota: Long?) {
                this.username = username
                this.mensagem = mensagem
                this.idProposta = idProposta
        }

        override fun toString(): String {
                return "$mensagem, $username!"
        }
}