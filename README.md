# ViceRemote

Tools to help with quick roundtrip development using [Kick Assembler](http://www.theweb.dk/KickAssembler) and the [Vice Commodore 64 emulator](http://vice-emu.sourceforge.net), by sending commands to the remote monitor of a running Vice instance.

They are designed to fail fast and not interfere with your build process if Vice happens not to be running.

For these tools to be of use, Vice needs to be running with the [remote monitor](http://vice-emu.sourceforge.net/vice_6.html#SEC84) enabled. For example:

```sh
x64 -remotemonitor
```

## Kick Assembler modifier: ViceWrite

The ViceWrite modifier allows you to quickly roundtrip your code changes by injecting compiled bytes straight into the memory of a running Vice emulator.

Wrap the code you want to inject in the ``ViceWrite`` modifier like so:

```asm
.plugin "nl.micheldebree.kickass.ViceWrite"

* = $0810

.modify ViceWrite() {

  // notice the program counter inside the modifier
  // aswell as outside. this is a must!
  * = $0810

  jsr $ff81

loop:
  inc $d020
  jmp loop

}
```

Make sure the ``viceremote-x.y.z.jar`` is on your classpath when you run ``KickAss.jar``.

## Command line tool: ViceRemote

This sends a monitor command to a running Vice instance. This way you can incorporate Vice monitor commands into your build process. For example resetting the Commodore 64 before compilation, and running your code after compilation:

```sh
java -jar viceremote-0.1.0.jar "reset 1"
```

Resets Vice

```sh
java -jar viceremote-0.1.0.jar "g 0810"
```

Jumps to $0810

See [Vice monitor command reference](http://vice-emu.sourceforge.net/vice_12.html#SEC29) for more commands.

## Examples

An example build script:

```sh
java -jar viceremote-0.1.0.jar "reset 1"
java -cp viceremote-0.1.0.jar:KickAss.jar cml.kickass.KickAssembler test.asm
java -jar viceremote-0.1.0.jar "g 0810"
```

- Reset the running Vice emulator
- Compile and inject code straight into Vice
- Jump to $0810 to start the code
