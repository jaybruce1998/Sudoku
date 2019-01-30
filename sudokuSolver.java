import javax.swing.*;
import java.awt.event.*;
public class sudokuSolver
{
   static JFrame frame;
   static SudokuPanel sp;
   static Sudoku puzzle;
   static int option, hints;
   static String inputValue;
   static boolean notFound;
   public static void createPanel()
   {
      frame = new JFrame();
      sp = new SudokuPanel(puzzle);
      frame.add(sp);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(705, 745);
      frame.setVisible(true);
   }
   public static boolean solvable(Sudoku puzzle, boolean displayPuzzle)
   {
      boolean solvable;
      solvable=true;
      while(solvable&&!puzzle.completed())
      {
         solvable=false;
         for(int r=0; r<9; r++)
            for(int c=0; c<9; c++)
               if(puzzle.getSudoku(r, c).getVal()<1) 
               {
                  if(puzzle.getSudoku(r, c).numPos()<2||puzzle.noOtherPlace(r, c)>0)
                  {
                     solvable=true;
                     if(puzzle.getSudoku(r, c).numPos()<2)
                        puzzle.getSudoku(r, c).changeToIndex(0);
                     else
                        puzzle.getSudoku(r, c).changeTo(puzzle.noOtherPlace(r, c));
                     puzzle.writeNumber(r, c, puzzle.getSudoku(r, c).getVal());
                     puzzle.getRidOfAll(puzzle.getSudoku(r, c).getVal(), r, c);
                     if(displayPuzzle)
                     {
                        frame.repaint();
                        sp.repaint();
                     }
                  }
                  if(!solvable)
                  {
                     for(int n=1; n<=9; n++)
                     {
                        if(puzzle.noOtherRowInBox(n, r, c))
                           puzzle.getRidOfAllInRowExcept(n, r, c);
                        else
                           if(puzzle.noOtherColInBox(n, r, c))
                              puzzle.getRidOfAllInColExcept(n, r, c);
                     }
                     if(puzzle.getSudoku(r, c).numPos()>1&&puzzle.getSudoku(r, c).numPos()<6&&!puzzle.getSudoku(r, c).ed(puzzle.getSudoku(r, c).numPos()))
                        solvable=puzzle.nakedBox(puzzle.getSudoku(r, c).numPos(), r, c)||puzzle.nakedRow(puzzle.getSudoku(r, c).numPos(), r, c)||puzzle.nakedCol(puzzle.getSudoku(r, c).numPos(), r, c);
                  }
               }
      }
      return solvable;
   }
   public static void main(String[] args) throws Exception
   {
      notFound=true;
      inputValue = JOptionPane.showInputDialog("What would you like to do?\n1) Solve a puzzle from a text file.\n2) Generate and solve a puzzle\n3) Exit.");
      if(inputValue==null||inputValue.equals(""))
         System.exit(0);
      for(int i=0; i<4&&notFound; i++)
         if(inputValue.contains(""+i))
         {
            option=i;
            notFound=false;     
         }
      if(option==1)
      {
         puzzle=new Sudoku(JOptionPane.showInputDialog("What's the filename?")+".txt", false, puzzle);
         createPanel();
         sp.repaint();
         frame.repaint();
         if(solvable(puzzle, true))
            System.out.println("That was easy!");
         else
            System.out.println("...can I have something easier?");
      }
      if(option==2)
      {
         hints=sudokuPlayer.getHints();
         puzzle=new Sudoku(80);
         puzzle.makeInto(new Sudoku(hints));
         System.out.println("Unsolved puzzle:");
         System.out.println();
         System.out.println(puzzle);
         createPanel();
         sp.repaint();
         frame.repaint();
         solvable(puzzle, true);
      }
      JOptionPane.showInputDialog("Would you like to save this puzzle?");
      System.exit(0);
   }
}