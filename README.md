# AndroidPrinter
POS/ESC Android Printer

<img src="https://raw.githubusercontent.com/salvadordeveloper/AndroidPrinter/master/screenshots/screenshoot.jpg" width="250" height="400"/>


## Getting Started

This is a guide to implement connection to an app service in your app, it can print tickets or anything you want in thermal printer's with connection Bluetooth, Wifi or USB.

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
  ticketBuilder.addLine
```








