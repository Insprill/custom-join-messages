Chat Messages
=============

Example
-------

.. code-block:: yaml

  Messages:
    1:
      - "Hello, world!"
      - "[This is a clickable & hoverable message!](https://google.com/ When clicked I'll go to google, and when hovered I'll show this!)"


Parameters
----------

Chat messages don't have any message-specific parameters. The messages are simply added as a list directly under the index.
All lines will be sent at once, in order.


Clickable & Hoverable Messages
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Chat messages can be written to perform certain action when clicked on, or display text when hovered over.
For more information on how to do this, check out the :ref:`formatting` page.


Centered Messages
~~~~~~~~~~~~~~~~~

Messages can be roughly centered in the chat box by prefixing the message with ``center:``. This prefix will be removed before being sent.


Console Messages
~~~~~~~~~~~~~~~~

By default, public chat messages will be sent to the servers console. This can be disabled by setting ``Public.Send-To-Console`` to false.
