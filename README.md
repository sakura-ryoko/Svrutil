# SvrUtil-Lite (Forked by Sakura-Ryoko)
Fork strips out a bunch of useless junk features from SvrUtil.

## Config file
This mod will work out of the box with all the commands ready to be used.  
There are a config file to modify some vanilla behavior, in that case please see [here](CONFIG.md).

## Commands
Note: These are just the default command configuration, you may optionally disable, change the required permission level or remap a command name in the [Command Config].  

| Command                                                                                     | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              | Privilege              |
|---------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------|
| /fancykick <Minecraft Text>                                                                 | Kick someone with Minecraft Raw JSON Text.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               | Op Level 2             |
| /silentkick <Player>                                                                        | Kick a player with a convincing message.<br>By default it is `Internal Exception: java.lang.StackOverflowError`, you may change it in the config file.                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | Op Level 2             |
| /feed (Target player)                                                                       | Fill the hunger and saturation to full for you, or the player you specified.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             | Op Level 2             |
| /heal (Target Player)                                                                       | Sets the health & hunger to full for you,<br>or the player you specified.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                | Op Level 2             |
| /svrutil<br>/svrutil reload                                                                 | Main SvrUtil command.`/svrutil` to see the version and homepage.<br>`/svrutil reload` to reload the config                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               | Op Level 2             |
| /msg <Target Player> <Message>                                                              | This sends a private message to the target player,<br>along with a "ding" sound.<br><br>Note: This will deregister the default vanilla `/msg` command.                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | Op Level 0<br>(Anyone) |
| /r <Message>                                                                                | Reply message command.<br>This works the same way for `/msg`, except it automatically sends it<br>to the last player you messaged.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       | Op Level 0<br>(Anyone) |
| /opLevel <Player>                                                                           | This returns the OP Level of the specified player.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       | Op Level 2             |
| /silentTp <Player>                                                                          | Teleport to player without sending a public system message                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               | Op Level 2             |
| /suicide                                                                                    | This kills yourself in-game, along with a public message being sent.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | Op Level 0<br>(Anyone) |
| /spawn                                                                                      | Teleport you to the world spawn.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         | Op Level 0<br>(Anyone) |
| /where <Target Player>                                                                      | Tells the XYZ Coordinates of the player                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  | Op Level 2             |

## Setup
Run ./gradlew build

## License
This project is licensed under the MIT License.
