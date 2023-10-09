.. _formatting:

Formatting
==========

CJM offers the unique ability to choose which formatter you'd like to use for formatting messages.
The formatter you want to use must be set in the :ref:`config.yml`.
It currently has three options:

* MineDown_
* MiniMessage_
* Legacy_

The three options are *not* compatible with each other, and you may only use one at a time.

If you have any placeholders that aren't pre-formatted, they will be run through this formatter.
This means if you have placeholders with legacy color codes, you'll need to use the legacy formatter.

Minedown
~~~~~~~~

MineDown is loosely based on Markdown_,
making it easy to create complex-styled messages with gradients, clickable/hoverable messages, and more.

`MineDown Syntax`_.

MiniMessage
~~~~~~~~~~~

MiniMessage is a simple formatter designed to be easy for end users to learn.
If you use Paper_ (which you should) you'll recognize it from their configuration files on modern Minecraft versions.
Like MineDown, it support the creation of complex-styled messages with gradients, clickable/hoverable messages, and more.

`MiniMessage Syntax`_.

Legacy
~~~~~~

The legacy syntax (``&`` codes) is the one everyone should be familiar with.
It also has support for three special styles of HEX colors, being ``{#000000}``, ``<#000000>``, and ``&#000000``.

`Legacy Syntax`_.


.. _MineDown: https://github.com/Phoenix616/MineDown
.. _MineDown Syntax: https://github.com/Phoenix616/MineDown#syntax
.. _MiniMessage: https://docs.adventure.kyori.net/minimessage/index.html
.. _MiniMessage Syntax: https://docs.adventure.kyori.net/minimessage/format.html
.. _Legacy: https://minecraft.wiki/w/Formatting_codes
.. _Legacy Syntax: https://minecraft.wiki/w/Formatting_codes

.. _Markdown: https://www.markdownguide.org/getting-started/#what-is-markdown
.. _Paper: https://github.com/PaperMC/Paper
