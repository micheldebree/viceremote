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