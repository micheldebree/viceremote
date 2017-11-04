package nl.micheldebree.kickass;

import java.io.IOException;
import java.util.List;

import kickass.plugins.interf.IEngine;
import kickass.plugins.interf.IMemoryBlock;
import kickass.plugins.interf.IModifier;
import kickass.plugins.interf.IValue;
import nl.micheldebree.vice.Connection;

public class ViceWrite implements IModifier {

    @Override
    public String getName() {
        return "ViceWrite";
    }

    @Override
    public byte[] execute(List<IMemoryBlock> blocks, IValue[] options, IEngine engine) {

        if (blocks.size() != 1) {
            engine.error("Can only handle one continuous block of bytes.");
        }

        IMemoryBlock block = blocks.iterator().next();

        if (block.getBytes().length < 1) {
            engine.print("Nothing done for block " + block.getName() + " because it is empty.");
            return block.getBytes();
        }

        String result;
        try {
            result = this.send(block);
        } catch (IOException e) {
            result = e.getMessage();
        }

        StringBuffer logMessage = new StringBuffer("ViceWrite: ");
        logMessage.append(block.getName());
        logMessage.append(" => $");

        logMessage.append(Integer.toHexString(block.getStartAddress()));
        logMessage.append(": ");
        logMessage.append(result);
        engine.printNow(logMessage.toString());
        // return bytes unchanged
        return block.getBytes();

    }

    private String send(IMemoryBlock block) throws IOException {

        Connection connection = new Connection("127.0.0.1", 6510);

        connection.send("> ");
        connection.send(Integer.toHexString(block.getStartAddress()));
        connection.send(" ");
        for (byte oneByte : block.getBytes()) {
            connection.send(Integer.toHexString(oneByte & 0xff));
            connection.send(" ");
        }
        connection.send("\n");

        String response = connection.receive(10);
        connection.close();
        return response;

    }

}
