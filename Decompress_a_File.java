/*
 * 
 * 
 * @author
 */

public class Decompress_a_File {
	public static void main(String[] args) {
		/** Checks arguments and gets file names if valid */
		if (args.length != 2) {
			System.out.println("Usage: java Decompress_a_File compressedFile.txt decompressedFile.txt");
			System.exit(1);
		}
	}
	
	/**
	 * Get Huffman codes for the characters This method is called once after a
	 * Huffman tree is built
	 */
	public static String[] getCode(Tree.Node root) {
		if (root == null)
			return null;
	    String[] codes = new String[2 * 128];
	    assignCode(root, codes);
	    return codes;
	}

	/* Recursively get codes to the leaf node */
	private static void assignCode(Tree.Node root, String[] codes) {
		if (root.left != null) {
			root.left.code = root.code + "0";
			assignCode(root.left, codes);

			root.right.code = root.code + "1";
			assignCode(root.right, codes);
		} else {
			codes[(int) root.element] = root.code;
	    }
	}

	/** Get a Huffman tree from the codes */
	public static Tree getHuffmanTree(int[] counts) {
	// Create a heap to hold trees
		Heap<Tree> heap = new Heap<Tree>(); // Defined in Listing 24.10
	    for (int i = 0; i < counts.length; i++) {
	    	if (counts[i] > 0)
	    		heap.add(new Tree(counts[i], (char) i)); // A leaf node tree
	    }

	    while (heap.getSize() > 1) {
	    	Tree t1 = heap.remove(); // Remove the smallest weight tree
	    	Tree t2 = heap.remove(); // Remove the next smallest weight
	    	heap.add(new Tree(t1, t2)); // Combine two trees
	    }
	    return heap.remove(); // The final tree
	}

	/** Get the frequency of the characters */
	public static int[] getCharacterFrequency(String text) {
		int[] counts = new int[256]; // 256 ASCII characters
	    for (int i = 0; i < text.length(); i++)
	    	counts[(int) text.charAt(i)]++; // Count the character in text

	    return counts;
	}
	 
	 /** Define a Huffman coding tree */
	public static class Tree implements Comparable<Tree> {
		Node root; // The root of the tree

		/** Create a tree with two subtrees */
	  	public Tree(Tree t1, Tree t2) {
	   		root = new Node();
	   		root.left = t1.root;
	   		root.right = t2.root;
	   		root.weight = t1.root.weight + t2.root.weight;
	  	}
		
		/** Create a tree containing a leaf node */
		public Tree(int weight, char element) {
			root = new Node(weight, element);
		}

		@Override
		/** Compare trees based on their weights */
		public int compareTo(Tree t) {
			if (root.weight < t.root.weight) // Purposely reverse the order
				return 1;
			else if (root.weight == t.root.weight)
				return 0;
			else
				return -1;
		}
		
		public class Node {
			char element; // Stores the character for a leaf node
			int weight; // weight of the subtree rooted at this node
			Node left; // Reference to the left subtree
			Node right; // Reference to the right subtree
			String code = ""; // The code of this node from the root

			/** Create an empty node */
			public Node() {
			}

			/** Create a node with the specified weight and character */
			public Node(int weight, char element) {
				this.weight = weight;
				this.element = element;
			}
		}	
	}
	
	static class BitOutputStream {
		// a private variable of type OutputStream is declared
		private OutputStream out;

		//An array of type Boolean is created.
		private boolean[] buffer = new boolean[8];

		//A private valriable of type int is declared.
		private int count = 0;

		//The function to assign the output of the outputstream is     declared.
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
