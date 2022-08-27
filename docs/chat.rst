Chat Config
===========

Example
-------

.. code-block:: yaml

  Messages:
    1:
      - 'Hello, world!'
      - '{"text":"This is a JSON message!"}'


Parameters
----------

Chat messages don't have any message-specific parameters. The messages are simply added as a list directly under the index.
All lines will be sent at once, but in order.

Clickable & Hoverable Messages
~~~~~~~~~~~~~

Chat messages can be written to perform certain action when clicked on, or display text when hovered over.
For more information on how to do this, check out the :ref:`formatting` page.

Centered Messages
~~~~~~~~~~~~~~~~~
Messages can be roughly centered in the chat box by prefixing the message with ``center:``. This prefix will be removed before being sent.
