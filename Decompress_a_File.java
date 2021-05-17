import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;

/*
 * 
 * 
 * @author Paul Tran, Xinyi Xu
 */

public class Decompress_a_File {
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		/** Checks arguments */
		if (args.length != 2) {
			System.out.println("Usage: java Decompress_a_File compressedFile.txt decompressedFile.txt");
			System.exit(1);
		}
		
		/** Checks if file exists */
		File compressedFile = new File(args[0]);
		if (!compressedFile.exists()) {
			System.out.println("File " + args[0] + " does not exist");
			System.exit(2);
		}
		
		/** Opens Input Streams to read file */
		FileInputStream input = new FileInputStream(args[0]);
		ObjectInputStream objectInput = new ObjectInputStream(input);
		
		/** Stores Huffman codes for each ASCII character in an array */
		String[] codes = (String[])(objectInput.readObject()); 
		int sizeOfData = objectInput.readInt();
		
		/** Creates Stringbuilder to store bits read from compressed source file */
		StringBuilder compressedText = new StringBuilder("");
		int bit = 0;
		while ((bit = input.read()) != -1) {
			compressedText.append(getBits(bit)); //
		}
		input.close();
		compressedText.delete(sizeOfData, compressedText.length()); // Trims bits
		
		/** Builds decompressed text */
		StringBuilder decompressedText = new StringBuilder();
		while(compressedText.length() != 0) {
			/** Goes through list of prefix codes for each character */
			for (int i = 0; i < codes.length; i++) {
			    if((codes[i] != null) && (compressedText.indexOf(codes[i]) == 0)) {
			    	/** Removes character prefix code from text after appending decoded character */
			    	decompressedText.append((char)i);
			    	compressedText.delete(0, codes[i].length()); 
			    	break;
			    }
			}
		}

		BufferedWriter writer = new BufferedWriter(new FileWriter(args[1])); // Create new BufferedWriter and new file to write to
		writer.append(decompressedText); // Writes text to new file
		writer.close();
	}
	
	/** Retrieves bits from Huffman encoded text */
	public static String getBits(int value) {
		String bit = "";
		value %= 256;
		int i = 0;
		int temp = value >> i;
		for (int j = 0; j < 8; j++) {
			bit = (temp & 1) + bit;
		    i++;
		    temp = value >> i;
		}
		return bit;
	}
}
