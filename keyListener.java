   import java.io.*;
   import java.util.*;
   import java.awt.event.*;
   public class keyListener extends KeyAdapter
   {
      private int key = 0, r, c;
      public void keyPressed(KeyEvent k)
      {
         key=k.getKeyCode();
         if(key==KeyEvent.VK_ENTER)
            try
            {
               sudokuPlayer.solved.save(mouseListener.puzzle, sudokuPlayer.getSec());
            }
               catch(Exception e)
               {
                                             
               }
         else
            if(sudokuPlayer.game.selectedRow()<9)
            {
               r=mouseListener.puzzle.selectedRow();
               c=mouseListener.puzzle.selectedCol();
               if(key==KeyEvent.VK_1||key==KeyEvent.VK_NUMPAD1)
                  mouseListener.puzzle.setVis(r, c, 1);
               else
                  if(key==KeyEvent.VK_2||key==KeyEvent.VK_NUMPAD2)
                     mouseListener.puzzle.setVis(r, c, 2);
                  else
                     if(key==KeyEvent.VK_3||key==KeyEvent.VK_NUMPAD3)
                        mouseListener.puzzle.setVis(r, c, 3);
                     else
                        if(key==KeyEvent.VK_4||key==KeyEvent.VK_NUMPAD4)
                           mouseListener.puzzle.setVis(r, c, 4);
                        else
                           if(key==KeyEvent.VK_5||key==KeyEvent.VK_NUMPAD5)
                              mouseListener.puzzle.setVis(r, c, 5);
                           else
                              if(key==KeyEvent.VK_6||key==KeyEvent.VK_NUMPAD6)
                                 mouseListener.puzzle.setVis(r, c, 6);
                              else
                                 if(key==KeyEvent.VK_7||key==KeyEvent.VK_NUMPAD7)
                                    mouseListener.puzzle.setVis(r, c, 7);
                                 else
                                    if(key==KeyEvent.VK_8||key==KeyEvent.VK_NUMPAD8)
                                       mouseListener.puzzle.setVis(r, c, 8);
                                    else
                                       if(key==KeyEvent.VK_9||key==KeyEvent.VK_NUMPAD9)
                                          mouseListener.puzzle.setVis(r, c, 9);
                                       else
                                          mouseListener.puzzle.setVis(r, c, 0);
               mouseListener.puzzle.unClick();
            }
      }
      public void setCode(int c)
      {
         key=c;
      }
      public int getCode()
      {
         return key;
      }
   }