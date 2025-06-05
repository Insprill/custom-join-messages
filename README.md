[![Spigot Downloads][spigot-downloads-shield]][spigot-url]
[![bStats Servers][bstats-servers-shield]][bstats-url]
[![Spigot Rating][spigot-rating-shield]][spigot-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![GNU License][license-shield]][license-url]
[![Discord][discord-shield]][discord-url]




<!-- PROJECT LOGO -->
<div align="center">
  <h1>Custom Join Messages</h1>
  <p>
    The most feature-packed and highly customizable plugin for join/quit notifications on the market
    <br />
    <br />
    <a href="https://github.com/Insprill/Custom-Join-Messages/issues">Report Bug</a>
    ·
    <a href="https://github.com/Insprill/Custom-Join-Messages/issues">Request Feature</a>
  </p>
</div>




<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#features">Features</a></li>
    <li>
      <a href="#download">Download</a>
      <ul>
        <li><a href="#releases">Releases</a></li>
        <li><a href="#snapshots">Snapshots</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#building">Building</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Support</a></li>
  </ol>
</details>




<!-- FEATURES -->
## Features

* 📢 Multiple Message Types
    * Chat
    * Titles
    * Actionbar
    * Bossbar
    * Sounds (1.12+)

* 💬 Advanced Formatting
    * All messages support [PlaceholderAPI][placeholderapi-url] placeholders
    * All messages support different formatters to fit your needs
        * [Legacy Color Codes][legacy-color-codes-url]
        * [MiniMessage][minimessage-url] ([Paper][papermc-url] only)
        * [MineDown][minedown-url]
    * All messages support HEX colors
    * All messages support gradients when using MineDown or MiniMessage
    * Chat messages support hover/click actions when using MineDown or MiniMessage

* 🌎 World-Based Messages
    * Treat a single server with multiple worlds as separate servers
    * Customizable groups for what worlds count as the same "server"

* 🗝️ Permission-Based Messages
    * Send different messages based on what permissions a user has

* 🎛️ Message Conditions
    * Radius
    * Min/Max Online Players

* 🔒 AuthMe Integration
    * Only send messages once players have authenticated themselves

* ⛓️ Jail Integration
    * Don't send messages for jailed players
    * Supports most jail plugins

* 🥷 Vanish Integration
    * Send messages when vanishing/unvanishing
    * Supports most vanish plugins

* 📜 Supports All Platforms
    * [Spigot][spigotmc-url]
    * [Paper][papermc-url]
    * [Folia][folia-url]
    * [Purpur][purpurmc-url]
    * Most other forks




<!-- DOWNLOAD -->
## Download
### Releases
Releases of CJM can be downloaded from [Modrinth][modrinth-url] (recommended), [Hangar][hangar-url], or [SpigotMC][spigot-url].

### Snapshots
Like living on the edge?
Builds from the latest commit can be downloaded via the [Snapshot Hangar channel][hangar-versions-url] or from [GitHub Actions][github-actions-url].




<!-- USAGE -->
## Usage

Unsure how something works? All documentation can be found on the [wiki][wiki-url].




<!-- BUILDING -->
## Building

To compile Custom Join Messages, you will need an internet connection and a terminal.  
Clone this repo, then run `./gradlew build`.  
You can find the compiled jar in the `build/libs` directory.




<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create.  
Any contributions you make are **greatly appreciated**!  
If you're new to contributing to open-source projects, you can follow [this](https://docs.github.com/en/get-started/quickstart/contributing-to-projects) guide.




<!-- Statistics -->
## Statistics

[![Statistics](https://bstats.org/signatures/bukkit/Custom%20Join%20Messages.svg)][bstats-url]

Statistics are collected through [bStats][bstats-url],
an open-source service that collects anonymous data for Minecraft software. You can opt out in `plugins/bStats/config.yml`.



<!-- LICENSE -->
## License

Distributed under the GNU General Public License v3.0.  
See [LICENSE][license-url] for more information.




<!-- SUPPORT -->
## Support

Found a bug? Want to request a feature? Head over to [the issue tracker][issues-url].\
Unsure how something works? Check out the [wiki][wiki-url].\
Still need personalized support? Subscribe to my [Patreon][patreon-url] and join my [Discord Server][discord-url].\
Once you've subscribed and joined, you can ask questions in the `#support` channel.\


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[bstats-servers-svg]: https://bstats.org/signatures/bukkit/Custom%20Join%20Messages.svg
[bstats-servers-shield]: https://img.shields.io/bstats/servers/6346.svg?style=for-the-badge
[bstats-url]: https://bstats.org/plugin/bukkit/Custom%20Join%20Messages/6346
[spigot-rating-shield]: https://img.shields.io/spiget/rating/71608.svg?style=for-the-badge
[github-actions-url]: https://nightly.link/Insprill/custom-join-messages/workflows/gradle/develop
[hangar-url]: https://hangar.papermc.io/Insprill/Custom-Join-Messages
[hangar-versions-url]: https://hangar.papermc.io/Insprill/Custom-Join-Messages/versions
[modrinth-url]: https://modrinth.com/plugin/custom-join-messages
[spigot-url]: https://www.spigotmc.org/resources/71608
[stars-shield]: https://img.shields.io/github/stars/Insprill/Custom-Join-Messages.svg?style=for-the-badge
[stars-url]: https://github.com/Insprill/Custom-Join-Messages/stargazers
[issues-shield]: https://img.shields.io/github/issues/Insprill/Custom-Join-Messages.svg?style=for-the-badge
[issues-url]: https://github.com/Insprill/Custom-Join-Messages/issues
[license-shield]: https://img.shields.io/github/license/Insprill/Custom-Join-Messages.svg?style=for-the-badge
[license-url]: https://github.com/Insprill/Custom-Join-Messages/blob/master/LICENSE
[discord-shield]: https://img.shields.io/discord/626995215558901771?color=%235663F7&label=Discord&style=for-the-badge
[discord-url]: https://discord.gg/vjQ3F2XDTc
[spigot-downloads-shield]: https://img.shields.io/spiget/downloads/71608?style=for-the-badge
[patreon-url]: https://www.patreon.com/insprill
[wiki-url]: https://cjm.insprill.net/
[spigotmc-url]: https://www.spigotmc.org/
[papermc-url]: https://papermc.io/
[folia-url]: https://papermc.io/software/folia
[purpurmc-url]: https://purpurmc.org/
[placeholderapi-url]: https://hangar.papermc.io/HelpChat/PlaceholderAPI
[minedown-url]: https://github.com/Phoenix616/MineDown#syntax
[minimessage-url]: https://docs.advntr.dev/minimessage/index.html
[legacy-color-codes-url]: https://minecraft.wiki/w/Formatting_codes#Color_codes
