import java.io.*;
import java.util.Scanner;
public class BankAccount_001 {

	/**
	 * 
	 *@author Mark Goldstein
	 *@version 0.01
	 *@date 9/5/2018 
	 * 
	 *
*/

		public static void main(String[] args) throws IOException {
			// constant definitions
			final int MAX_NUM = 50;

			// variable declarations
			int[] acctNumArray = new int[MAX_NUM]; // array of account numbers
			double[] balanceArray = new double[MAX_NUM]; // array of balances
			int numAccts; // number of accounts
			char choice; // menu item selected
			boolean not_done = true; // loop control flag

			// open input test cases file
			// File testFile = new File("C:\\Users\\Mark\\");
			// File testFile = new File("mytestcases.txt");
			File testFile = new File("mytestcases.txt");

			// create Scanner object
			Scanner kybd = new Scanner(testFile);
			// Scanner kybd = new Scanner(System.in);

			// open the output file
			// PrintWriter outFile = new
			// PrintWriter("/bc/cisc3115/pgms/chapter_00/prj00_01BankAccounts/myoutput.txt");
			PrintWriter outFile = new PrintWriter("myoutput.txt");
			// PrintWriter outFile = new PrintWriter(System.out);

			/* first part */
			/* fill and print initial database */
			numAccts = readAccts(acctNumArray, balanceArray, MAX_NUM);
			outFile.println("NAME:Mark Goldstein");
			printAccts(acctNumArray, balanceArray, numAccts, outFile);

			/* second part */
			/* prompts for a transaction and then */
			/* call functions to process the requested transaction */
			do {
				menu();
				choice = kybd.next().charAt(0);
				switch (choice) {
				case 'q':
				case 'Q':
					not_done = false;
					outFile.println("Final Database of Accounts:");
					printAccts(acctNumArray, balanceArray, numAccts, outFile);
					break;
				case 'b':
				case 'B':
					balance(acctNumArray, balanceArray, numAccts, outFile, kybd);
					break;
				case 'd':
				case 'D':
					deposit(acctNumArray, balanceArray, numAccts, outFile, kybd);
					break;
				case 'w':
				case 'W':
					withdrawal(acctNumArray, balanceArray, numAccts, outFile, kybd);
					break;
				case 'n':
				case 'N':
					numAccts = newAcct(acctNumArray, balanceArray, numAccts, outFile, kybd);
					break;
				case 'x':
				case 'X':
					numAccts = deleteAcct(acctNumArray, balanceArray, numAccts, outFile, kybd);
					break;
				default:
					outFile.println("Error: " + choice + " is an invalid selection -  try again");
					outFile.println();
					outFile.flush();
					break;
				}
				// give user a chance to look at output before printing menu
				// pause(kybd);
			} while (not_done);

			// close the output file
			outFile.close();

			// close the test cases input file
			kybd.close();

			System.out.println();
			System.out.println("The program is terminating");
		}

		/*
		 * Method readAccts(): Input: acctNumArray - reference to array of account
		 * numbers balanceArray - reference to array of account balances maxAccts -
		 * maximum number of active accounts allowed Process: Reads the initial database
		 * of accounts and balances Output: Fills in the initial account and balance
		 * arrays and returns the number of active accounts
		 */
		public static int readAccts(int[] acctNumArray, double[] balanceArray, int maxAccts) throws IOException {
			// open database input file
			// create File object
			// File dbFile = new
			// File("/bc/cisc3115/pgms/chapter_00/prj00_01BankAccounts/myinput.txt");
			File dbFile = new File("myinput.txt");

			// create Scanner object
			Scanner sc = new Scanner(dbFile);

			int count = 0; // account number counter

			while (sc.hasNext() && count < maxAccts) {
				acctNumArray[count] = sc.nextInt();
				balanceArray[count] = sc.nextDouble();
				count++;
			}

			// close the input file
			sc.close();

			// return the account number count
			return count;
		}

		/*
		 * Method printAccts(): Input: acctNumArray - array of account numbers
		 * balanceArray - array of account balances numAccts - number of active accounts
		 * outFile - reference to the output file Process: Prints the database of
		 * accounts and balances Output: Prints the database of accounts and balances
		 */
		public static void printAccts(int[] acctNumArray, double[] balanceArray, int numAccts, PrintWriter outFile) {
			outFile.println();
			outFile.println("\t\tDatabase of Bank Accounts");
			outFile.println();
			outFile.println("Account   Balance");
			for (int index = 0; index < numAccts; index++) {
				outFile.printf("%7d  $%7.2f", acctNumArray[index], balanceArray[index]);
				outFile.println();
			}
			outFile.println();

			// flush the output file
			outFile.flush();
		}

		/*
		 * Method menu(): Input: none Process: Prints the menu of transaction choices
		 * Output: Prints the menu of transaction choices
		 */
		public static void menu() {
			System.out.println();
			System.out.println("Select one of the following transactions:");
			System.out.println("\t****************************");
			System.out.println("\t    List of Choices         ");
			System.out.println("\t****************************");
			System.out.println("\t     W -- Withdrawal");
			System.out.println("\t     D -- Deposit");
			System.out.println("\t     N -- New Account");
			System.out.println("\t     B -- Balance Inquiry");
			System.out.println("\t     X -- Delete Account");
			System.out.println("\t     Q -- Quit");
			System.out.println();
			System.out.print("\tEnter your selection: ");
		}

		/*
		 * Method findAcct(): Input: acctNumArray - array of account numbers numAccts -
		 * number of active accounts requestedAccount - requested account
		 * requested_number Process: Performs a linear search on the acctNunArray for
		 * the requested account Output: If found, the index of the requested account is
		 * returned Otherwise, returns -1
		 */
		public static int findAcct(int[] acctNumArray, int numAccts, int requestedAccount) {
			for (int index = 0; index < numAccts; index++)
				if (acctNumArray[index] == requestedAccount)
					return index;
			return -1;
		}

		/*
		 * Method balance(): Input: acctNumArray - array of account numbers balanceArray
		 * - array of account balances numAccts - number of active accounts outFile -
		 * reference to output file kybd - reference to the "test cases" input file
		 * Process: Prompts for the requested account Calls findAcct() to see if the
		 * account exists If the account exists, the balance is printed Otherwise, an
		 * error message is printed Output: If the account exists, the balance is
		 * printed Otherwise, an error message is printed
		 */
		public static void balance(int[] acctNumArray, double[] balanceArray, int numAccts, PrintWriter outFile,
				Scanner kybd) {
			int requestedAccount;
			int index;
			String accLength; // Sets up new account as string to ensure validity
			System.out.println();
			System.out.print("Enter the account number: ");
			if (kybd.hasNextInt()) {
				accLength = kybd.next();
				if (accLength.length() != 6 || Integer.parseInt(accLength) < 100000) {
					outFile.println("Transaction Requested: Balance");
					outFile.printf("Error: Account number entered invalid!"
							+ "\nAccount numbers must be a 6-digit integer "
							+ "\nbetween 100000 and 999999.\n\n");

				} else {
					requestedAccount = Integer.parseInt(accLength);
					// call findAcct to search if requestedAccount exists
					index = findAcct(acctNumArray, numAccts, requestedAccount);
					if (index == -1) // invalid account
					{
						outFile.println("Transaction Requested: Balance Inquiry");
						outFile.println("Error: Account number " + requestedAccount + " does not exist.");
					} else // valid account
					{
						outFile.println("Transaction Requested: Balance Inquiry");
						outFile.println("Account Number: " + requestedAccount);
						outFile.printf("Current Balance: $%.2f", balanceArray[index]);
						outFile.println();
					}
				}
			} else { // Message for invalid account number entered
				accLength = kybd.next();
				outFile.println("Transaction Requested: Balance");
				outFile.printf("Error: Account number entered invalid!\nAccount numbers must be a 6-digit integer "
						+ "\nbetween 100000 and 999999.\n\n");
			}
			outFile.println();
			outFile.flush(); // flush the output buffer
			printAccts(acctNumArray, balanceArray, numAccts, outFile);
		}

		/*
		 * Method deposit(): Input: acctNumArray - array of account numbers balanceArray
		 * - array of account balances numAccts - number of active accounts outFile -
		 * reference to the output file kybd - reference to the "test cases" input file
		 * Process: Prompts for the requested account Calls findacct() to see if the
		 * account exists If the account exists, prompts for the amount to deposit If
		 * the amount is valid, it makes the deposit and prints the new balance
		 * Otherwise, an error message is printed Output: For a valid deposit, the
		 * deposit transaction is printed Otherwise, an error message is printed
		 */
		public static void deposit(int[] acctNumArray, double[] balanceArray, int numAccts, PrintWriter outFile,
				Scanner kybd) {
			int requestedAccount;
			int index;
			double amountToDeposit;
			String accLength; // Sets up account as string to ensure validity

			System.out.println();
			System.out.print("Enter the account number: ");
			if (kybd.hasNextInt()) {
				accLength = kybd.next();

				if (accLength.length() != 6 || Integer.parseInt(accLength) < 100000) {
					outFile.println("Transaction Requested: Deposit");
					outFile.printf("Error: Account number entered invalid!\nAccount numbers must be a 6-digit integer "
							+ "\nbetween 100000 and 999999.\n\n");
				} // prompt for account number
				requestedAccount = Integer.parseInt(accLength); // read-in the account number

				// call findAcct to search if requestedAccount exists
				index = findAcct(acctNumArray, numAccts, requestedAccount);

				if (index == -1) // invalid account
				{
					outFile.println("Transaction Requested: Deposit");
					outFile.println("Error: Account number " + requestedAccount + " does not exist.");
				} else // valid account
				{
					System.out.println("Enter amount to deposit: "); // prompt for amount to deposit
					if (kybd.hasNextDouble()) {
						amountToDeposit = kybd.nextDouble(); // read-in the amount to deposit

						if (amountToDeposit <= 0.00) {
							// invalid amount to deposit
							outFile.println("Transaction Requested: Deposit");
							outFile.println("Account Number: " + requestedAccount);
							outFile.printf("Error: $%.2f is an invalid amount", amountToDeposit);
							outFile.println();
						} else {
							outFile.println("Transaction Requested: Deposit");
							outFile.println("Account Number: " + requestedAccount);
							outFile.printf("Old Balance: $%.2f", balanceArray[index]);
							outFile.println();
							outFile.println("Amount to Deposit: $" + amountToDeposit);
							balanceArray[index] += amountToDeposit; // make the deposit
							outFile.printf("New Balance: $%.2f", balanceArray[index]);
							outFile.println();
						}

					} else { // invalid amount to deposit
						accLength = kybd.next();
						outFile.println("Transaction Requested: Deposit");
						outFile.println("Account Number: " + requestedAccount);
						outFile.printf("Error: Deposit entered invalid! Deposit must be a positive integer or double.");
						outFile.println();
					}
				}

			} else { // Message for invalid account number entered
				accLength = kybd.next();
				outFile.println("Transaction Requested: Deposit");
				outFile.printf("Error: Account number entered invalid!\nAccount numbers must be a 6-digit integer "
						+ "\nbetween 100000 and 999999.\n\n");
			}
			outFile.println();
			outFile.flush(); // flush the output buffer
			printAccts(acctNumArray, balanceArray, numAccts, outFile);
		}

		/*
		 * Method withdrawal(): Input: Account number followed by amount to withdraw
		 * Process: Ensures account exists and removes amount selected, otherwise
		 * displays error message. Output: Lists old balance, then new balance
		 * successfully withdrawn.
		 */

		public static void withdrawal(int[] acctNumArray, double[] balanceArray, int numAccts, PrintWriter outFile,
				Scanner kybd) {
			int requestedAccount;
			int index;
			double amountToWithdraw;
			String accLength; // Sets up new account as string to ensure validity
			System.out.println();
			System.out.print("Enter the account number: "); // prompt for account number

			if (kybd.hasNextInt()) { // Validates input
				accLength = kybd.next(); // Sets validated integer as next string for testing
				if (accLength.length() != 6 || Integer.parseInt(accLength) < 100000) {
					outFile.println("Transaction Requested: Withdrawal");
					outFile.printf("Error: Account number entered invalid!\nAccount numbers must be a 6-digit integer "
							+ "\nbetween 100000 and 999999.\n\n");

				} else {
					// Calls findAcct to search if requestedAccount exists
					requestedAccount = Integer.parseInt(accLength);
					index = findAcct(acctNumArray, numAccts, requestedAccount);
					if (index == -1) // Signals invalid account
					{
						outFile.println("Transaction Requested: Withdrawal");
						outFile.println("Error: Account number " + requestedAccount + " does not exist");
					} else { // Signals valid account
						System.out.print("Enter amount to Withdrawal: "); // Prompts for withdrawal
						// Ensures correct input
						if (kybd.hasNextDouble()) {
							amountToWithdraw = kybd.nextDouble();

							// Criteria for invalid deposit request
							if (amountToWithdraw <= 0.00) {
								outFile.println("Transaction Requested: Withdrawal");
								outFile.println("Account Number: " + requestedAccount);
								outFile.printf("Error: $%.2f is an invalid amount", amountToWithdraw);
								outFile.println();

								// User trying to withdraw more than they have
							} else if (balanceArray[index] < amountToWithdraw) {
								outFile.println("Transaction Requested: Withdrawal");
								outFile.println("Account Number: " + requestedAccount);
								outFile.printf("Error: Insufficient funds");
								outFile.println();

							} else { // Making successful withdrawal
								outFile.println("Transaction Requested: Withdrawal");
								outFile.println("Account Number: " + requestedAccount);
								outFile.printf("Old Balance: $%.2f", balanceArray[index]);
								outFile.println();
								outFile.println("Amount to Withdrawal: $" + amountToWithdraw);
								balanceArray[index] -= amountToWithdraw;
								outFile.printf("New Balance: $%.2f", balanceArray[index]);
								outFile.println();
							}
						} else {
							accLength = kybd.next();
							outFile.println("Transaction Requested: Withdrawal");
							outFile.println("Error: Withdrawal must be numbers!");
						}
					}
				}
			} else { // Message for invalid account number entered
				accLength = kybd.next();
				outFile.println("Transaction Requested: Withdrawal");
				outFile.printf("Error: Account number entered invalid!\nAccount numbers must be a 6-digit integer "
						+ "\nbetween 100000 and 999999.\n\n");
			}
			outFile.println();
			outFile.flush();
			printAccts(acctNumArray, balanceArray, numAccts, outFile);
		}

		/*
		 * Method newAcct(): Input:New Account number
		 *
		 * Process:Uses find account to check if account already exists. If account
		 * number is valid but taken, teller is notified. If account number is not
		 * valid, error message and instructions displayed. Once a valid new account
		 * number is entered, adds account to array, as well as parallel balanceAccount
		 * array.
		 *
		 * Output: Displays the successful creation of the new account.
		 *
		 */
		public static int newAcct(int[] acctNumArray, double[] balanceArray, int numAccts, PrintWriter outFile,
				Scanner kybd) {
			int accountNew, index;
			String accLength; // Sets up new account as string to ensure validity
			System.out.println();
			System.out.println("Enter New Account Number:");

			// Checks read-in the account number
			if (kybd.hasNextInt()) { // Validates input
				accLength = kybd.next();

				// Calls findAcct to search if requestedAccount exists
				if (accLength.length() != 6 || Integer.parseInt(accLength) < 100000) {
					outFile.println("Transaction Requested: New Account");
					outFile.printf("Error: Account number entered invalid!\nAccount numbers must be a 6-digit integer "
							+ "\nbetween 100000 and 999999.\n\n");
					outFile.flush();

				} else {
					accountNew = Integer.parseInt(accLength);

					index = findAcct(acctNumArray, numAccts, accountNew);
					if (index != -1) // invalid account
					{
						outFile.println("Transaction Requested: Create New Account");
						outFile.println("Error: Account number " + accountNew + " is already in use.");
					} else {
						acctNumArray[numAccts] = accountNew;
						balanceArray[numAccts] = 0.00;
						outFile.println("Transaction Requested: Create New Account");
						outFile.println("New Account with account number " + accountNew + " created.");
						numAccts++;
					}
				}
			} else { // Message for invalid account number entered
				accLength = kybd.next();
				outFile.println("Transaction Requested: New Account");
				outFile.printf("Error: Account number entered invalid!\nAccount numbers must be a 6-digit integer "
						+ "\nbetween 100000 and 999999.\n\n");
			}
			outFile.println();
			outFile.flush();
			printAccts(acctNumArray, balanceArray, numAccts, outFile);
			return numAccts;
		}

		/*
		 * Method deleteAcct(): Input: Existing account number
		 *
		 * Process: Ensures account balance is at 0.00 and delete account. This is done
		 * by getting the index of the account, cloning the arrays, manipulating them
		 * and then putting it back into the original array. Responds with message
		 *
		 * Output: Reprint array with parallel order.
		 */

		public static int deleteAcct(int[] acctNumArray, double[] balanceArray, int numAccts, PrintWriter outFile,
				Scanner kybd) {
			int delAcct, index;
			String delTemp, accLength; // Setting up accounts as strings to test validity
			System.out.println("Enter Account Number for deletion:");
			if (kybd.hasNextInt()) {
				accLength = kybd.next();
				delAcct = Integer.parseInt(accLength);
				index = findAcct(acctNumArray, numAccts, delAcct);
				delTemp = Integer.toString(delAcct);

				if (index == -1) {
					outFile.println("Transaction Requested: Delete Account");
					outFile.println("Error: Account entered does not exist!");
				} else if (Integer.parseInt(delTemp) <= 100000 || delTemp.length() != 6) {
					outFile.println("Transaction Requested: Delete Account");
					outFile.printf("Error: Account number entered invalid!\nAccount numbers must be a 6-digit integer "
							+ "\nbetween 100000 and 999999.\n\n");
				} else if (balanceArray[index] != 0.00) {
					outFile.println("Transaction Requested: Delete Account");
					outFile.printf("Error: Account is not empty. \nRemove funds from account before deleting.");
				} else {

					int[] tempAcc = new int[acctNumArray.length];
					double[] tempBalance = new double[balanceArray.length];
					for (int i = 0; i < numAccts; i++) {
						if (i == index) {
						} else if (i > index) {
							tempAcc[i - 1] = acctNumArray[i];
						} else {
							tempAcc[i] = acctNumArray[i];
						}
					}
					// Replacing old Account array with new Account array
					for (int i = 0; i < acctNumArray.length; i++) {
						acctNumArray[i] = tempAcc[i];
					}
					for (int a = 0; a < numAccts; a++) {
						if (a == index) {
						} else if (a > index) {
							tempBalance[a - 1] = balanceArray[a];
						} else {
							tempBalance[a] = balanceArray[a];
						}
					}
					// Replacing old Balance array with new Balance array
					for (int i = 0; i < acctNumArray.length; i++) {
						balanceArray[i] = tempBalance[i];
					}
					outFile.println("Transaction Requested: Delete Account");
					outFile.println("Deleted account number: " + delAcct);
					numAccts--;
				}
			} else {
				accLength = kybd.next();
				outFile.println("Transaction Requested: New Account");
				outFile.printf("Error: Account number entered invalid!\nAccount numbers must be a 6-digit integer "
						+ "\nbetween 100000 and 999999.\n\n");
			}
			outFile.println();
			outFile.flush();
			printAccts(acctNumArray, balanceArray, numAccts, outFile);
			return numAccts;

		}

		/* Method pause() */
		public static void pause(Scanner keyboard) {
			String tempstr;
			System.out.println();
			System.out.print("press ENTER to continue");
			tempstr = keyboard.nextLine(); // flush previous ENTER
			tempstr = keyboard.nextLine(); // wait for ENTER
		}
	}
