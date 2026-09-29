Toggling Messages
=================

Custom Join Messages has a powerful system for toggling which players receive which messages,
without having to change your configuration.

The ``cjm.command.toggle`` permission will give players access to the three toggle commands:

* ``/cjm toggleAction`` Toggles all messages for a certain action (first join, join, quit).
* ``/cjm toggleType`` Toggles all messages of a certain type (chat, title, sound, etc).
* ``/cjm toggleActionType`` Toggles all messages of a certain combination of action and type (quit sounds, first join title, etc).

Instead of providing on/off/toggle to change the behaviour, you may provide ``status`` to see the current toggled state.
If you're running the command from the server console, you may also provide a player name/uuid to toggle something for them.

These toggles may also be operated via permissions.
All view permissions start with ``cjm.view``, followed by an action, a type, or an action and a type.
Here is an example of each:

* ``cjm.view.join``
* ``cjm.view.chat``
* ``cjm.view.quit.sound``

Those are not the only options, you can replace the action and type with whichever you wish to toggle.
All view permissions are granted by default, and it's up to your permission manager to explicitly revoke them
if you wish to toggle off a message.

With the console command or permissions, you may integrate these options into a GUI using another plugin.
