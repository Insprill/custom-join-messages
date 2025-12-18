# Sound "Messages"

## Example

```yaml
Sounds:
  1:
    Sound: "ENTITY_PLAYER_LEVELUP"
    Volume: 10.0
    Pitch: 1.0
```

## Parameters

### Sound

The sound to play. A full list of sounds can be found
[Here](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Sound.html),
or they can be specified the vanilla way with namespaces. The minecraft
namespace is assumed if none is specified.

### Volume

How loud the sound should be.

### Pitch

The pitch of the sound.

### Global

Whether the sound should be played only to the player or in the world.
