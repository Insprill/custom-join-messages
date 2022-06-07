The Plugin Config
=================

* language:
   * **Default**: ``en``
   * **Description**: The language used for plugin messages. Your own languages can be added in the ``locale`` folder, and can be used by setting this to the name of the file without the extension.

* Addons:
   * **Description**: Settings related to how CJM interacts with other plugins.
   * AuthMe:
      * **Description**: Settings for AuthMe integration.
      * Use-Login-Event:
         * **Default**: ``true``
         * **Description**: Whether AuthMes login event should be used over the regular join event. When enabled, join messages will only be sent when a player logs in.

   * Vanish:
      * **Description**: Settings for Vanish plugin hooks.
      * Fake-Message:
         * **Description**: Settings for sending fake messages when vanishing/unvanishing.
         * Enabled:
            * **Default**: ``true``
            * **Description**: Whether fake messages should be sent.
         * Vanish:
            * **Default**: ``true``
            * **Description**: Whether fake messages should be sent when vanishing.
         * Unvanish:
            * **Default**: ``true``
            * **Description**: Whether fake messages should be sent when unvanishing.

   * Jail:
      * **Description**: Setting related to jailed players.
      * Send-Messages-For-Jailed-Players:
            * **Default**: ``true``
            * **Description**: Whether messages should be sent from jailed players. **Only works with EssentialsX.**

* World-Based-Messages:
   * **Description**: Settings for world-based messages. If enabled, messages will be sent when changing worlds, excluding changing dimensions of the same world.
   * Enabled:
      * **Default**: ``true``
      * **Description**: Whether world-based messages are enabled.
   * Blacklist-As-Whitelist:
      * **Default**: ``true``
      * **Description**: Whether the blacklist should be treated as a whitelist.
   * Blacklist:
      * **Default**: ``true``
      * **Description**: A list of worlds that should not have messages sent in.

* Version:
   * **Description**: The version of the config. **DO NOT TOUCH THIS VALUE**.
