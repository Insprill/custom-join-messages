# config.yml

- language:

  - **Default**: `en`
  - **Description**: The language used for plugin messages.
  - **Supported Values**:
    - `en` - English

- Addons:

  - **Description**: Settings related to how CJM interacts with other plugins.

  - AuthMe:

    - **Description**: Settings for AuthMe integration.
    - Use-Login-Event:
      - **Default**: `true`
        - **Description**: Whether AuthMe's login event should be used over the regular join event.
          When enabled, join messages will only be sent when a player logs in.

  - Vanish:

    - **Description**: Settings for Vanish plugin hooks.
    - Fake-Message:
      - **Description**: Settings for sending fake messages when vanishing/unvanishing.
      - Enabled:
        - **Default**: `true`
        - **Description**: Whether fake messages should be sent.

  - Jail:

    - **Description**: Setting related to jailed players.
    - Ignore-Jailed-Players:
      - **Default**: `true`
      - **Description**: Whether messages should be sent from jailed players.
        **Only works with EssentialsX and CMI.**

- Formatting

  - **Description**: Settings related to how text is formatted.
  - Formatter:
    - **Default**: `MINEDOWN`

    - **Description**: Which formatter should be used to format text.
      You can choose from the following:

      - `MINEDOWN`
      - `MINIMESSAGE`
      - `LEGACY`

      All options are case-sensitive. For more information
      about formatting, check the
      `formatting`{.interpreted-text role="ref"} page.

- World-Based-Messages:

  - **Description**: Settings for world-based messages. If
    enabled, messages will be sent when changing worlds,
    excluding changing dimensions of the same world.

    - Enabled:

      - **Default**: `true`
      - **Description**: Whether world-based messages are
        enabled.

    - Groups:

      - **Description**: All world groups. Messages will
        only be sent when moving between groups. Children
        keys don't matter as long as there's no
        duplicates. Keys cannot be changed without losing
        reference to the group and essentially resetting it.

    - Ungrouped-Mode:

      - **Default**: `INDIVIDUAL`

      - **Description**: How worlds which aren't in a group
        should be handled. This has three options:

        - `NONE` - No messages will be sent when
          entering/exiting ungrouped worlds.
        - `SAME` - Ungrouped worlds will be treated as if
          they're all one group.
        - `INDIVIDUAL` - Ungrouped worlds will be treated
          as if each worlds is its own group.

        All options are case-sensitive.

- Update-Checker:

  - **Description**: Settings related to the update checker.

  - Enabled:

    - **Default**: `true`
    - **Description**: Whether the update checker is
      enabled.

  - Date-Format:

    - **Default**: `MM-dd-yyyy HH:mm`
    - **Description**: The date format used when
      displaying the release date of a new version.

  - Notifications:

    - **Description**: Settings for how the update checker
      notifies you when there is an update.

    - In-Game:

      - **Default**: `true`
      - **Description**: Whether operators should
        get a chat message upon joining the server
        when an update is available.

    - Console:

      - **Default**: `true`
      - **Description**: Whether a message should be
        sent in console upon startup when an update
        is available.

- Version:

  - **Description**: The version of the config. **DO NOT TOUCH
    THIS VALUE**.
