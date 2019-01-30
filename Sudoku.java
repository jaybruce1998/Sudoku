   import java.io.*;
   import java.util.*;
   public class Sudoku
   {
      boolean doingWell;
      int errors, randomI, randomR, randomC, rHints, cHints, bHints, rows, cols, boxs, randomInt, selectedR, selectedC;
      sudokuNumber[][] puzzle=new sudokuNumber[9][9];
      int visPuzzle[][]=new int[9][9];
      ArrayList<String> fileNumbers=new ArrayList<String>();
      ArrayList<Integer> allRows=new ArrayList<Integer>();
      ArrayList<Integer> allCols=new ArrayList<Integer>();
      ArrayList<Integer> naked=new ArrayList<Integer>();
      ArrayList<Integer> nudity=new ArrayList<Integer>();
      public Sudoku(int hints)
      {
         selectedR=9;
         selectedC=9;
         errors=1;
         for(int r=0; r<9; r++)
            for(int c=0; c<9; c++)
               puzzle[r][c]=new sudokuNumber();
         makePuzzle();
         generateHints(hints);
      }
      public Sudoku(Sudoku solved)
      {
         for(int r=0; r<9; r++)
            for(int c=0; c<9; c++)
            {
               visPuzzle[r][c]=solved.visPuzzle[r][c];
               puzzle[r][c]=new sudokuNumber();
               puzzle[r][c].changeTo(visPuzzle[r][c]);
            }
         for(int r=0; r<9; r++)
            for(int c=0; c<9; c++)
               if(puzzle[r][c].getVal()>0)
                  getRidOfAll(puzzle[r][c].getVal(), r, c);
      }
      public Sudoku(String filename, boolean completed, Sudoku solved) throws Exception
      {
         input(fileNumbers, filename, completed);
         convert(completed, solved);
         for(int r=0; r<9; r++)
            for(int c=0; c<9; c++)
               if(puzzle[r][c].getVal()>0)
                  getRidOfAll(puzzle[r][c].getVal(), r, c);
      }
      public void makePuzzle()
      {
         while(errors>0)
         {
            for(int r=0; r<9; r++)
               for(int c=0; c<9; c++)
                  puzzle[r][c].reset();
            generatePuzzle();
         }
      }
      public boolean nakedBox(int n, int actualR, int actualC)
      {
         naked.clear();
         nudity.clear();
         for(int r=firstRow(actualR); r<firstRow(actualR)+3; r++)
            for(int c=firstCol(actualC); c<firstCol(actualC)+3; c++)
               if(puzzle[r][c].getVal()<1&&puzzle[r][c].couldOnlyBe(puzzle[actualR][actualC].possibilities()))
               {
                  naked.add(r);
                  nudity.add(c);
               }
         if(naked.size()==n)
         {
            for(int i=0; i<n; i++)
               puzzle[naked.get(i)][nudity.get(i)].wasEd(n);
            for(int r=firstRow(actualR); r<firstRow(actualR)+3; r++)
               for(int c=firstCol(actualC); c<firstCol(actualC)+3; c++)
                  if(!(naked.contains(r)&&nudity.contains(c)))
                     for(int i=0; i<n; i++)
                        puzzle[r][c].getRidOf(puzzle[actualR][actualC].nthPos(i));
            return true;
         }
         return false;
      }
      public boolean nakedRow(int n, int actualR, int actualC)
      {
         naked.clear();
         for(int c=0; c<9; c++)
            if(puzzle[actualR][c].getVal()<1&&puzzle[actualR][c].couldOnlyBe(puzzle[actualR][actualC].possibilities()))
               naked.add(c);
         if(naked.size()==n)
         {
            for(int i=0; i<n; i++)
               puzzle[actualR][naked.get(i)].wasEd(n);
            for(int c=0; c<9; c++)
               if(!naked.contains(c))
                  for(int i=0; i<n; i++)
                     puzzle[actualR][c].getRidOf(puzzle[actualR][actualC].nthPos(i));
            return true;
         }
         return false;
      }
      public boolean nakedCol(int n, int actualR, int actualC)
      {
         naked.clear();
         for(int r=0; r<9; r++)
            if(puzzle[r][actualC].getVal()<1&&puzzle[r][actualC].couldOnlyBe(puzzle[actualR][actualC].possibilities()))
               naked.add(r);
         if(naked.size()==n)
         {
            for(int i=0; i<n; i++)
               puzzle[naked.get(i)][actualC].wasEd(n);
            for(int r=0; r<9; r++)
               if(!naked.contains(r))
                  for(int i=0; i<n; i++)
                     puzzle[r][actualC].getRidOf(puzzle[actualR][actualC].nthPos(i));
            return true;
         }
         return false;
      }
      public boolean notInBox(int n, int actualR, int actualC)
      {
         for(int r=firstRow(actualR); r<firstRow(actualR)+3; r++)
            for(int c=firstCol(actualC); c<firstCol(actualC)+3; c++)
               if(puzzle[r][c].getVal()==n)
                  return false;
         return true;
      }
      public boolean noOtherRowInBox(int n, int actualR, int actualC)
      {
         for(int r=firstRow(actualR); r<firstRow(actualR)+3; r++)
            for(int c=firstCol(actualC); c<firstCol(actualC)+3; c++)
               if(r!=actualR&&(puzzle[r][c].getVal()==n||puzzle[r][c].couldBe(n)))
                  return false;
         return true;
      }
      public boolean noOtherColInBox(int n, int actualR, int actualC)
      {
         for(int r=firstRow(actualR); r<firstRow(actualR)+3; r++)
            for(int c=firstCol(actualC); c<firstCol(actualC)+3; c++)
               if(c!=actualC&&(puzzle[r][c].getVal()==n||puzzle[r][c].couldBe(n)))
                  return false;
         return true;
      }
      public void getRidOfAllInRowExcept(int n, int r, int actualC)
      {
         for(int c=0; c<firstCol(actualC); c++)
            puzzle[r][c].getRidOf(n);
         for(int c=firstCol(actualC)+3; c<9; c++)
            puzzle[r][c].getRidOf(n);
      }
      public void getRidOfAllInColExcept(int n, int actualR, int c)
      {
         for(int r=0; r<firstRow(actualR); r++)
            puzzle[r][c].getRidOf(n);
         for(int r=firstRow(actualR)+3; r<9; r++)
            puzzle[r][c].getRidOf(n);  
      }
      public void input(ArrayList<String> array, String filename, boolean com) throws Exception
      {
         BufferedReader infile = new BufferedReader(new FileReader(filename));
         String s;
         for(int r=0; r<9; r++)
         {
            s = infile.readLine();
            array.add(s);
         }
         if(com)
         {
            for(int r=9; r<18; r++)
            {
               s = infile.readLine();
               array.add(s);
            }
            sudokuPlayer.setSec(Integer.parseInt(infile.readLine()));
         }
         infile.close();
      }
      public void convert(boolean completed, Sudoku solved)
      {
         for(int r=0; r<9; r++)
            for(int c=0; c<9; c++)
            {
               puzzle[r][c]=new sudokuNumber();
               puzzle[r][c].changeTo(Integer.parseInt(fileNumbers.get(r).charAt(c)+""));
               visPuzzle[r][c]=puzzle[r][c].getVal();
            }
         if(completed)
            for(int r=9; r<18; r++)
               for(int c=0; c<9; c++)
               {
                  solved.puzzle[r-9][c]=new sudokuNumber();
                  solved.puzzle[r-9][c].changeTo(Integer.parseInt(fileNumbers.get(r).charAt(c)+""));
                  solved.visPuzzle[r-9][c]=solved.puzzle[r-9][c].getVal();
               }
      }
      public void getRidOfAll(int n, int actualR, int actualC)
      {
         for(int r=0; r<9; r++)
            puzzle[r][actualC].getRidOf(n);
         for(int c=0; c<9; c++)
            puzzle[actualR][c].getRidOf(n);
         for(int r=firstRow(actualR); r<firstRow(actualR)+3; r++)
            for(int c=firstCol(actualC); c<firstCol(actualC)+3; c++)
               puzzle[r][c].getRidOf(n);
      }
      public void resetHints()
      {
         for(int r=0; r<9; r++)
            for(int c=0; c<9; c++)
               visPuzzle[r][c]=0;
      }
      public void reset()
      {
         for(int r=0; r<9; r++)
            for(int c=0; c<9; c++)
               puzzle[r][c].reset();
      }
      public boolean generated(int hints)
      {
         doingWell=true;
         allRows.clear();
         allCols.clear();
         for(int r=0; r<9; r++)
            for(int c=0; c<9; c++)
            {
               allRows.add(r);
               allCols.add(c);
               visPuzzle[r][c]=puzzle[r][c].getVal();
            }
         this.visPuzzle[allRows.get(randomInt)][allCols.get(randomInt)]=0;
         allRows.remove(randomInt);
         allCols.remove(randomInt);
         Sudoku unsolved=new Sudoku(this);
         for(int i=80; doingWell&&i>hints; i--)
         {
            if(allRows.size()>0)
            {
               randomI=(int)(Math.random()*allRows.size());
               this.visPuzzle[allRows.get(randomI)][allCols.get(randomI)]=0;
               unsolved.makeInto(this);
            }
            else
               doingWell=false;
            while(doingWell&&!sudokuSolver.solvable(unsolved, false))
            {
               this.visPuzzle[allRows.get(randomI)][allCols.get(randomI)]=this.puzzle[allRows.get(randomI)][allCols.get(randomI)].getVal();
               if(allRows.size()>2)
               {
                  allRows.remove(randomI);
                  allCols.remove(randomI);
                  randomI=(int)(Math.random()*allRows.size());
                  this.visPuzzle[allRows.get(randomI)][allCols.get(randomI)]=0;
                  unsolved.makeInto(this);
               }
               else
                  doingWell=false;
            }
            if(doingWell)
            {
               allRows.remove(randomI);
               allCols.remove(randomI);
            }
         }
         return doingWell;
      }
      public void generateHints(int hints)
      {
         randomInt=(int)(Math.random()*81);
         while(!generated(hints))
            if(randomInt<80)
               randomInt++;
            else
               randomInt=0;
         for(int r=0; r<visPuzzle.length; r++)
            for(int c=0; c<visPuzzle[0].length; c++)
               if(visPuzzle[r][c]>0)
                  puzzle[r][c].change();
      }
      public void changeRandom()
      {
         if(Math.random()<.5)
            if(randomR<8)
               randomR++;
            else
               randomR=0;
         else
            if(randomC<8)
               randomC++;
            else
               randomC=0;
      }
      public int hintsInRow(int r)
      {
         rHints=0;
         for(int c=0; c<9; c++)
            if(visPuzzle[r][c]>0)
               rHints++;
         return rHints;
      }
      public void makeInto(Sudoku solved)
      {
         for(int r=0; r<9; r++)
            for(int c=0; c<9; c++)
            {
               visPuzzle[r][c]=solved.visPuzzle[r][c];
               puzzle[r][c].reset();
               puzzle[r][c].changeTo(visPuzzle[r][c]);
               if(puzzle[r][c].getVal()>0)
               {
                  puzzle[r][c].change();
                  getRidOfAll(puzzle[r][c].getVal(), r, c);
               }
            }
      }
      public void generatePuzzle()
      {
         for(int r=0; r<9&&errors<2; r++)
         {
            for(int c=0; c<9&&errors<2; c++)
            {
               randomI=(int)(Math.random()*puzzle[r][c].numPos());
               puzzle[r][c].changeToIndex(randomI);
               while(!numberWorks(r, c)&&errors<2)
               {
                  puzzle[r][c].getRidOf(puzzle[r][c].getVal());
                  if(puzzle[r][c].numPos()<1)
                  {
                     puzzle[r][c].reset();
                     errors++;
                  }
                  randomI=(int)(Math.random()*puzzle[r][c].numPos());
                  puzzle[r][c].changeToIndex(randomI);
               }
               for(int row=0; row<9&&errors<2; row++)
               {
                  puzzle[row][c].getRidOf(puzzle[r][c].getVal());
                  if(puzzle[row][c].numPos()<1&&puzzle[row][c].getVal()<1)
                  {
                     puzzle[r][c].reset();
                     errors++;
                  }
               }
               for(int col=0; col<9&&errors<2; col++)
               {
                  puzzle[r][col].getRidOf(puzzle[r][c].getVal());
                  if(puzzle[r][col].numPos()<1&&puzzle[r][col].getVal()<1)
                  {
                     puzzle[r][c].reset();
                     errors++;
                  }
               }
               for(int row=firstRow(r); row<firstRow(r)+3&&errors<2; row++)
                  for(int col=firstCol(c); col<firstCol(c)+3&&errors<2; col++)
                  {
                     puzzle[row][col].getRidOf(puzzle[r][c].getVal());
                     if(puzzle[row][col].numPos()<1&&puzzle[row][col].getVal()<1)
                     {
                        puzzle[r][c].reset();
                        errors++;
                     }
                  }
            }
         }
         if(errors>1)
            errors=1;
         else
            errors=0;
      }
      public boolean numberWorks(int actualR, int actualC)
      {
         for(int r=actualR+1; r<9; r++)
            if(puzzle[r][actualC].numPos()<2&&puzzle[r][actualC].couldBe(puzzle[actualR][actualC].getVal()))
               return false;
         for(int c=actualC+1; c<9; c++)
            if(puzzle[actualR][c].numPos()<2&&puzzle[actualR][c].couldBe(puzzle[actualR][actualC].getVal()))
               return false;
         for(int r=actualR+1; r<firstRow(actualR)+3; r++)
            for(int c=actualC+1; c<firstCol(actualC)+3; c++)
               if(puzzle[r][c].numPos()<2&&puzzle[r][c].couldBe(puzzle[actualR][actualC].getVal()))
                  return false;
         return true;
      }
      public boolean completed()
      {
         for(int r=0; r<9; r++)
            for(int c=0; c<9; c++)
               if(visPuzzle[r][c]<1)
                  return false;
         return true;
      }
      public int firstRow(int r)
      {
         return r/3*3;
      }
      public int firstCol(int c)
      {
         return c/3*3;
      }
      public boolean spaceOccupied(int r, int c)
      {
         return visPuzzle[r][c]>0;
      }
      public void writeNumber(int r, int c, int n)
      {
         visPuzzle[r][c]=n;
      }
      public boolean puzzlesAreEqual(Sudoku solved)
      {
         for(int r=0; r<9; r++)
            for(int c=0; c<9; c++)
               if(this.visPuzzle[r][c]!=solved.getVis(r, c))
                  return false;
         return true;
      }
      public sudokuNumber getSudoku(int r, int c)
      {
         return puzzle[r][c];
      }
      public boolean canChange(int r, int c)
      {
         return puzzle[r][c].isChangable();
      }
      public int noOtherPlace(int r, int c)
      {
         for(int n=1; n<=9; n++)
            if(noOtherRow(n, r, c)>0||noOtherCol(n, r, c)>0||noOtherBox(n, r, c)>0)
               return n;
         return 0;
      }
      public int noOtherRow(int n, int r, int actualC)
      {
         rows=0;
         for(int c=0; c<9; c++)
         {
            if(puzzle[r][c].getVal()==n)
               return 0;
            if(c==actualC||puzzle[r][c].getVal()>0||!puzzle[r][c].couldBe(n))
               rows++;
         }
         if(rows>8)
            return n;
         return 0;
      }
      public int noOtherCol(int n, int actualR, int c)
      {
         cols=0;
         for(int r=0; r<9; r++)
         {
            if(puzzle[r][c].getVal()==n)
               return 0;
            if(r==actualR||puzzle[r][c].getVal()>0||!puzzle[r][c].couldBe(n))
               cols++;
         }
         if(cols>8)
            return n;
         return 0;
      }
      public int noOtherBox(int n, int actualR, int actualC)
      {
         boxs=0;
         for(int r=firstRow(actualR); r<firstRow(actualR)+3; r++)
            for(int c=firstCol(actualC); c<firstCol(actualC)+3; c++)
            {
               if(puzzle[r][c].getVal()==n)
                  return 0;
               if((r==actualR&&c==actualC)||puzzle[r][c].getVal()>0||!puzzle[r][c].couldBe(n))
                  boxs++;
            }
         if(boxs>8)
            return n;
         return 0;
      }
      public void fillInBlank(Sudoku unsolved)
      {
         boolean filled, replaced;
         filled=false;
         replaced=false;
         for(int r=0; !filled&&r<9; r++)
            for(int c=0; !filled&&c<9; c++)
               if(unsolved.getSudoku(r, c).getVal()<1)
               {
                  visPuzzle[r][c]=puzzle[r][c].getVal();
                  for(int row=r+1; !replaced&&row<9; row++)
                     for(int col=c; !replaced&&col<9; col++)
                        if(visPuzzle[row][col]>0)
                        {
                           visPuzzle[row][col]=0;
                           replaced=true;
                        }  
                  for(int row=0; !replaced&&row<r; row++)
                     for(int col=0; !replaced&&col<c; col++)
                        if(visPuzzle[row][col]>0)
                        {
                           visPuzzle[row][col]=0;
                           replaced=true;
                        }
                  filled=true;
               }
      }
      public int getVis(int r, int c)
      {
         return visPuzzle[r][c];
      }
      public void setVis(int r, int c, int n)
      {
         visPuzzle[r][c]=n;
      }
      public int selectedRow()
      {
         return selectedR;
      }
      public int selectedCol()
      {
         return selectedC;
      }
      public void click(int r, int c)
      {
         selectedR=r;
         selectedC=c;
         visPuzzle[r][c]=10;
      }
      public void unClick()
      {
         selectedR=9;
      }
      public void save(Sudoku p, int sec) throws Exception
      {
         System.setOut(new PrintStream("puzzle.txt"));
         for(int r=0; r<9; r++)
         {
            for(int c=0; c<9; c++)
               System.out.print(p.getVis(r, c));
            System.out.println();
         }
         for(int r=0; r<9; r++)
         {
            for(int c=0; c<9; c++)
               System.out.print(puzzle[r][c].getVal());
            System.out.println();
         }
         System.out.println(sec);
         System.exit(0);
      }
      public String toString()
      {
         String s="";
         for(int r=0; r<9; r++)
         {
            for(int c=0; c<9; c++)
               s+=puzzle[r][c].getVal();
            s+="\r";     
         }
         return s;
      }
   }