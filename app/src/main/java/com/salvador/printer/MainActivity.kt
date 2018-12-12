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

    val PICK_IMAGE = 1

    private var btnHelloWorld : Button? = null
    private var btnPrintDemo : Button? = null
    private var btnPrintImage : Button? = null
    private var btnPrintQR : Button? = null

    private var imgPrinter : ImageView? = null

    private var ticketBuilder : TicketBuilder? = null

    private var imagePath : String? = null


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

            // ticket.addQRCode("433244")

            //  ticket.addBarCode("4433443")
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
        // toolbar.setNavigationIcon(R.drawable.ic_menu)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("Printers")
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }


    public fun checkPermissions(){
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
            val imageUri = data?.getData()



            if(imageUri != null){

                val photo = getBitmap(imageUri)
                imagePath =  ImageUtils.getPath(this,imageUri)
                imgPrinter?.setImageBitmap(getImage(photo,300,2))
            }
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor!!.moveToFirst()
        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return cursor.getString(idx)
    }

    fun getPath(context: Context, uri: Uri): String {
        var result: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.getContentResolver().query(uri, proj, null, null, null)
        if (cursor != null) {
            if (cursor!!.moveToFirst()) {
                val column_index = cursor!!.getColumnIndexOrThrow(proj[0])
                result = cursor!!.getString(column_index)
            }
            cursor!!.close()
        }
        if (result == null) {
            result = ""
        }
        return result
    }

    private fun getImage(bmp : Bitmap, width : Int, mode : Int) : Bitmap{
        val width = (width + 7) / 8 * 8
        var height = bmp.getHeight() * width / bmp.getWidth()
        height = (height + 7) / 8 * 8

        var rszBitmap = bmp
        rszBitmap = getResizedBitmap(bmp, width, height)
        val fsBitmap = Utils.floydSteinbergDithering(rszBitmap)

        return fsBitmap
    }

    private fun getBitmap(uri : Uri) : Bitmap {
        val parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        val fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image
    }

    fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)

        // "RECREATE" THE NEW BITMAP
        val resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false)
        bm.recycle()
        return resizedBitmap
    }


}
