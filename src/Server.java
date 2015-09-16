
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;


public class Server extends Thread {
  
  GUI user = null;
  int role;
  Board board = null;
  int nextX;
  int nextY;
  boolean next = false;
  int nextAI = 0;
  int strategy = 0;
  int maxForward = 8;
  int attackChoice;
  int defendChoice;
  int bestAttack;
  int bestDefend;
  int MAX_VALUE = 1000000;
  int MIN_VALUE = -1000000;

  long starttime = 0L;
  boolean test = false;
  int stepCount = -2;
  int[] ans = new int[30];
  
  public Server(GUI user, int role) throws IOException{
    this.user = user;
    this.role = role;
    this.board = new Board();
    if (role == 2) {
      this.board.set(7, 7, 1);
      this.stepCount = -1;
    }
  }
  
  
  public void initializeSet(int x, int y, int player)
  {
    if (board.board[x][y] == 0)
      board.set(x, y, player);
  }
  
  public void run(){
    int k;

    while (board.total != 225) {
      if (next) {
        stepCount += 2;
        next = false;

        if (board.isBanned(nextX * 100 + nextY, role)) {
          user.setBanned(3 - role);
        }

        if (board.isWin(nextX * 100 + nextY, role)) {
          user.setWinner(role);
          if (!test)
            return;
          System.out.println("step " + (stepCount + 1) + "\t" + "User wins");
          return;
        }

        board.set(nextX, nextY, role);
        if (board.total == 225) {
          user.setWinner(3);
          return;
        }
        starttime = System.currentTimeMillis();
        bestAttack = MIN_VALUE;

        k = Try0(3 - role, board, 0);
        if (k != -1) {
          if (k >= 10000) {
            k -= 10000;
            user.computer(k/100, k%100);
            user.setWinner(3-role);
            return;
          }
          board.set(k/100, k%100, 3-role);
          user.computer(k/100, k%100);
        }
        else {
          k = Try2(3 - role, board);
        board.set(k/100, k%100, 3-role);
        user.computer(k/100, k%100);
        }
      }
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    user.setWinner(3);
  }
   
  
  public int Try0(int r, Board board, int stamp){
    if(stamp>=maxForward)
      return -1;
    int max = MIN_VALUE, choice = -1, max2 = MIN_VALUE;
    int k = board.getFive(r);
    if(k!=-1){
      return k+10000;
    }
    
    k = board.getFive(3-r);
    if(k!=-1){
      return k;
    }
    
    k = board.getDoubleFour(r);
    if(k!=-1){
      return k;
    }
    
    HashSet<Integer> t = board.getFour(r);
    if(t.size()!=0){
      for(int n: t){
        Board temp = new Board(board);
        temp.set(n/100, n%100, r);
        int p = temp.evaluate(r);
        int estimate = Try3(3-r, temp,stamp+1,r,max);
        if(estimate==MAX_VALUE){
          return n;
        }
        if(max<estimate){
          max = estimate;
          choice = n;
          max2 = p;
        }
        else if(max==estimate&&max2<p){
          max2 = p;
          choice = n;
        }
      }
    }
    
    
    t = board.DefendFour(3-r);
    if(t!=null){
      max = MIN_VALUE;
      max2 = MIN_VALUE;
      choice = -1;
      for(int n: t){
        Board temp = new Board(board);
        temp.set(n/100, n%100, r);
        int p = temp.evaluate(r);
        int estimate = Try3(3-r, temp,stamp+1, r,max);
        if(max<estimate){
          max = estimate;
          choice = n;
          max2 = p;
        }
        else if(max==estimate&&max2<p){
          max2 = p;
          choice = n;
        }
      }
      return choice;
    }
    
    k = board.getFakeDoubleFour(r);
    if(k!=-1){
      return k;
    }
    
    
    k = board.getDoubleThree(r);
    if(k!=-1){
      Board temp = new Board(board);
      temp.set(k/100, k%100, r);
      int p = temp.evaluate(r);
      int estimate = Try3(3-r, temp, stamp+1,r,max);
      if(estimate==MAX_VALUE){
        return k;
      }
      if(max<estimate){
        max = estimate;
        choice = k;
        max2 = p;
      }
      else if(max==estimate&&max2<p){
        max2 = p;
        choice = k;
      }
    }
    
    t = board.getThree(r);
    if(t.size()!=0){
      for(int n: t){
        Board temp = new Board(board);
        temp.set(n/100,n%100,r);
        int p = temp.evaluate(r);
        int estimate = Try3(3-r,temp,stamp+1,r,max);
        if(estimate==MAX_VALUE){
          return n;
        }
        if(max<estimate){
          max = estimate;
          choice = n;
          max2 = p;
        }
        else if(max==estimate&&max2<p){
          max2 = p;
          choice = n;
        }
      }
    }
    
    t = board.DefendThree(3-r);
    if(t!=null){
      max = MIN_VALUE;
      choice = -1;
      for(int n: t){
        Board temp = new Board(board);
        temp.set(n/100, n%100, r);
        int p = temp.evaluate(r);
        int estimate = Try3(3-r, temp,stamp+1, r, max);
        if(max<estimate){
          max = estimate;
          choice = n;
          max2 = p;
        }
        else if(max==estimate&&max2<p){
          max2 = p;
          choice = n;
        }
      }
      return choice;
    }
    
/*    k = board.getFakeDoubleThree(r);
    if(k!=-1){
      return k;
    }*/
    
    
    if(stamp==0){
      bestAttack = max;
      attackChoice = choice;
    }
    
    return -1;
  }
  
  public int Try3(int r, Board board, int stamp, int player, int lowbound){
    if(System.currentTimeMillis()-starttime>=8000){
      System.out.println("timeout");
      return lowbound;
    }
    int max = board.evaluate(r);
    if(r!=player&&-max<=lowbound)
      return lowbound;
    int choose = -1;
    int expaned = 0;
    
    if(stamp>=maxForward){
      return board.evaluate(player);
    }
    
    int k = board.getFive(r);
    if(k!=-1){
      if(r==player)
        return MAX_VALUE;
      else
        return MIN_VALUE;
    }
    
    k = board.getFive(3-r);
    if(k!=-1){
      if(board.isBanned(k, r)){
        return MAX_VALUE;
      }
      board.set(k/100, k%100, r);
      return Try3(3-r, board, stamp, player,lowbound);
    }
    
    k = board.getDoubleFour(r);
    if(k!=-1){
      if(r==player)
        return MAX_VALUE;
      else
        return MIN_VALUE;
    }
    
    HashSet<Integer> t = board.getFour(r);
    if(t.size()!=0){
      for(int n: t){
        expaned++;
        Board temp = new Board(board);
        temp.set(n/100, n%100, r);
        int estimate = Try3(3-r, temp,stamp+1, r,max);
        if(estimate==MAX_VALUE){
          if(player==r)
            return MAX_VALUE;
          else
            return MIN_VALUE;
        }
        if(estimate>=max){
          choose = n;
          max = estimate;
          if(r!=player&&(-estimate<=lowbound))
            return lowbound;
        }
      }
    }
    
    t = board.DefendFour(3-r);
    if(t!=null){
      t.removeAll(board.getBanned(r));
      max = MIN_VALUE;
      choose = -1;
      for(int n: t){
        Board temp = new Board(board);
        temp.set(n/100, n%100, r);
        int estimate = Try3(3-r, temp,stamp+1, r, max);
        if(max<=estimate){
          max = estimate;
          choose = n;
          if(r!=player&&(-estimate<=lowbound))
            return lowbound;
        }
      }
      if(player==r)
        return max;
      else
        return -max;
    }
    
    
    k = board.getFakeDoubleFour(r);
    if(k!=-1){
      if(r==player)
        return MAX_VALUE;
      else
        return MIN_VALUE;
    }
    
    
    
    k = board.getDoubleThree(r);
    if(k!=-1){
      Board temp = new Board(board);
      temp.set(k/100, k%100, r);
      int p = temp.evaluate(r);
      int estimate = Try3(3-r, temp, stamp+1,r,max);
      if(estimate==MAX_VALUE){
        if(player==r)
          return MAX_VALUE;
        else
          return MIN_VALUE;
      }
      if(estimate>=max){
        choose = k;
        max = estimate;
        if(r!=player&&(-estimate<=lowbound))
          return lowbound;
      }
    }
    
    t = board.getThree(r);
    if(t.size()!=0){
      for(int n: t){
        expaned++;
        Board temp = new Board(board);
        temp.set(n/100,n%100,r);
        int estimate = Try3(3-r,temp,stamp+1, r, max);
        if(estimate==MAX_VALUE){
          if(player==r)
            return MAX_VALUE;
          else
            return MIN_VALUE;
        }
        if(estimate>=max){
          choose = n;
          max = estimate;
          if(r!=player&&(-estimate<=lowbound))
            return lowbound;
        }
      }
    }
    
    
    t = board.DefendThree(3-r);
    if(t!=null){
      t.removeAll(board.getBanned(r));
      max = MIN_VALUE;
      choose = -1;
      for(int n: t){
        Board temp = new Board(board);
        temp.set(n/100, n%100, r);
        int estimate = Try3(3-r, temp,stamp+1, r, max);
        if(max<=estimate){
          max = estimate;
          choose = n;
          if(r!=player&&(-estimate<=lowbound))
            return lowbound;
        }
      }
      if(player==r)
        return max;
      else
        return -max;
    }
    
/*    k = board.getFakeDoubleThree(r);
    if(k!=-1){
      if(r==player)
        return MAX_VALUE;
      else
        return MIN_VALUE;
    }*/
    
    
    //NOTE.....
    if(expaned<=0&&stamp<3){
      t = board.getHeuristic(r);
      for(int n: t){
        Board temp = new Board(board);
        temp.set(n/100, n%100, r);
        int estimate = Try3(3-r, temp, stamp+1, r, max);
        if(estimate==MAX_VALUE){
          if(player==r)
            return MAX_VALUE;
          else
            return MIN_VALUE;
        }
        if(estimate>=max){
          choose = n;
          max = estimate;
          if(r!=player&&(-estimate<=lowbound))
            return lowbound;
        }
      }
    }
    
    if(player==r){
      return max;
    }
    else{
      return -max;
    }
  }
  
  
  public int Try1(int r, Board board){
    HashSet<Integer> s = board.getHeuristic(r);
    if(s.size()==1){
      Iterator<Integer> i = s.iterator();
      return i.next();
    }
    int max = MIN_VALUE, max2 = MIN_VALUE;
    int choose = -1;
    for(int n: s){
      Board temp = new Board(board);
      temp.set(n/100, n%100, r);
      int p = temp.evaluate(r);
      int estimate = Try3(3-r,temp,1,r,max);
      if(estimate>max){
        choose = n;
        max = estimate;
        max2 = p;
      }
      else if(max==estimate&&max2<p){
        max2 = p;
        choose = n;
      }
    }
    return choose;
  }
  
  
  
  public int Try2(int r, Board board){
    int max2 = MIN_VALUE;
    bestDefend = MIN_VALUE;
    HashSet<Integer> t = board.getFour(3-r);
    t.addAll(board.getThree(3-r));
    t.removeAll(board.getBanned(r));
    t.removeAll(board.getThree(r));
    t.removeAll(board.getFour(r));
    
    if(t.size()!=0){
      for(int n: t){
        Board temp = new Board(board);
        temp.set(n/100, n%100, r);
        int p = temp.evaluate(r);
        int estimate = Try3(3-r, temp,1,r,bestDefend);
        if(bestDefend<estimate){
          bestDefend = estimate;
          defendChoice = n;
          max2 = p;
        }
        else if(bestDefend==estimate&&max2<p){
          max2 = p;
          defendChoice = n;
        }
      }
    }
    
    //NOTE.....
    if(board.total<=10&&((bestDefend==0||bestDefend==MIN_VALUE)&&(bestAttack==0||bestAttack==MIN_VALUE))){
      int k = Try1(r, board);
      Board temp = new Board(board);
      temp.set(k/100, k%100, r);
      int p = this.Try3(3-r, temp, 0, r, MIN_VALUE);
      if(p>=bestDefend&&p>=bestAttack){
        return k;
      }
    }
    
    if(bestDefend==MIN_VALUE){
      if(bestAttack!=MIN_VALUE){
        return attackChoice;
      }
      else{
        return Try1(r, board);
      }
    }
    else{
      if(bestAttack<=bestDefend){
        return defendChoice;
      }
      else{
        return attackChoice;
      }
    }
  }
  
  public void set(int x, int y, int player)
  {
    this.next = true;
    this.nextX = x;
    this.nextY = y;
  }  

}
