import javax.swing.*;
import java.awt.*;
  
public class SudokuPanel extends JPanel
{
   ImageIcon[] icons;
   String location;
   Sudoku puzzle;
   public SudokuPanel(Sudoku p)
   {
      super();
      location=System.getProperty("user.dir")+"/";
      puzzle=p;
      icons=new ImageIcon[11];
      for(int i=0; i<icons.length; i++)
         icons[i] = new ImageIcon(location+i+".png");
   }
   
   public void paintComponent(Graphics g)
   {
      int rowP, colP;
      rowP=2;
      colP=3;
      for(int r=0; r<9; r++)
      {
         for(int c=0; c<9; c++)
         {
            if(c%3==0)
               colP+=5;
            g.drawImage(icons[puzzle.getVis(c, r)].getImage(), r*75+rowP, c*75+colP, 75, 75, null);
         }
         colP=3;  
         if(r%3==2)
            rowP+=5;
      }
      
      g.drawString(sudokuPlayer.getTime(), 3, 705);
   }
}