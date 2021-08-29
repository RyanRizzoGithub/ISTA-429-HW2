
/*
* AUTHOR: Ryan Rizzo
* FILE: PA12Main.java
* ASSIGNMENT: Programming Assignment 12 - Anagrams
* COURSE: CSc 210; Section 001; Spring 2021
* PURPOSE: This program's purpose is to find all anagrams contained
* within a given word.
* 
* ----------------- Whats an Anagram? --------------------------
* An anagram is a word or phrase that can bee formed by 
* rearranging the lterrs of another for.
* For Example....
* "midterm" & "trimmed"
* 
* ------------------ How This is done ---------------------------
* Step 1:
* First we must take in a dictionary of all words and covert it
* into a list. This list can be referenced later
* 
* Step 2:
* Second, we must find all of the words that can be constructed
* using the letters in the given word, and exist in the 
* dictionary.
* 
* Step 3:
* Lastly, a recurive backtracking algorithm finds all anagrams
* of these words that (1) use each letter of the given word (2) 
* use words found in step 2
* 
* ---------------------- Commands -------------------------------
* args[0}
* This first argument must be the name of a .txt file 
* that represents the dictionary of all words to be 
* considered
* 
* args[1]
* This second argument must be the word which which will
* have all anagrams searched for
* 
* args[2]
* This is the integer that represents the maximum number
* of words allowed for each anagram. If this value is 0
* then there is no limit.
* 
* 
* ------------------- Public Methods ------------------------
* Each public method is defined below
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class PA12Main {
	/*
	 * This method is responsible for creating a list of strings containing all of
	 * the words in the dictionary file. -------------------------------------------
	 * 
	 * @ param file, A string representing the file path
	 * 
	 * @ return dic, The list containing all words
	 */
	public static List<String> dicToList(String file) throws FileNotFoundException {
		// Create a new List of strings
        List<String> dic = new LinkedList<String>();
		// Create a file with given string path
        File dicFile = new File(file);
        Scanner reader = new Scanner(dicFile);
		// Read each line of the dictionary
        while (reader.hasNextLine()) {
			// Add the word to the list
            dic.add(reader.nextLine());
        }
        reader.close();
        return dic;
    }

	/*
	 * The purpose of this method is to find all words that can be created, using
	 * the letters in the given word, which also exists in the dictionary list
	 * ----------------------------------------------------
	 * 
	 * @ param dic, The List of Strings containing all words in the dictionary
	 * 
	 * @ param compare, This is the word given by command line arg, which all
	 * anagrams will be found for
	 * 
	 * @ return found, A list of all found words
	 */
    public static List<String> findWords(List<String> dic, String compare) {
		// Create a new empty list to hold all found words
        List<String> found = new LinkedList<String>();
		// Create Letter Inventory object of given word
        LetterInventory word = new LetterInventory(compare);
		// Look at each word in the dictionary and use the
		// letter inventory to determine of it can be added
		for (String words : dic) {
			if (word.contains(words)) {
				found.add(words);
            }
        }
		return found;
	}

	/*
	 * The purpose of this method is to find all the anagrams of the given word. It
	 * does this by using recurisve backtracking. This method also makes use of a
	 * helper method. ----------------------------------------------------
	 * anagrams()....
	 * 
	 * @ param word, the word for which the anagrams will be found
	 * 
	 * @ param choices, the list of all words which can be made using the letters in
	 * word
	 * 
	 * @ param numWords, the maximum number of words allowed in each anagram
	 * 
	 * @ return anagramHelper()
	 * 
	 * anagramsHelper()....
	 * 
	 * @ param toUse, the LetterInventory object of the given word. This represents
	 * each letter in the word
	 * 
	 * @ param choices, the list of all words which can be made using the letters in
	 * word
	 * 
	 * @ param choosen, this is a list that represents all words which have been
	 * added to the anagram so far
	 * 
	 * @ param numWords, the maximum number of words allowed in each anagram
	 * 
	 * @ param anagrams, this is a list of lists. Each list represents a full
	 * anagram of the given word
	 * 
	 * @ return anagrams, see above
	 */
	public static List<List<String>> anagrams(String word, List<String> choices, int numWords) {
		// Create a letter inventory of the given word
		LetterInventory toUse = new LetterInventory(word);
		// Create a new empty list to contain the choosen words
		List<String> choosen = new LinkedList<String>();
		// Create an empty list of lists to hold all anograms
		List<List<String>> anagrams = new LinkedList<List<String>>();
		// Call the helper method
		return anagramsHelper(toUse, choices, choosen, numWords, anagrams);
	}

	private static List<List<String>> anagramsHelper(LetterInventory toUse,
			List<String> choices, List<String> choosen, int numWords,
			List<List<String>> anagrams) {

		// Check if the letter inventory is out of letters
		if (toUse.isEmpty()) {
			// If so we can check if we found a proper anagram
			if (numWords >= choosen.size() || numWords == 0) {
				// Add anagram to thge list
				anagrams.add(choosen);
			} else {
				return anagrams;
			}
		}
		else {
			// Iterate over all of the choices
			for (String choice : choices) {
				// Check if the letter inventory contains
				// the correct letters for the choice
				if (toUse.contains(choice)) {
					// Crete a new letter inventory with reduced letters
					LetterInventory nextToUse = new LetterInventory(toUse.toString());
					// Create a new list of choosen words, with new choice added
					List<String> nextChoose = new LinkedList<String>();
					for (String i : choosen) {
						nextChoose.add(i);
					}
					nextToUse.subtract(choice);
					nextChoose.add(choice);
					// Recursive call with new toUse, and choosen
					anagramsHelper(nextToUse, choices, nextChoose, numWords, anagrams);
				}
			}
		}
		return anagrams;
    }

	/*
	 * This method is responsible for creating a String output representation of our
	 * anagram. This includes the phrase which needs to be scrambled. All of the
	 * words that were found in the findWords() method, and all of the anagrams
	 * found in the anagrams() method
	 * -------------------------------------------------------
	 * 
	 * @ param word, the word that is being scrambled
	 * 
	 * @ param choosen, all of the words that can be created using the choosen word
	 * 
	 * @ param anagrams, a list of lists containsing all anagrams of the choosen
	 * word.
	 */
	public static String toString(String word, List<String> choosen,
			List<List<String>> anagrams) {
		// crreate a new string
        String output = new String("");

		// Add the word being scrambled
		output += "Phrase to scramble: " + word + "\n\n";
		// Add the words that can be created
		output += "All words found in " + word + ":\n";
		output += choosen + "\n\n";
		// Add all the anagrams
		output += "Anagrams for " + word + ":\n";
		for (List<String> anagram : anagrams) {
			output += anagram + "\n";
		}
        return output;
    }

    public static void main(String[] args) throws FileNotFoundException {
		// Create variables for the command line arguments
        String fileName = args[0];
        String choosenWord = args[1];
        int numWords = Integer.parseInt(args[2]);

		// Convert dictionary file to a list
		List<String> dic = dicToList(fileName);
		// Find all the words that can be created
        List<String> choices = findWords(dic, choosenWord);
		// Find all the anagrams
		List<List<String>> anagrams = anagrams(choosenWord, choices, numWords);
		// Create a string for output
		String output = toString(choosenWord, choices, anagrams);
		System.out.println(output);
    }
}
