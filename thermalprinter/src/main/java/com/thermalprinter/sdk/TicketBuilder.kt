package com.thermalprinter.sdk

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.widget.Toast
import com.thermalprinter.sdk.LineLabel.Companion.ALIGN_CENTER
import com.thermalprinter.sdk.LineLabel.Companion.ALIGN_LEFT
import com.thermalprinter.sdk.LineLabel.Companion.ALIGN_RIGHT
import com.thermalprinter.sdk.LineLabel.Companion.SIZE_1
import com.thermalprinter.sdk.LineLabel.Companion.SIZE_2
import com.thermalprinter.sdk.LineLabel.Companion.SIZE_3

class TicketBuilder(val context : Context) {

    private var ticket: StringBuilder? = null

    init{
        newTicket()
    }

    fun newTicket(){
        ticket = StringBuilder()
    }

    fun addLine(line : String){
        ticket?.appendln(LineLabel.Line + line + LineLabel.Line)
    }

    fun addWhiteLine(){
        ticket?.appendln(LineLabel.Line+LineLabel.Line)
    }

    fun addLine(line : String, size : Int, align : Int) {
        ticket?.append(LineLabel.Line)

        when (size) {
            SIZE_1 -> ticket?.append(LineLabel.Size1)
            SIZE_2 -> ticket?.append(LineLabel.Size2)
            SIZE_3 -> ticket?.append(LineLabel.Size3)
        }
        when (align) {
            ALIGN_LEFT -> ticket?.append(LineLabel.Left)
            ALIGN_CENTER -> ticket?.append(LineLabel.Center)
            ALIGN_RIGHT -> ticket?.append(LineLabel.Right)
        }

        ticket?.append(line)

        when (size) {
            SIZE_1 -> ticket?.append(LineLabel.Size1)
            SIZE_2 -> ticket?.append(LineLabel.Size2)
            SIZE_3 -> ticket?.append(LineLabel.Size3)
        }
        when (align) {
            ALIGN_LEFT -> ticket?.append(LineLabel.Left)
            ALIGN_CENTER -> ticket?.append(LineLabel.Center)
            ALIGN_RIGHT -> ticket?.append(LineLabel.Right)
        }
        ticket?.append("\n")
    }


    fun addQRCode(code : String){
        ticket?.appendln("<QRCODE>$code<QRCODE>")
    }

    fun addBarCode(code : String){
        ticket?.appendln("<BARCODE>$code<BARCODE>")

    }
    fun addLinePoints(){
        ticket?.appendln("<LinePoints><LinePoints>")
    }

    fun addLeftRight(left : String, right : String){
        ticket?.appendln("<LineLF><L>$left<L><R>$right<R><LineLF>")
    }

    fun getTicket() : String {
        return ticket.toString()
    }

    fun printTicket(){
        try {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.setClassName("com.salvador.print",
                    "com.salvador.print.SendCommand")
            sharingIntent.putExtra(Intent.EXTRA_TEXT, getTicket())
            context.startActivity(sharingIntent)
        } catch (e: Exception) {
            Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
        }
    }

}