# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).


## [Unreleased]

### Fixed

- Title messages not being sent if either the primary title or subtitle are blank.
- Command warning during startup on 1.20.6+ servers.


## [17.7.0] - 2024-12-26

### Added

- Support for 1.21.3/1.21.4.
- Support for SayanVanish versions newer than v1.5.0.

### Removed

- Support for sounds messages on servers older than 1.12.


## [17.6.0] - 2024-06-15

### Added

- Support for 1.20.6 and 1.21 (no changes needed).
- Support for [SayanVanish](https://modrinth.com/plugin/sayanvanish).

### Fixed

- A harmless warning at startup when using AdvancedVanish.


## [17.5.0] - 2024-04-28

Custom Join Messages is now published on [Hangar](https://hangar.papermc.io/Insprill/Custom-Join-Messages)!

### Added

- Support for 1.20.5 (no changes needed).
- Support for [Advanced Vanish](https://www.spigotmc.org/resources/86036/).

### Fixed

- Private messages being sent when unvanishing.


## 17.4.1 - 2024-04-03

### Fixed

- Compatibility with CMI versions newer than 9.7.0.0. Older CMI versions are no longer supported.


## 17.4.0 - 2024-03-17

### Added

- Support for basic legacy colour codes when using MiniMessage.

### Fixed

- Blank chat message lines being ignored.
 

## 17.3.2 - 2024-02-24

### Fixed
- An error when trying to read player locales on non-Paper 1.20.2+ servers.


## 17.3.1 - 2023-08-18

### Fixed
- Fake join/quit messages sending when they shouldn't when using VelocityVanish.


## 17.3.0 - 2023-06-27

### Added
- Support for 1.20/1.20.1 (no changes required, already compatible).

### Fixed
- Quit messages not working with AuthMe.


## 17.2.4 - 2023-04-21

### Fixed
- The global sound toggle being backwards.


## 17.2.3 - 2023-04-15

### Fixed
- A warning when loading on 1.19.4 servers.
- Player visited worlds being updated when world-based messages are disabled.
- The MiniMessage formatter sometimes trying to load on incompatible servers.
- The MineDown formatter being allowed to be selected on servers older than 1.12.2.
- An error when logging chat messages to console on servers older than 1.12.2.

### Changed
- The legacy formatter is now the default.


## 17.2.2 - 2023-02-10

### Fixed
- The Spigot update checker URL.


## 17.2.1 - 2023-02-10

### Fixed
- Some incompatibilities with Java 8.
- EssentialsX vanish message showing the person who executed the command, not the person who was vanished.


## 17.2.0 - 2023-01-25

### Added
- An option to change the text formatter. More information on the wiki.

### Fixed
- A possible bug where players wouldn't be granted the cjm.default permission.


## 17.1.1 - 2023-01-13

### Fixed
- An error in the update checker on < 1.17 servers.


## 17.1.0 - 2022-12-08

### Added
- Support for 1.19.3 (no changes required, already compatible).
- An option to send public chat messages to console.

### Fixed
- Fixed VelocityVanish not being a soft-dependency.


## 17.0.1 - 2022-11-30

### Fixed
- Fixed the Modrinth update checker not parsing responses correctly.


## 17.0.0 - 2022-11-14

### Changed
- Reformatted configuration files. Your old config will be renamed and a new one will be generated.

### Added
- Support for formatting messages with MineDown. It may differ from what you're used to, so please check out the wiki.
- Support for world-based messages.
- Bossbar messages.
- Sound "messages".
- Automatic config reloading. Once changes are saved, they will be automatically picked up.
- Option to not send messages for jailed players (Only supports EssentialsX and CMI jails).
- Support for VelocityVanish, and EssentialsX's vanish.
- The 'Min-Players' config option.
- The 'Max-Players' config option.

### Fixed
- Fixed HEX colour codes not working on 1.17-1.19.x.

### Removed
- Ssupport for 1.8.x servers.
- Support for syncing toggle status via MySQL.
- The "cjm debug" command.
- The "cjm joindate" command.
- The "cjm reload" command.
- The "cjm version" command.
- Support for tellraw formatted JSON messages. Please use the MineDown format instead.


<!-- Diffs -->
[Unreleased]: https://github.com/Insprill/custom-join-messages/compare/master...develop
[17.7.0]: https://github.com/Insprill/intellectual/compare/v17.6.0...v17.7.0
[17.6.0]: https://github.com/Insprill/intellectual/compare/v17.5.0...v17.6.0
[17.5.0]: https://github.com/Insprill/intellectual/compare/5765a505...v17.5.0
