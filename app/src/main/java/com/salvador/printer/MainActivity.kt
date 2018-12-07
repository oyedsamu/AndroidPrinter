package com.salvador.printer

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.askjeffreyliu.floydsteinbergdithering.Utils
import com.thermalprinter.sdk.TicketBuilder
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val PICK_IMAGE = 1

    private var btnHelloWorld : Button? = null
    private var btnPrintDemo : Button? = null
    private var btnPrintImage : Button? = null

    private var imgPrinter : ImageView? = null

    private var ticketBuilder : TicketBuilder? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnHelloWorld = findViewById(R.id.btn_helloworld)
        btnPrintDemo = findViewById(R.id.btn_demo)
        btnPrintImage = findViewById(R.id.btn_print_image)

        imgPrinter = findViewById(R.id.image)

        ticketBuilder = TicketBuilder(this)

        image?.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.getData()

            if(imageUri != null){

                imgPrinter?.setImageBitmap(getImage(getBitmap(imageUri!!),300,2))
            }


        }
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
