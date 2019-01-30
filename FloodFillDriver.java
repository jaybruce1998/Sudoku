//Chris Cho
   import java.io.*;
   import java.util.*;

   public class FloodFillDriver
   {
      public static Scanner input = new Scanner(System.in);
   
      public static void input(ArrayList<String> array, String filename) throws Exception
      {
         BufferedReader infile = new BufferedReader(new FileReader(filename));
         String s = infile.readLine();
         while(s != null)
         {
            array.add(s);
            s = infile.readLine();
         }
         infile.close();
      }
      
      public static String[][] convert(ArrayList<String> array)
      {
         int rows = array.size();
         int cols = array.get(0).length();
         String[][] grid = new String[rows][cols];
         for(int r=0;r<grid.length;r++)
            for(int c=0;c<grid[0].length;c++)
               grid[r][c] = array.get(r).charAt(c)+"";
         return grid;
      }
      
      public static void floodFill(String[][] grid, int x, int y)
      {
         if(grid[x][y] != null)
            grid[x][y] = "X";
         if(x-1 >= 0 && grid[x-1][y].equals("."))
            floodFill(grid, x-1, y);
         if(x+1 < grid.length && grid[x+1][y].equals("."))
            floodFill(grid, x+1, y);
         if(y-1 >= 0 && grid[x][y-1].equals("."))
            floodFill(grid, x, y-1);
         if(y+1 < grid[0].length && grid[x][y+1].equals("."))
            floodFill(grid, x, y+1);
      }
   
      public static void display(String[][] grid)
      {
         for(int r=0;r<grid.length;r++)
         {
            for(int c=0;c<grid[0].length;c++)
               System.out.print(grid[r][c]);
            System.out.println();
         }
      }
   
      public static void main(String[] args) throws Exception
      {
         ArrayList<String> list = new ArrayList();
         input(list, "floodGrid.txt");
         String[][] grid = convert(list);
         display(grid);
         System.out.println("Grid is size ("+grid.length+", "+grid[0].length+")");
         System.out.println("Fill from which tile?");
         System.out.print("Row: ");
         int row = input.nextInt()-1;
         System.out.print("Column: ");
         int col = input.nextInt()-1;
         while(grid[row][col] == null || grid[row][col].equals("@"))
         {
            System.out.println("Not a valid tile");
            System.out.println("Fill from which tile?");
            System.out.print("Row: ");
            row = input.nextInt()-1;
            System.out.print("Column: ");
            col = input.nextInt()-1;
         }
         floodFill(grid, row, col);
         display(grid);
      }
   }