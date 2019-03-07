# AndroidPrinter
POS/ESC Android Printer

<img src="https://raw.githubusercontent.com/salvadordeveloper/AndroidPrinter/master/screenshots/screenshoot.jpg" width="250" height="400"/>


## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

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








