# Changelog

## 17.3.0:

- Added support for 1.20 (no changes required, already compatible).

## 17.2.4:

- Fixed the global sound toggle being backwards.

## 17.2.3:

- Fixed a warning when loading on 1.19.4 servers.
- Fixed player visited worlds being updated when world-based messages are disabled.
- Fixed the MiniMessage formatter sometimes trying to load on incompatible servers.
- Fixed the Minedown formatter being allowed to be selected on servers older than 1.12.2.
- Fixed an error when logging chat messages to console on servers older than 1.12.2.
- The legacy formatter is now the default.


## 17.2.2:
- Fixed Spigot update checker URL.


## 17.2.1:
- Fixed some incompatibilities with Java 8.
- Fixed EssentialsX vanish message showing the person who executed the command, not the person who was vanished.


## 17.2.0:
- Added an option to change the text formatter. More information on the wiki.
- Fixed a possible bug where players wouldn't be granted the cjm.default permission.


## 17.1.1:
- Fixed an error in the update checker on < 1.17 servers.


## 17.1.0:
- Added support for 1.19.3 (no changes required, already compatible).
- Added an option to send public chat messages to console.
- Fixed VelocityVanish not being a soft-dependency.


## 17.0.1:
- Fixed the Modrinth update checker not parsing responses correctly.


## 17.0.0:
- Reformatted configuration files. Your old config will be renamed and a new one will be generated.
- Added support for formatting messages with MineDown. It may differ from what you're used to, so please check out the wiki.
- Added support for world-based messages.
- Added bossbar messages.
- Added sound "messages".
- Added automatic config reloading. Once changes are saved, they will be automatically picked up.
- Added option to not send messages for jailed players (Only supports EssentialsX and CMI jails).
- Added support for VelocityVanish, and EssentialsX's vanish.
- Added 'Min-Players' config option.
- Added 'Max-Players' config option.
- Fixed HEX colour codes not working on 1.17-1.19.x.
- Removed support for 1.8.x servers.
- Removed support for syncing toggle status via MySQL.
- Removed the "cjm debug" command.
- Removed the "cjm joindate" command.
- Removed the "cjm reload" command.
- Removed the "cjm version" command.
- Removed support for tellraw formatted JSON messages. Please use the MineDown format instead.
