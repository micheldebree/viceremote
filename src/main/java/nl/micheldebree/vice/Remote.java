package nl.micheldebree.vice;

import java.io.IOException;

public class Remote {

    public static void main(String[] args) {

        Connection connection;

        if (args.length != 1) {
            System.out.println("USAGE: ViceRemote \"<VICE monitor command>\"");
            System.out.println("See http://vice-emu.sourceforge.net/vice_12.html#SEC29");
            System.exit(1);
        }

        try {
            connection = new Connection("127.0.0.1", 6510);
            connection.send(args[0]);
            connection.send("\n");
            System.out.println("ViceRemote: \"" + args[0] + "\": " + connection.receive(10));
            connection.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

}


