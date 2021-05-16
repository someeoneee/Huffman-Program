import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.BufferedWriter;

/*
 * 
 * 
 * @author
 */

public class Decompress_a_File {
	public static void main(String[] args) {
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
		
		/** Creates FileInputStream to read file */
		FileInputStream input = new FileInputStream(args[0]);
		
		/** Creates Stringbuilder to store text read from the source file */
		StringBuilder text = new StringBuilder("");
		int bit = 0;
		while ((bit = input.read()) != -1) {
			text.append(bit); //FOR NOW IT APPENDS ASCII VALUE 
		}
		input.close();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));
  		writer.append(text);
  		writer.close();
	}
	
	static class BitOutputStream {
		// a private variable of type OutputStream is declared
		private OutputStream out;

		//An array of type Boolean is created.
		private boolean[] buffer = new boolean[8];

		//A private valriable of type int is declared.
		private int count = 0;

		//The function to assign the output of the outputstream is declared.
		public BitOutputStream(OutputStream out) {
			this.out = out;
		}

		//A method write is defined which is used by a file to read bit by bit.
		public void write(boolean x) throws IOException {
			//the counter is incremented.
		    this.count++;

		    //value is assigned to the buffer
		    this.buffer[8-this.count] = x;
		    
		    //if condition executes
		    if (this.count == 8){
		    	int num = 0;
		    	
		        //for loop to parse the stream
		        for (int index = 0; index < 8; index++) {
		            num = 2*num + (this.buffer[index] ? 1 : 0);
		        }

		        //method is called to write into a stream
		        this.out.write(num - 128);
		        this.count = 0;
		    }
		}

		//The file is closed.
		public void close() throws IOException {

			//declare the variable of type int
		    	int num = 0;

		    	//for loop to parse through the stream
		    	for (int index = 0; index < 8; index++){
		    		num = 2*num + (this.buffer[index] ? 1 : 0);
		    	}

		    	//write method is called.
		    	this.out.write(num - 128);

		    	//close method is called.
		    	this.out.close();
		}

		//method for decompression is created.
		public static String decompress(String s) {
			//String objects are created.
			String temp = new String();
			String result = new String();
		
			//loop through the string
			for(int i = 0; i < s.length(); i++) {
				temp = temp + s.charAt(i);
				//the logic for decoding
				Character c = codeToChar.get(temp);
				if(c!=null && c!=0) {
					result = result + c;
					temp = "";
				}
			}
		
			//decoded value is returned.
			return result;
		 }
	}
}
