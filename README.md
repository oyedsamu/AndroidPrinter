# AndroidPrinter
POS/ESC Android Printer

<img src="https://raw.githubusercontent.com/salvadordeveloper/AndroidPrinter/master/screenshots/screenshoot.jpg" width="250" height="400"/>


## Getting Started

This is a guide to implement connection to an app service in your app, it can print tickets or anything you want in thermal printer's with connection Bluetooth, Wifi or USB.

## Download Thermal Printer App Service

To use this library you need install the app.

<img src="https://raw.githubusercontent.com/salvadordeveloper/AndroidPrinter/master/screenshots/download.png"
href="https://play.google.com/store/apps/details?id=com.salvador.print"
width="250" height="80"/>

## Import to proyect

Add line to Gradle.

```
    implementation 'com.salvador.printer:app:1.0.0'
```

## How to use

First initialize the TicketBuilder.

```
  var ticketBuilder = TicketBuilder(this)
```

Every time to need clean de ticket.-

```
  ticketBuilder.newTicket();
```

Add line to ticket.


```
  //Size : Size1, Size2, Size3
  //Align : Center, Left, Right
  ticketBuilder.addLine(string line, size : int, align : int)
```

Left Right Line.-

```
  ticketBuilder.addLine(string line, size : int, align : int)
```

Print QR and BarCode.

```
  ticketBuilder?.addBarCode(code : string)
  ticketBuilder?.addQRCode(qrcode : string)
```

Send to printer.

```
  ticketBuilder.printTicket();
```

Note.- You need be sure that have printer already connected in the Thermal Printer App.

Example 
```
    var ticketBuilder = TicketBuilder(this)
   
    ticketBuilder.newTicket()
    ticketBuilder.addLine("Thermal Printer",2,2)
    ticketBuilder.addLine("Ticket #234",0,2)
    ticketBuilder.addWhiteLine()

    ticketBuilder.addLeftRight("10:32","05/12/18")
    ticketBuilder.addWhiteLine()
    ticketBuilder.addLinePoints()
    ticketBuilder.addLeftRight("Product 1","10.00")
    ticketBuilder.addLeftRight("Product 2","5.00")
    ticketBuilder.addLeftRight("Product 3","65.00")
    ticketBuilder.addLeftRight("Product 4","43.00")
    ticketBuilder.addLinePoints()
    ticketBuilder.addWhiteLine()

    ticketBuilder.addLeftRight("Total : ","132.00")
    ticketBuilder.addWhiteLine()
    ticketBuilder.addLine("Thanks",0,2)
    ticketBuilder.addWhiteLine()
    ticketBuilder.addWhiteLine()
    ticketBuilder.addWhiteLine()

```

Example Result.

<img src="https://raw.githubusercontent.com/salvadordeveloper/AndroidPrinter/master/screenshots/test.jpg" width="250" height="250"/>











