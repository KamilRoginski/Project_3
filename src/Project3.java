// Name: Kamil Roginski
// Project: CMSC 315 Project 3
// Date: 22 APR 2025
// Description: Main class to drive user I/O and display results.

import java.util.*;

public class Project3 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String answer = "";
        do {
            System.out.print("Enter a binary tree: ");
            String line = in.nextLine().trim();
            BinaryTree tree;
            try {
                tree = new BinaryTree(line);
            } catch (InvalidSyntaxException e) {
                System.out.println("Error: " + e.getMessage());
                continue;
            }
            // display original
            System.out.print(tree.toIndentedString());

            if (!tree.isBST()) {
                System.out.println("It is not a binary search tree");
            } else if (tree.isBalanced()) {
                System.out.println("It is a balanced binary search tree");
            } else {
                System.out.println("It is a binary search tree but it is not balanced");
            }

            // if not balanced BST, rebuild & show heights
            if (!tree.isBST() || !tree.isBalanced()) {
                BinaryTree balanced = new BinaryTree(tree.getValues());
                System.out.print(balanced.toIndentedString());
                System.out.println("Original tree has height " + tree.getHeight());
                System.out.println("Balanced tree has height " + balanced.getHeight());
            }

            System.out.print("More trees? Y or N: ");
            answer = in.nextLine().trim();
        } while (answer.equalsIgnoreCase("Y"));
        in.close();
    }
}
