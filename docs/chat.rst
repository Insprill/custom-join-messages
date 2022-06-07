Chat Config
===========

Example
--------------

.. code-block:: yaml

  Messages:
    1:
      - 'Hello, world!'
      - '{"text":"This is a JSON message!"}'


Parameters
----------

Chat messages don't have any message-specific parameters.
The messages and simply added directly under the index.

All lines will be sent at once, but in order.

JSON messages are supported using the ``tellraw`` command format.
`Here <https://minecraft.tools/en/tellraw.php>`_ you can find a generator for these messages.
When added to the list, it **must** be surrounded by *single* quotes (``'``).
