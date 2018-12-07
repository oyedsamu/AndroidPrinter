package com.salvador.printer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.thermalprinter.sdk.TicketBuilder


class MainActivity : AppCompatActivity() {

    private var btnHelloWorld : Button? = null
    private var btnPrintDemo : Button? = null
    private var ticketBuilder : TicketBuilder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnHelloWorld = findViewById(R.id.btn_helloworld)
        btnPrintDemo = findViewById(R.id.btn_demo)
        ticketBuilder = TicketBuilder(this)

        btnPrintDemo?.setOnClickListener {
            ticketBuilder?.newTicket()
            ticketBuilder?.addLine("Title")
            ticketBuilder?.addLine("Bluetooth Example Test",2,2)
            ticketBuilder?.addLeftRight("10:32","05/12/18")
            ticketBuilder?.addWhiteLine()
            ticketBuilder?.addLinePoints()
            ticketBuilder?.addWhiteLine()
            ticketBuilder?.addLine("Thanks")
            ticketBuilder?.addWhiteLine()
            ticketBuilder?.addWhiteLine()
            ticketBuilder?.addWhiteLine()
            ticketBuilder?.printTicket()
        }

        btnHelloWorld?.setOnClickListener {
            ticketBuilder?.newTicket()
            ticketBuilder?.addLine("HelloWorld")
            ticketBuilder?.printTicket()
        }


    }
}
