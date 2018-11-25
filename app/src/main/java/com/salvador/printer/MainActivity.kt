package com.salvador.printer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.thermalprinter.sdk.TicketBuilder


class MainActivity : AppCompatActivity() {

    private var btnHelloWorld : Button? = null
    private var btnPrintDemo : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ticketBuilder = TicketBuilder(this)
        ticketBuilder.addLine("Hola")
        ticketBuilder.printTicket()
    }
}
