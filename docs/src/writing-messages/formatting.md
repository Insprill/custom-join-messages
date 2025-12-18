# Formatting

CJM offers the unique ability to choose which formatter you'd like to
use for formatting messages. The formatter you want to use must be set
in the `config.yml`{.interpreted-text role="ref"}. It currently has
three options:

- [MineDown](https://github.com/Phoenix616/MineDown)
- [MiniMessage](https://docs.adventure.kyori.net/minimessage/index.html)
- [Legacy](https://minecraft.wiki/w/Formatting_codes)

The three options are *not* compatible with each other, and you may only
use one at a time.

If you have any placeholders that aren't pre-formatted, they will be
run through this formatter. This means if you have placeholders with
legacy color codes, you'll need to use the legacy formatter.

## Minedown

MineDown is loosely based on
[Markdown](https://www.markdownguide.org/getting-started/#what-is-markdown),
making it easy to create complex-styled messages with gradients,
clickable/hoverable messages, and more.

[MineDown Syntax](https://github.com/Phoenix616/MineDown#syntax).

## MiniMessage

MiniMessage is a simple formatter designed to be easy for end users to
learn. If you use [Paper](https://github.com/PaperMC/Paper) (which you
should) you'll recognize it from their configuration files on modern
Minecraft versions. Like MineDown, it supports the creation of
complex-styled messages with gradients, clickable/hoverable messages,
and more. Our version of MiniMessage also support basic legacy color
codes to help ease the transition.

[MiniMessage
Syntax](https://docs.adventure.kyori.net/minimessage/format.html).

## Legacy

The legacy syntax (`&` codes) is the one everyone should be familiar
with. It also has support for three special styles of HEX colors, being
`{#000000}`, `<#000000>`, and `&#000000`.

[Legacy Syntax](https://minecraft.wiki/w/Formatting_codes).
