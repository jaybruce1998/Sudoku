   import javax.swing.*;
   import java.awt.event.*;
   public class sudokuPlayer
   {
   
      private static JFrame frame;
      private static SudokuPanel sp;
      private static mouseListener ml;
      static keyListener kl;
      static Sudoku game, solved;
      static int sec;
      static String inputValue;
      static boolean firstTime;
      public static void createPanel()
      {
         frame = new JFrame();
         sp = new SudokuPanel(game);
         ml = new mouseListener(game);
         kl=new keyListener();
         sp.addMouseListener(ml);
         frame.addKeyListener(kl);
         frame.add(sp);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setSize(705, 745);
         frame.setVisible(true);
         firstTime=false;
      }
   
      public static int getHints()
      {
         boolean notFound=true;
         int hints=0;
         inputValue = JOptionPane.showInputDialog("How many hints do you want?\nYou must generate a number of hints between 17 and 80.");
         if(inputValue==null||inputValue.equals(""))
            System.exit(0);
         for(int i=80; i>=17&&notFound; i--)
            if(inputValue.contains(""+i))
            {
               hints=i;
               notFound=false;     
            }
         if(hints==0)
            hints=(int)(Math.random()*64)+17;
         return hints;
      }
      public static void getInput()
      {
         while(kl.getCode()<1)
         {
            sp.repaint();
            frame.repaint();
         }
      }
      public static String getTime()
      {
         String t;
         t=sec/60+":";
         if(sec%60<10)
            t+=0;
         t+=sec%60;
         return t;
      }
      public static void setSec(int s)
      {
         sec=s;
      }
      public static int getSec()
      {
         return sec;
      }
      public static void main(String[] args) throws Exception
      {
         inputValue="";
         solved=new Sudoku(80);
         game=new Sudoku(80);
         firstTime=true;
         int delay = 1000;
         ActionListener taskPerformer = 
            new ActionListener() {
               public void actionPerformed(ActionEvent evt) {
                  sec++;
               }
            };
         while(!inputValue.contains("3"))
         {
            inputValue = JOptionPane.showInputDialog("What would you like to do?\n1) Start a new puzzle.\n2) Load a puzzle.\n3) Exit.");
            if(inputValue==null||inputValue.equals(""))
               System.exit(0);
            if(inputValue.contains("1")||inputValue.contains("2"))
            {
               if(inputValue.contains("1"))
               {
                  sec=0;
                  game.makeInto(new Sudoku(getHints()));
                  solved.makeInto(game);
                  sudokuSolver.solvable(solved, false);
               }
               else
                  if(inputValue.contains("2"))
                     game.makeInto(new Sudoku(JOptionPane.showInputDialog("What's the filename?")+".txt", true, solved));
               if(firstTime)
                  createPanel();
               new Timer(delay, taskPerformer).start();
               while(!game.completed())
               {
                  sp.repaint();
                  frame.repaint();
                  kl.setCode(0); 
                  getInput();
               }
               sp.repaint();
               frame.repaint();
               System.out.println("Actual puzzle:");
               System.out.println();
               System.out.println(solved);
               if(game.puzzlesAreEqual(solved))
                  System.out.println("You solved the puzzle in "+getTime()+"!");
               else
                  System.out.println("You failed!");
            }
         }
      }
   }