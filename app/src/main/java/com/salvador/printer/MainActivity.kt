package com.salvador.printer

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.askjeffreyliu.floydsteinbergdithering.Utils
import com.thermalprinter.sdk.TicketBuilder
import android.provider.MediaStore
import android.provider.DocumentsContract
import com.thermalprinter.sdk.ImageUtils
import android.provider.MediaStore.Images
import java.io.ByteArrayOutputStream
import android.R.attr.data
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.NotificationCompat.getExtras
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View


class MainActivity : AppCompatActivity() {

    private val PICK_IMAGE = 1

    private var btnHelloWorld : Button? = null
    private var btnPrintDemo : Button? = null
    private var btnPrintImage : Button? = null
    private var btnPrintQR : Button? = null
    private var imgPrinter : ImageView? = null

    private var imagePath : String? = null

    private var ticketBuilder : TicketBuilder? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        initToolbar()
        checkPermissions()

        btnHelloWorld = findViewById(R.id.btn_helloworld)
        btnPrintDemo = findViewById(R.id.btn_demo)
        btnPrintImage = findViewById(R.id.btn_print_image)
        btnPrintQR = findViewById(R.id.btn_print_qr)
        imgPrinter = findViewById(R.id.image2)
        ticketBuilder = TicketBuilder(this)

        imgPrinter?.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
        }

        btnPrintDemo?.setOnClickListener {
            ticketBuilder?.newTicket()
            ticketBuilder?.addLine("Thermal Printer",2,2)
            ticketBuilder?.addLine("Ticket #234",0,2)
            ticketBuilder?.addWhiteLine()

            ticketBuilder?.addLeftRight("10:32","05/12/18")
            ticketBuilder?.addWhiteLine()
            ticketBuilder?.addLinePoints()
            ticketBuilder?.addLeftRight("Product 1","10.00")
            ticketBuilder?.addLeftRight("Product 2","5.00")
            ticketBuilder?.addLeftRight("Product 3","65.00")
            ticketBuilder?.addLeftRight("Product 4","43.00")
            ticketBuilder?.addLinePoints()
            ticketBuilder?.addWhiteLine()

            ticketBuilder?.addLeftRight("Total : ","132.00")
            ticketBuilder?.addWhiteLine()
            ticketBuilder?.addLine("Thanks",0,2)
            ticketBuilder?.addWhiteLine()
            ticketBuilder?.addWhiteLine()
            ticketBuilder?.addWhiteLine()

            ticketBuilder?.printTicket()
        }

        btnHelloWorld?.setOnClickListener {
            ticketBuilder?.newTicket()
            ticketBuilder?.addLine("Hello World")
            ticketBuilder?.printTicket()
        }

        btnPrintQR?.setOnClickListener{
            ticketBuilder?.newTicket()
            ticketBuilder?.addBarCode("44333223334")
            ticketBuilder?.addWhiteLine()
            ticketBuilder?.addWhiteLine()
            ticketBuilder?.addQRCode("www.google.com")
            ticketBuilder?.printTicket()
        }

        btnPrintImage?.setOnClickListener{
            if(imagePath != null){
                ticketBuilder?.newTicket()
                ticketBuilder?.addImage(imagePath!!)
                Toast.makeText(this,imagePath,Toast.LENGTH_SHORT).show()

                ticketBuilder?.printTicket()
            }else{
                Toast.makeText(this,"Image not selected",Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Printer Demo"
    }

    private fun checkPermissions(){
        val  permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
        @TargetApi(Build.VERSION_CODES.M)

        if (permissionCheck== PackageManager.PERMISSION_GRANTED){
            //this means permission is granted and you can do read and write
        }else{
            val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permissions,1001)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            if(imageUri != null){
                val photo = getBitmap(imageUri)
                imagePath =  ImageUtils.getPath(this,imageUri)
                imgPrinter?.setImageBitmap(photo)
            }
        }
    }

    private fun getBitmap(uri : Uri) : Bitmap {
        val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r");
        val fileDescriptor = parcelFileDescriptor.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    }

}
