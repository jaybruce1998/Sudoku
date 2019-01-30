   import java.io.*;
   import java.util.*;
   public class sudokuNumber
   {
      int value;
      boolean foundVal, changable;
      boolean[] ed=new boolean[4];
      ArrayList<Integer> possible=new ArrayList<Integer>();
      public sudokuNumber()
      {
         fillArrayList();
      }
      public void reset()
      {
         value=0;
         possible.clear();
         fillArrayList();
         changable=true;
      }
      public void fillArrayList()
      {
         for(int n=1; n<=9; n++)
            possible.add(n);
         for(int i=0; i<ed.length; i++)
            ed[i]=false;
      }
      public void changeToIndex(int i)
      {
         value=possible.get(i);
      }
      public void changeTo(int n)
      {
         value=n;
      }
      public int numPos()
      {
         return possible.size();
      }
      public int nthPos(int i)
      {
         return possible.get(i);
      }
      public boolean ed(int i)
      {
         return ed[i-2];
      }
      public void wasEd(int i)
      {
         ed[i-2]=true;
      }
      public boolean couldBe(int n)
      {
         for(int i=0; i<possible.size(); i++)
            if(possible.get(i)==n)
               return true;
         return false;
      }
      public boolean couldOnlyBe(ArrayList<Integer> possibilities)
      {
         if(possible.size()<1)
            return false;
         for(int i=0; i<possible.size(); i++)
            if(!possibilities.contains(possible.get(i)))
               return false;
         return true;
      }
      public ArrayList<Integer> possibilities()
      {
         return possible;
      }
      public void getRidOf(int n)
      {
         foundVal=false;
         for(int i=0; i<numPos()&&!foundVal; i++)
            if(possible.get(i)==n)
            {
               possible.remove(i);
               foundVal=true;
            }
      }
      public int getVal()
      {
         return value;
      }
      public void change()
      {
         changable=false;
      }
      public boolean isChangable()
      {
         return changable;
      }
   }