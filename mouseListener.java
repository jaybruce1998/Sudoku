import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
public class mouseListener extends MouseAdapter
{
   static Sudoku puzzle;
   int r, c;
   public mouseListener(Sudoku p)
   {
      puzzle=p;
   }
   public void mousePressed(MouseEvent e)
   {
      r=(int)(e.getY())/75;
      c=(int)(e.getX())/75;
      if(r<9&&c<9)
         if(puzzle.getSudoku(r, c).isChangable()&&e.getButton() == MouseEvent.BUTTON1&&puzzle.selectedRow()>8)
            puzzle.click(r, c);
         else
            if(e.getButton() == MouseEvent.BUTTON3&&puzzle.selectedRow()<9)
            {
               puzzle.setVis(puzzle.selectedRow(), puzzle.selectedCol(), 0);
               puzzle.unClick();
            }
   }
   public int getRow()
   {
      return r;
   }
   public int getCol()
   {
      return c;
   }
}