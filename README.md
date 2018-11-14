# LEDSignControl
RS232 with MySQL database

This is a Java program built to run on a Raspberry Pi to control a RS232 sign (specifically Cincinnati Series Two 20-character LED sign).
It features two forks (main and fixed-messages):
Main has the ability to display a message using scroll or blink, or normal text. Supports only ascii characters.
Fixed message is built to display any number of fixed messages, must be coded in first. 
   Does support scroll and blink, but must be coded in. Supports only ascii characters.
Either fork uses a webserver to interface with (running a mysql database and single web page). 

The database is as follows:
MariaDB [messagedb]> select * from t;

| message | isupdated | scrollon | error |

message - the message to be displayed.

isupdated - false, update the display, true, display is updated.

scrollon - true if scroll is required, else false.

error - holds any errors.

blink characters are added to the message by the user using buttons on the webpage.

SQL Users are defined as follows: 
javauser / admin, 
client / admin
