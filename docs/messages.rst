The Message Config
==================

Messages are very flexible and can be configured in a variety of ways.
All message types follow the same basic structure:

.. code-block:: yaml

  Enabled:
  Visibility:
    Action:
      Group Index:
        Global Parameters:
        Type-Specific Message Holder:
          Message Index:
            Type-Specific Parameters:

Enabled
^^^^^^^
Whether the message type is enabled.


Visibility
^^^^^^^^^^
Message visibility is who will see the message. There is two types:

* **Public** The message is visible to all users.
* **Private** The message is visible to the player joining.


Action
^^^^^^
The action is when the message is displayed. There are three types:

* **First-Join**: The player joins for the first time.
* **Join**: The player joins having played before.
* **Quit**: The player leaves the server. This does not work with the ``Private`` visibility.


Group Index
^^^^^^^^^^^
The group index is a unique identifier for the group of messages.
The identifier **must** be an integer. If it's not, the message will not work.
If a player has permissions for multiple groups, the group with the highest index will be used.
The indexes don't `need` to be sequential, but they must be unique per-action.


Global Parameters
^^^^^^^^^^^^^^^^^
Parameters are used to further configure when and to who the message is displayed.

* **Permission**: The permission needed for this message to be displayed.
* **Radius**: The radius around the player to display the message. Set to ``-1`` to disable.
* **Max-Players**: The maximum number of players online in which the message will be sent. Set to ``-1`` to disable.
* **Min-Players**: The minimum number of players online in which the message will be sent. Set to ``-1`` to disable.

Message Index
^^^^^^^^^^^^^
The message index is a unique identifier for the message. When there is more than one message, CJM will pick a random one to send.
