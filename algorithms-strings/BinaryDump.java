/******************************************************************************
 * java BinaryDump n < abra.txt
 *  Reads in a binary file and writes out the bits, n per line.
 *  % java BinaryDump 16 < abra.txt
 *  0100000101000010
 *  0101001001000001
 *  0100001101000001
 *  0100010001000001
 *  0100001001010010
 *  0100000100100001
 *
 ******************************************************************************/

/**
 The BinaryDump class provides a client for displaying the contents of a binary file in binary.
 For more full-featured versions, see the Unix utilities od (octal dump) and hexdump (hexadecimal dump).
 */
public class BinaryDump {

    // Do not instantiate.
    private BinaryDump() { }

    /**
     * Reads in a sequence of bytes from standard input and writes
     * them to standard output in binary, k bits per line,
     * where k is given as a command-line integer (defaults
     * to 16 if no integer is specified); also writes the number
     * of bits.
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        int bitsPerLine = 16;
        if (args.length == 1) {
            bitsPerLine = Integer.parseInt(args[0]);
        }

        int count;
        for (count = 0; !BinaryStdIn.isEmpty(); count++) {
            if (bitsPerLine == 0) {
                BinaryStdIn.readBoolean();
                continue;
            }
            else if (count != 0 && count % bitsPerLine == 0) System.out.println();
            if (BinaryStdIn.readBoolean()) System.out.print(1);
            else                           System.out.print(0);
        }
        if (bitsPerLine != 0) System.out.println();
        System.out.println(count + " bits");
    }
}
