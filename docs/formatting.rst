.. _formatting:

Formatting
==========

CJM offers the unique ability to choose which formatter you'd like to use for formatting messages.
It currently has three options:

* `MineDown <https://github.com/Phoenix616/MineDown>`_
* `MiniMessage <https://docs.adventure.kyori.net/minimessage/index.html>`_
* `Legacy <https://minecraft.fandom.com/wiki/Formatting_codes>`_

The three options are *not* compatible with each other, and you may only use one at a time.

If you have any placeholders that aren't pre-formatted, they will be run through this formatter.
This means if you have placeholders with legacy color codes, you'll need to use the legacy formatter.

Minedown
~~~~~~~~

MineDown is loosely based on `Markdown <https://www.markdownguide.org/getting-started/#what-is-markdown>`_,
making it easy to create complex-styled messages with gradients, clickable/hoverable messages, and more.

`MineDown Syntax <https://github.com/Phoenix616/MineDown#syntax>`_.

MiniMessage
~~~~~~~~~~~

MiniMessage is a simple formatter designed to be easy for end users to learn.
If you use `Paper <https://github.com/PaperMC/Paper>`_ (which you should) you'll recognize it from their configuration files on modern Minecraft versions.

`MiniMessage Syntax <https://docs.adventure.kyori.net/minimessage/format.html>`_.

Legacy
~~~~~~

The legacy syntax (``&`` codes) is the one everyone should be familiar with. It also has support for three special styles of HEX colors, being ``{#000000}``, ``<#000000>``, and ``#&000000``.

`Legacy Syntax <https://minecraft.fandom.com/wiki/Formatting_codes>`_.
