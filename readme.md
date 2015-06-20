# tBot (TBD)

tBot is a desktop chat bot to be used with hitbox.tv, with features such as points and commands available.

tBot is written in latest version of Java which is required for the bot to function. Because of being writen in Java, it should be compatible with most operating systems without any other installations.

### First run

The first run of the programme will create a folder called "resources" in the same folder that the bot is ran from. Text files will be created inside this folder in the name of each channel connected to using the bot. These text files will be in the name of the channel joined and will be in JSON format containing the different commands, points and general settings for that channel.

### Connection

The first screen you will see when running the programme will be the connection screen, with text fields for Username, Password and Channel. The Username will the username of the bot you want to use, as well as the password. The channel will be the name of the channel you want to join. After hitting Login, if the details given are correct, a new screen will appear. If the screen does not change, there will msot likely be a problem with the connection. Hitting Help>Toggle Console will hopefully give an error; if the error is 400 for http://www.hitbox.tv/api/auth/token, it is a problem with the username or password entered. If the error is something different, please contact me for further assistance.

### Main view

If login is successful, the main view should appear with a chat section on the left, and a series of tabs on the right. The chat on the left will be the chat for the channel connected to.

###Tabs

- Dashboard: The dashboard tab has text fields and a choice box for the user to set the stream title and game being played for the channel connected to. This will only work if the bot is an editor/owner in the channel and if the bot has his account set up for livestreaming. This tab will also show a list of users connected to the channel, showing their User Level, name and points. The suerlist will be updated as points are given to the channel - <em>to be changed<em>.
- Commands: This tab is where the user will add custom caht commands for the bot to use. Fields for the Command Name, Message and User level required for users to use the commands are included. Added commands will appear in the table below. Please read the detailed command section for more information.
- Points: The points tab includes the settings for points for the connected channel. Kepping the Points Enabled checkbox checked will add the specified amount points to each connected user in the channel after the inputted time.
- Points Games: A tab which includes different games the viewers of the channel can join in with.
    - Raffle: Each user can join a running raffle by typing !joinRaffle x into the chat where x is a number less than Max Tickets and is optional; leaving x out will let the user join with 1 ticket. Joining the raffle will remove points based on the tickets cost and how many tickets played with (tickets played * ticket cost) Playing with more tickets will give a higher chance of winning.
    - Lottery: Each user can join a running raffle by typing !joinLottery x into the chat where x is a number less than Max Tickets and is optional; leaving x out will let the user join with 1 ticket. Joining the raffle will remove points based on the tickets cost and how many tickets played with (tickets played * ticket cost) Playing with more tickets will give a higher chance of winning. Winning the lottery will reward the drawn user with the total points played by each user AFTER htting the reset button.

###Commands

- Variables: Varaiables can be used to include information in the command's messages. For example, using <em>"You have %p %pN"<em> as the command message will reply telling the user how many points they have.
    - %p: replaced with the current points for the user who exectured the command.
    - %pN: replaced with the name of the points defined in the points settings.
    - %pA replaced with the amount of points given after each interval.
    - %pT: replaced with the time of the points interval.
- Whispers: Starting a command name with "/w " will send the message back to the user who exectuted the command by sending the bot a whisper with the command. For example, having the command name "/w points" will send the message to the user in a whisper if they send the bot a whipser using "/w botname points". At the end of the whisper sent by the bot will have an ignore message with some random characters to avoid a duplicate message error. Variables work in whisper commands.
