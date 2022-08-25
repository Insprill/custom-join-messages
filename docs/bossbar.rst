Bossbar Config
================

Example
-------

.. code-block:: yaml

  Messages:
    1:
      Message: "Hello, World!"
      Bar-Color: GREEN
      Bar-Style: SOLID
      Show-Time: 60
      Count-Down: true


Parameters
----------

Message
^^^^^^^
The message to be sent. Only supports a single line.

Bar-Color
^^^^^^^^^
What color the bar should be. `All colors <https://hub.spigotmc.org/javadocs/spigot/org/bukkit/boss/BarColor.html>`_

Bar-Style
^^^^^^^^^
The style of the bar (solid, 6 notches, 12 notches, etc). `All styles <https://hub.spigotmc.org/javadocs/spigot/org/bukkit/boss/BarStyle.html>`_


Show-Time
^^^^^^^^^
How long the bar should be shown, in ticks.

Count-Down
^^^^^^^^^^
Whether the bars progress should go down until it goes away.
