# tBot (TBD)

Download: http://gamingtom.com/h/chatbot/

tBot is a desktop chat bot to be used with hitbox.tv, with features such as points and commands available.

tBot is written in latest version of Java which is required for the bot to function. Because of being writen in Java, it should be compatible with most operating systems without any other installations.

tBot is completely free and requires no payments. However if you like the work I have done, feel free to donate:

- [PayPal](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=LAAKR2BWN92XG&lc=GB&item_name=GamingTom&currency_code=GBP&bn=PP%2dDonationsBF%3abtn_donate_LG%2egif%3aNonHosted)
- [ImRaising](https://imraising.tv/u/gamingtom)
- [StreamTip](https://streamtip.com/h/gamingtom)

### First run

The first run of the programme will create a folder called "resources" in the same folder that the bot is ran from. Text files will be created inside this folder in the name of each channel connected to using the bot. These text files will be in the name of the channel joined and will be in JSON format containing the different commands, points and general settings for that channel.

### Connection

The first screen you will see when running the programme will be the connection screen, with text fields for Username, Password and Channel. The Username will the username of the bot you want to use, as well as the password. The channel will be the name of the channel you want to join. After hitting Login, if the details given are correct, a new screen will appear. If the screen does not change, there will msot likely be a problem with the connection. Hitting Help>Toggle Console will hopefully give an error; if the error is 400 for http://www.hitbox.tv/api/auth/token, it is a problem with the username or password entered. If the error is something different, please contact me for further assistance.

### Main view

If login is successful, the main view should appear with a chat section on the left, and a series of tabs on the right. The chat on the left will be the chat for the channel connected to.

### Tabs

- Dashboard: The dashboard tab has text fields and a choice box for the user to set the stream title and game being played for the channel connected to. This will only work if the bot is an editor/owner in the channel and if the bot has his account set up for livestreaming. This tab will also show a list of users connected to the channel, showing their User Level, name and points. The suerlist will be updated as points are given to the channel - to be changed.
- Commands: This tab is where the user will add custom chat commands for the bot to use. Fields for the Command Name, Message and User level required for users to use the commands are included. Added commands will appear in the table below. To remove a command, you can enter the command name and hit Remove Command, or you can right click on the command in the table and select Remove. The button for Advanced Editor will give a script editor for you to edit the default/main script of the client. More infomration on scripts in a further section. Please read the detailed command section for more information.
- Timed Messages: To create a timed message, enter a unique ID for the bot, a message you want the bot to say and the time in seconds for how often you want the bot to repeat the message. To remove a timed message, or to disable it, right click the row in the table and select Remove or Toggle.
- Points: The points tab includes the settings for points for the connected channel. Kepping the Points Enabled checkbox checked will add the specified amount points to each connected user in the channel after the inputted time.
- Points Games: A tab which includes different games the viewers of the channel can join in with.
    - Raffle: Each user can join a running raffle by typing !joinRaffle x into the chat where x is a number less than Max Tickets and is optional; leaving x out will let the user join with 1 ticket. Joining the raffle will remove points based on the tickets cost and how many tickets played with (tickets played * ticket cost) Playing with more tickets will give a higher chance of winning.
    - Lottery: Each user can join a running raffle by typing !joinLottery x into the chat where x is a number less than Max Tickets and is optional; leaving x out will let the user join with 1 ticket. Joining the raffle will remove points based on the tickets cost and how many tickets played with (tickets played * ticket cost) Playing with more tickets will give a higher chance of winning. Winning the lottery will reward the drawn user with the total points played by each user AFTER htting the reset button.
- Scripts: The Scripts tab is where you can select which scripts you would like to be used with the client. To download a script, right click the script and select Download. More information on scripts is below.

### Scripts

My aim for tBot was for a community driven bot where users can create and share their own add-ons and change the bot to how they want it. I believe I've achieved this with an in-bot script editor and manager. This way, users have access to a full script API to create scripts using Javascript and get them uploaded to the script database for others to see and download from the bot. Scripts are created in Javascript and if you want a script added to the script database for others to download, please send me a download link to your script with the folders in the format of scriptName.zip>scriptName>script.js and I will verify that it is safe to use.

##### Script Functions

| Function                            | Use                                                                                   |
|-------------------------------------|---------------------------------------------------------------------------------------|
| function newMessage(message){       | Executes when any message is sent by the hitbox server.                               |
| function onInfoMsg(message){        | Executes when the server sends a message with the method "infoMsg".                   |
| function onStreamUpdate(message){   | Executes when there is an update to the stream information, e.g Game Played or Title. |
| function onHostUpdate(message){     | Executes when the host information is changed.                                        |
| function onMotdUpdate(message){     | Executes when the MOTD information is changed.                                        |
| function onText(message){           | Executes when a message is sent in chat.                                              |
| function onWhisper(message){        | Executes when the bot receives a whisper.                                             |
| function onSpecialChat(message){    | Executes when a special message is received, e.g someone follows the channel.         |
| function onMediaLog(message){       | Executes when the bot requests the media log.                                         |
| function onJoin(message){           | Executes when users join the channel.                                                 |
| function onPart(message){           | Executes when users leave the channel.                                                |
| function onUserList(message){       | Executes when the user list is requested.                                             |
| function onUserInfo(message){       | Executes when information is requested from a user.                                   |
| function onNewSubscriber(message){  | Executes when someone subscribes.                                                     |
| function onGiveawayStared(message){ | Executes when a hitbox giveaway is started in the channel.                            |
| function onGiveawayEnded(message){  | Executes when a hitbox giveaway is ended in the channel.                              |
| function onPollStared(message){     | Executes when a hitbox poll is started in the channel.                                |
| function onPollEnded(message){      | Executes when a hitbox poll is ended in the channel.                                  |

##### Script API

Function | Information | Extra Information
---|---|----
$api.log("message"); | Logs a message to the debug console. | 
$api.log("message", "colour"); | Logs a message to the debug console with the colour "colour" | "colour" is a hex colour value without the #
$api.readFile($script, "name.txt"); | Reads a files named "name.txt" from the installation folder of the script. | 
$api.writeFile($script, "name.txt", "contents"); | Writes to a file named "name.txt" in the installation folder of the script. | 
$api.addToFile($script, "name.txt", "contents"); | Adds a new line of "contents" to the end of "name.txt" | 
$api.isConnected(); | Returns true if the bot is connected to a channel. | 
$api.sendMessage("message"); | Sends "message" to the connected channel. | 
$api.printToChat("message"); | Prints "message" to the chat; client side only. | 
$api.getBotName(); | Returns the username of the connected bot. | 
$api.getChannel(); | Returns the name of the channel the bot is connected to. | 
$api.getDate(); | Returns a date in the format of "MM-dd-yy_HH-mm". | 
$api.getTime(); | Returns a time in the format of "HH:mm". | 
$api.getUserList(); | Returns the user list of the connected channel. | Parse the user list to a Javascript object using "JSON.parse($api.getUserList());"
$api.getCommands(); | Returns the command list of the connected channel. | Parse the command list to a Javascript object using "JSON.parse($api.getCommands());"
$api.getCommandMessage("commandName"); | Returns the command message for the command "commandName". | 
$api.getCommandLevel("commandName"); | Returns the user level needed for the command "commandName". | 
$api.getPoints("username"); | Returns the amount of points the user "username" has. | 
$api.addPoints("username", int); | Adds int amount of points to the user "username". | 
$api.removePoints("username", int); | Removes int amount of points from the user "username". | 
$api.setPoints("username", int); | Sets the points for "username" to int. | 

### Commands

- Variables: Varaiables can be used to include information in the command's messages. For example, using "You have %p %pN" as the command message will reply telling the user how many points they have.
    - %p: replaced with the current points for the user who exectured the command.
    - %pN: replaced with the name of the points defined in the points settings.
    - %pA replaced with the amount of points given after each interval.
    - %pT: replaced with the time of the points interval.
- Whispers: Starting a command name with "/w " will send the message back to the user who exectuted the command by sending the bot a whisper with the command. For example, having the command name "/w points" will send the message to the user in a whisper if they send the bot a whipser using "/w botname points". At the end of the whisper sent by the bot will have an ignore message with some random characters to avoid a duplicate message error. Variables work in whisper commands.
