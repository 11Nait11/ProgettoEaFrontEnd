package com.example.progettoeafrontend.ui


interface Payment {
    var pagamentoOk:Boolean
    fun paga(amount: Double): String
}




class PaypalPayment : Payment {
    override var pagamentoOk:Boolean=true


    override fun paga(amount: Double): String {

        val randomValue = (1..3).random()

        if (randomValue == 3) {
            pagamentoOk=false
            throw PaymentFailException("Pagamento con Paypal fallito: importo $amount")
        }

        return "Pagamento effettuato con Paypal: importo $amount"
    }
}


class CardaCreditoPayment : Payment {
    override var pagamentoOk:Boolean=true


    override fun paga(amount: Double): String {
        // Genera un numero casuale tra 1 e 5
        val randomValue = (1..2).random()

        if (randomValue == 2) {
            pagamentoOk=false
            throw PaymentFailException("Pagamento con carta di credito fallito: importo $amount")
        }

        return "Pagamento effettuato con carta di credito: importo $amount"
    }
}

class PostinoPayment:Payment{
    override var pagamentoOk: Boolean=true


    override fun paga(amount: Double): String {
        return "Pagamento da effettuare al postino: importo $amount"
    }

}


object PaymentFactory {
    private val imp = listOf(PaypalPayment(), CardaCreditoPayment(),PostinoPayment() )

    fun getImp(): Payment {
        val randomIndex = (0 until imp.size).random()
        return imp[randomIndex]
    }
}

class PaymentFailException(message: String) : Exception(message)