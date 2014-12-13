NetworkSecurityCourse - Trojan Horse Malware
=====================
**For Educational Purposes Only**
====================

_Objective:_
Remote access and data theft of infected computer system without leaving a trace. 

_Drawbacks:_
In order to create connection with client’s computer, it is necessary to run the Trojan at least once.

Once the Trojan is in the hands of a client and a connection was not yet been established, we have no indicator to possible errors in client.

Although Server is multi-threaded (thread per client), it is limited to the amount of clients it can hold at a time.

Requires JRE 1.7.0 to run.
	

Activation:
-----------
To make sure the Trojan horse will be activated at all times, upon first activation the
executable file is copied to the startup folder, and will run without showing the image after.
>See main function under TrojanHorse.java class

For the purpose of not raising any suspicions regarding the malware, when opening the infected file, a pic would be shown to user with the default user image viewer program.
>See TrojanHorse constructor under TrojanHorse.java class

__So how does it work?__

1. The TrojanHorse.java class runs a Client instance which keeps listening to the website: http://drorventura.github.io/templates/mp/addr.html

  >See checkForTrigger under Client.java class

2. This website contains a line with the following format: 

  [String Activated; String IP; String Port] 

  Where:
  
  + Activated - 1 if the server is interested in establishing connection with the client (0 otherwise).

  + IP - server IP.
 
  + Port - server port.

  When Activated = 1 the client stops listening to the website and opens a socket connected to server IP and server port.

  >See initConnection under Client.java class

3. The socket will remain open for the server to extract data from the client until the website will be updated to Activated = 0 and the server will send close connection message to the client.
  Afterwards the client will go back to listen to the website.

  >See closeConnection under Client.java class


Operation:
----------
The server is multi-threaded – it is using a broadcasting design pattern, therefore it can accept multiple client connection and sends a broadcast message to all connected clients.
The implementation of such a pattern in java is by the Observer Class and Observable interface.

###Server’s job flow description:###
**Server Class** – runs the main thread and waits for commands to broadcast.

**Listener Class** – listener thread that creates the server socket and waits for a new connection to a client socket.

**Connection Handler Class** – on every new connection the listener register a new instance that observes the Server class and waits for an event (command).

###Protocol Description:###
For each such connection the server can send one or more of the following commands:

0 - Close connection _closes the client socket_

1 - Print screen _take a screen shot of the client pc_

2 - Listen to keyboard _listen to client keyboards clicks for 30 seconds _

3 - Find documents _search the client pc for .doc, .xls, .pdf and .txt files_

4 – Find cookies and history _search the client pc for chrome cookies and history files_

The commands are sent as a binary string of length 5.
For example the command 2;3 is translated into 00110.
>See prepareMessage under ProtocolImpl.java class

###Functionality###
The client in response preforms the commands and sends back the required files to the server.

Some operations require creating new files on the client’s computer, therefore the files are saved as hidden and after they are sent, the client deletes them.
>See handleMessage under Client.java class, FindCookiesHistory.java class, FindDocuments.java class, ListenToKeyboard.java class and PrintScreen.java class

Once a response is returned to server it is processed by the protocol and it handles the message appropriately.
>See receiveMessage under ConnectionHandler.java class
