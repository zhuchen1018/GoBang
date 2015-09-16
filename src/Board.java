
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Board {
	
	public int[][] board = null;
	public ArrayList<Integer>[][] info = null;
	int total = 0;
	int score[] = null;
	HashSet<Integer> alwaysBanned = new HashSet<Integer>();
	
	public Board(){
		board = new int[15][];
		for(int i=0;i<15;i++)
			board[i] = new int[15];
		info = new ArrayList[2][];
		for(int i=0;i<2;i++){
			info[i] = new ArrayList[4];
			for(int j=0;j<4;j++)
				info[i][j] = new ArrayList<Integer>();
		}
		score = new int[2];
	}
	
	
	
	public Board(Board b){
		board = new int[15][];
		for(int i=0;i<15;i++){
			board[i] = new int[15];
			for(int j=0;j<15;j++)
				board[i][j] = b.board[i][j];
		}
		info = new ArrayList[2][];
		for(int i=0;i<2;i++){
			info[i] = new ArrayList[4];
			for(int j=0;j<4;j++)
				info[i][j] = new ArrayList<Integer>(b.info[i][j]);
		}
		this.total = b.total;
		score = new int[2];
		score[0] = b.score[0];
		score[1] = b.score[1];
	}
	
	
	
	public int getFive(int player){
		Iterator<Integer> i = info[player-1][3].iterator();
		while(i.hasNext()){
			int j = ((Integer)i.next()%10000)/4;
			if(player==1&&info[0][0].contains(j))
				continue;
			return j;
		}
		return -1;
	}
	
	public int getDoubleFour(int player){
		HashSet<Integer> s1 = new HashSet<Integer>();
		for(int i: info[player-1][2]){
			int m = i%10000;
			if(player==1&&info[0][0].contains(m/4))
				continue;
			if(s1.contains(m/4))
				return m/4;
			s1.add(new Integer(m/4));
		}
		
		for(int i: info[player-1][1]){
			int j = i%10000;
			if(s1.contains(j/4)&&(player==2||!info[0][0].contains(j/4))){
				for(int k: info[player-1][2]){
					if(j/4==k/4&&j!=k){
						return j/4;
					}
				}
			}
		}
		return -1;
	}
	
	
	public HashSet<Integer> DefendFour(int player){
		HashSet<Integer> ans = new HashSet<Integer>();		
		for(int i: info[player-1][2]){
			int m = i%10000;
			if(player==1&&info[0][0].contains(m/4))
				continue;
			for(int j: info[player-1][2]){
				int n = j%10000;
				if(m/4==n/4&&i!=j){
					int x1=i/1000000, y1=(i/10000)%100;
					int x2=j/1000000, y2=(j/10000)%100;
					switch(m%4){
					case 0:
						for(int k=0;k<5;k++){
							if(board[k+x1][y1]==0)
								ans.add(new Integer((x1+k)*100+y1));
						}
						break;
					case 1:
						for(int k=0;k<5;k++){
							if(board[x1][y1+k]==0)
								ans.add(new Integer(x1*100+y1+k));
						}
						break;
					case 2:
						for(int k=0;k<5;k++){
							if(board[x1+k][y1+k]==0)
								ans.add(new Integer((x1+k)*100+y1+k));
						}
						break;
					case 3:
						for(int k=0;k<5;k++){
							if(board[x1+k][y1-k]==0)
								ans.add(new Integer((x1+k)*100+y1-k));
						}
						break;
					}
					switch(n%4){
					case 0:
						for(int k=0;k<5;k++){
							if(board[k+x2][y2]==0)
								ans.add(new Integer((x2+k)*100+y2));
						}
						break;
					case 1:
						for(int k=0;k<5;k++){
							if(board[x2][y2+k]==0)
								ans.add(new Integer(x2*100+y2+k));
						}
						break;
					case 2:
						for(int k=0;k<5;k++){
							if(board[x2+k][y2+k]==0)
								ans.add(new Integer((x2+k)*100+y2+k));
						}
						break;
					case 3:
						for(int k=0;k<5;k++){
							if(board[x2+k][y2-k]==0)
								ans.add(new Integer((x2+k)*100+y2-k));
						}
						break;
					}
					if(player==2)
						ans.removeAll(info[0][0]);
					return ans;
				}
			}
		}
		
		for(int i: info[player-1][2]){
			int m=i%10000;
			if(player==1&&info[0][0].contains(m/4))
				continue;
			for(int j: info[player-1][1]){
				int n=j%10000;
				if(m!=n&&m/4==n/4){
					int x1=i/1000000, y1=(i/10000)%100;
					int x2=j/1000000, y2=(j/10000)%100;
					switch(m%4){
					case 0:
						for(int k=0;k<5;k++){
							if(board[k+x1][y1]==0)
								ans.add(new Integer((x1+k)*100+y1));
						}
						break;
					case 1:
						for(int k=0;k<5;k++){
							if(board[x1][y1+k]==0)
								ans.add(new Integer(x1*100+y1+k));
						}
						break;
					case 2:
						for(int k=0;k<5;k++){
							if(board[x1+k][y1+k]==0)
								ans.add(new Integer((x1+k)*100+y1+k));
						}
						break;
					case 3:
						for(int k=0;k<5;k++){
							if(board[x1+k][y1-k]==0)
								ans.add(new Integer((x1+k)*100+y1-k));
						}
						break;
					}
					switch(n%4){
					case 0:
						if(board[x2][y2]==player&&board[x2+1][y2]==player){
							ans.add(new Integer((x2-1)*100+y2));
							ans.add(new Integer((x2+2)*100+y2));
						}
						else if(board[x2][y2]==player&&board[x2+2][y2]==player){
							ans.add(new Integer((x2+1)*100+y2));
							ans.add(new Integer((x2-1)*100+y2));
							ans.add(new Integer((x2+3)*100+y2));
						}
						else if(board[x2][y2]==player&&board[x2+3][y2]==player){
							ans.add(new Integer((x2+1)*100+y2));
							ans.add(new Integer((x2+2)*100+y2));
						}
						else if(board[x2+1][y2]==player&&board[x2+2][y2]==player){
							ans.add(new Integer(x2*100+y2));
							ans.add(new Integer((x2+3)*100+y2));
						}
						else if(board[x2+1][y2]==player&&board[x2+3][y2]==player){
							ans.add(new Integer(x2*100+y2));
							ans.add(new Integer((x2+2)*100+y2));
							ans.add(new Integer((x2+4)*100+y2));
						}
						else if(board[x2+2][y2]==player&&board[x2+3][y2]==player){
							ans.add(new Integer((x2+1)*100+y2));
							ans.add(new Integer((x2+4)*100+y2));
						}
						break;
					case 1:
						if(board[x2][y2]==player&&board[x2][y2+1]==player){
							ans.add(new Integer(x2*100+y2-1));
							ans.add(new Integer(x2*100+y2+2));
						}
						else if(board[x2][y2]==player&&board[x2][y2+2]==player){
							ans.add(new Integer(x2*100+y2-1));
							ans.add(new Integer(x2*100+y2+1));
							ans.add(new Integer(x2*100+y2+3));
						}
						else if(board[x2][y2]==player&&board[x2][y2+3]==player){
							ans.add(new Integer(x2*100+y2+1));
							ans.add(new Integer(x2*100+y2+2));
						}
						else if(board[x2][y2+1]==player&&board[x2][y2+2]==player){
							ans.add(new Integer(x2*100+y2));
							ans.add(new Integer(x2*100+y2+3));
						}
						else if(board[x2][y2+1]==player&&board[x2][y2+3]==player){
							ans.add(new Integer(x2*100+y2));
							ans.add(new Integer(x2*100+y2+2));
							ans.add(new Integer(x2*100+y2+4));
						}
						else if(board[x2][y2+2]==player&&board[x2][y2+3]==player){
							ans.add(new Integer(x2*100+y2+1));
							ans.add(new Integer(x2*100+y2+4));
						}
						break;
					case 2:
						if(board[x2][y2]==player&&board[x2+1][y2+1]==player){
							ans.add(new Integer((x2-1)*100+y2-1));
							ans.add(new Integer((x2+2)*100+y2+2));
						}
						else if(board[x2][y2]==player&&board[x2+2][y2+2]==player){
							ans.add(new Integer((x2+1)*100+y2+1));
							ans.add(new Integer((x2-1)*100+y2-1));
							ans.add(new Integer((x2+3)*100+y2+3));
						}
						else if(board[x2][y2]==player&&board[x2+3][y2+3]==player){
							ans.add(new Integer((x2+1)*100+y2+1));
							ans.add(new Integer((x2+2)*100+y2+2));
						}
						else if(board[x2+1][y2+1]==player&&board[x2+2][y2+2]==player){
							ans.add(new Integer(x2*100+y2));
							ans.add(new Integer((x2+3)*100+y2+3));
						}
						else if(board[x2+1][y2+1]==player&&board[x2+3][y2+3]==player){
							ans.add(new Integer(x2*100+y2));
							ans.add(new Integer((x2+2)*100+y2+2));
							ans.add(new Integer((x2+4)*100+y2+4));
						}
						else if(board[x2+2][y2+2]==player&&board[x2+3][y2+3]==player){
							ans.add(new Integer((x2+1)*100+y2+1));
							ans.add(new Integer((x2+4)*100+y2+4));
						}
						break;
					case 3:
						if(board[x2][y2]==player&&board[x2+1][y2-1]==player){
							ans.add(new Integer((x2-1)*100+y2+1));
							ans.add(new Integer((x2+2)*100+y2-2));
						}
						else if(board[x2][y2]==player&&board[x2+2][y2-2]==player){
							ans.add(new Integer((x2+1)*100+y2-1));
							ans.add(new Integer((x2-1)*100+y2+1));
							ans.add(new Integer((x2+3)*100+y2-3));
						}
						else if(board[x2][y2]==player&&board[x2+3][y2-3]==player){
							ans.add(new Integer((x2+1)*100+y2-1));
							ans.add(new Integer((x2+2)*100+y2-2));
						}
						else if(board[x2+1][y2-1]==player&&board[x2+2][y2-2]==player){
							ans.add(new Integer(x2*100+y2));
							ans.add(new Integer((x2+3)*100+y2-3));
						}
						else if(board[x2+1][y2-1]==player&&board[x2+3][y2-3]==player){
							ans.add(new Integer(x2*100+y2));
							ans.add(new Integer((x2+2)*100+y2-2));
							ans.add(new Integer((x2+4)*100+y2-4));
						}
						else if(board[x2+2][y2-2]==player&&board[x2+3][y2-3]==player){
							ans.add(new Integer((x2+1)*100+y2-1));
							ans.add(new Integer((x2+4)*100+y2-4));
						}
						break;
					}
					if(player==2)
						ans.removeAll(info[0][0]);
					return ans;
				}
			}
		}
		
		return null;
		
	}
	
	
	
	public HashSet<Integer> DefendThree(int player){
		HashSet<Integer> ans = new HashSet<Integer>();
		
		for(int i: info[player-1][1]){
			int m=i%10000;
			if(player==1&&info[0][0].contains(m/4))
				continue;
			for(int j: info[player-1][1]){
				int n=j%10000;
				if(m!=n&&m/4==n/4){
					int x1=i/1000000, y1=(i/10000)%100;
					int x2=j/1000000, y2=(j/10000)%100;
					switch(m%4){
					case 0:
						if(board[x1][y1]==player&&board[x1+1][y1]==player){
							ans.add(new Integer((x1-1)*100+y1));
							ans.add(new Integer((x1+2)*100+y1));
						}
						else if(board[x1][y1]==player&&board[x1+2][y1]==player){
							ans.add(new Integer((x1+1)*100+y1));
							ans.add(new Integer((x1-1)*100+y1));
							ans.add(new Integer((x1+3)*100+y1));
						}
						else if(board[x1][y1]==player&&board[x1+3][y1]==player){
							ans.add(new Integer((x1+1)*100+y1));
							ans.add(new Integer((x1+2)*100+y1));
						}
						else if(board[x1+1][y1]==player&&board[x1+2][y1]==player){
							ans.add(new Integer(x1*100+y1));
							ans.add(new Integer((x1+3)*100+y1));
						}
						else if(board[x1+1][y1]==player&&board[x1+3][y1]==player){
							ans.add(new Integer(x1*100+y1));
							ans.add(new Integer((x1+2)*100+y1));
							ans.add(new Integer((x1+4)*100+y1));
						}
						else if(board[x1+2][y1]==player&&board[x1+3][y1]==player){
							ans.add(new Integer((x1+1)*100+y1));
							ans.add(new Integer((x1+4)*100+y1));
						}
						break;
					case 1:
						if(board[x1][y1]==player&&board[x1][y1+1]==player){
							ans.add(new Integer(x1*100+y1-1));
							ans.add(new Integer(x1*100+y1+2));
						}
						else if(board[x1][y1]==player&&board[x1][y1+2]==player){
							ans.add(new Integer(x1*100+y1-1));
							ans.add(new Integer(x1*100+y1+1));
							ans.add(new Integer(x1*100+y1+3));
						}
						else if(board[x1][y1]==player&&board[x1][y1+3]==player){
							ans.add(new Integer(x1*100+y1+1));
							ans.add(new Integer(x1*100+y1+2));
						}
						else if(board[x1][y1+1]==player&&board[x1][y1+2]==player){
							ans.add(new Integer(x1*100+y1));
							ans.add(new Integer(x1*100+y1+3));
						}
						else if(board[x1][y1+1]==player&&board[x1][y1+3]==player){
							ans.add(new Integer(x1*100+y1));
							ans.add(new Integer(x1*100+y1+2));
							ans.add(new Integer(x1*100+y1+4));
						}
						else if(board[x1][y1+2]==player&&board[x1][y1+3]==player){
							ans.add(new Integer(x1*100+y1+1));
							ans.add(new Integer(x1*100+y1+4));
						}
						break;
					case 2:
						if(board[x1][y1]==player&&board[x1+1][y1+1]==player){
							ans.add(new Integer((x1-1)*100+y1-1));
							ans.add(new Integer((x1+2)*100+y1+2));
						}
						else if(board[x1][y1]==player&&board[x1+2][y1+2]==player){
							ans.add(new Integer((x1+1)*100+y1+1));
							ans.add(new Integer((x1-1)*100+y1-1));
							ans.add(new Integer((x1+3)*100+y1+3));
						}
						else if(board[x1][y1]==player&&board[x1+3][y1+3]==player){
							ans.add(new Integer((x1+1)*100+y1+1));
							ans.add(new Integer((x1+2)*100+y1+2));
						}
						else if(board[x1+1][y1+1]==player&&board[x1+2][y1+2]==player){
							ans.add(new Integer(x1*100+y1));
							ans.add(new Integer((x1+3)*100+y1+3));
						}
						else if(board[x1+1][y1+1]==player&&board[x1+3][y1+3]==player){
							ans.add(new Integer(x1*100+y1));
							ans.add(new Integer((x1+2)*100+y1+2));
							ans.add(new Integer((x1+4)*100+y1+4));
						}
						else if(board[x1+2][y1+2]==player&&board[x1+3][y1+3]==player){
							ans.add(new Integer((x1+1)*100+y1+1));
							ans.add(new Integer((x1+4)*100+y1+4));
						}
						break;
					case 3:
						if(board[x1][y1]==player&&board[x1+1][y1-1]==player){
							ans.add(new Integer((x1-1)*100+y1+1));
							ans.add(new Integer((x1+2)*100+y1-2));
						}
						else if(board[x1][y1]==player&&board[x1+2][y1-2]==player){
							ans.add(new Integer((x1+1)*100+y1-1));
							ans.add(new Integer((x1-1)*100+y1+1));
							ans.add(new Integer((x1+3)*100+y1-3));
						}
						else if(board[x1][y1]==player&&board[x1+3][y1-3]==player){
							ans.add(new Integer((x1+1)*100+y1-1));
							ans.add(new Integer((x1+2)*100+y1-2));
						}
						else if(board[x1+1][y1-1]==player&&board[x1+2][y1-2]==player){
							ans.add(new Integer(x1*100+y1));
							ans.add(new Integer((x1+3)*100+y1-3));
						}
						else if(board[x1+1][y1-1]==player&&board[x1+3][y1-3]==player){
							ans.add(new Integer(x1*100+y1));
							ans.add(new Integer((x1+2)*100+y1-2));
							ans.add(new Integer((x1+4)*100+y1-4));
						}
						else if(board[x1+2][y1-2]==player&&board[x1+3][y1-3]==player){
							ans.add(new Integer((x1+1)*100+y1-1));
							ans.add(new Integer((x1+4)*100+y1-4));
						}
						break;
					}
					switch(n%4){
					case 0:
						if(board[x2][y2]==player&&board[x2+1][y2]==player){
							ans.add(new Integer((x2-1)*100+y2));
							ans.add(new Integer((x2+2)*100+y2));
						}
						else if(board[x2][y2]==player&&board[x2+2][y2]==player){
							ans.add(new Integer((x2+1)*100+y2));
							ans.add(new Integer((x2-1)*100+y2));
							ans.add(new Integer((x2+3)*100+y2));
						}
						else if(board[x2][y2]==player&&board[x2+3][y2]==player){
							ans.add(new Integer((x2+1)*100+y2));
							ans.add(new Integer((x2+2)*100+y2));
						}
						else if(board[x2+1][y2]==player&&board[x2+2][y2]==player){
							ans.add(new Integer(x2*100+y2));
							ans.add(new Integer((x2+3)*100+y2));
						}
						else if(board[x2+1][y2]==player&&board[x2+3][y2]==player){
							ans.add(new Integer(x2*100+y2));
							ans.add(new Integer((x2+2)*100+y2));
							ans.add(new Integer((x2+4)*100+y2));
						}
						else if(board[x2+2][y2]==player&&board[x2+3][y2]==player){
							ans.add(new Integer((x2+1)*100+y2));
							ans.add(new Integer((x2+4)*100+y2));
						}
						break;
					case 1:
						if(board[x2][y2]==player&&board[x2][y2+1]==player){
							ans.add(new Integer(x2*100+y2-1));
							ans.add(new Integer(x2*100+y2+2));
						}
						else if(board[x2][y2]==player&&board[x2][y2+2]==player){
							ans.add(new Integer(x2*100+y2-1));
							ans.add(new Integer(x2*100+y2+1));
							ans.add(new Integer(x2*100+y2+3));
						}
						else if(board[x2][y2]==player&&board[x2][y2+3]==player){
							ans.add(new Integer(x2*100+y2+1));
							ans.add(new Integer(x2*100+y2+2));
						}
						else if(board[x2][y2+1]==player&&board[x2][y2+2]==player){
							ans.add(new Integer(x2*100+y2));
							ans.add(new Integer(x2*100+y2+3));
						}
						else if(board[x2][y2+1]==player&&board[x2][y2+3]==player){
							ans.add(new Integer(x2*100+y2));
							ans.add(new Integer(x2*100+y2+2));
							ans.add(new Integer(x2*100+y2+4));
						}
						else if(board[x2][y2+2]==player&&board[x2][y2+3]==player){
							ans.add(new Integer(x2*100+y2+1));
							ans.add(new Integer(x2*100+y2+4));
						}
						break;
					case 2:
						if(board[x2][y2]==player&&board[x2+1][y2+1]==player){
							ans.add(new Integer((x2-1)*100+y2-1));
							ans.add(new Integer((x2+2)*100+y2+2));
						}
						else if(board[x2][y2]==player&&board[x2+2][y2+2]==player){
							ans.add(new Integer((x2+1)*100+y2+1));
							ans.add(new Integer((x2-1)*100+y2-1));
							ans.add(new Integer((x2+3)*100+y2+3));
						}
						else if(board[x2][y2]==player&&board[x2+3][y2+3]==player){
							ans.add(new Integer((x2+1)*100+y2+1));
							ans.add(new Integer((x2+2)*100+y2+2));
						}
						else if(board[x2+1][y2+1]==player&&board[x2+2][y2+2]==player){
							ans.add(new Integer(x2*100+y2));
							ans.add(new Integer((x2+3)*100+y2+3));
						}
						else if(board[x2+1][y2+1]==player&&board[x2+3][y2+3]==player){
							ans.add(new Integer(x2*100+y2));
							ans.add(new Integer((x2+2)*100+y2+2));
							ans.add(new Integer((x2+4)*100+y2+4));
						}
						else if(board[x2+2][y2+2]==player&&board[x2+3][y2+3]==player){
							ans.add(new Integer((x2+1)*100+y2+1));
							ans.add(new Integer((x2+4)*100+y2+4));
						}
						break;
					case 3:
						if(board[x2][y2]==player&&board[x2+1][y2-1]==player){
							ans.add(new Integer((x2-1)*100+y2+1));
							ans.add(new Integer((x2+2)*100+y2-2));
						}
						else if(board[x2][y2]==player&&board[x2+2][y2-2]==player){
							ans.add(new Integer((x2+1)*100+y2-1));
							ans.add(new Integer((x2-1)*100+y2+1));
							ans.add(new Integer((x2+3)*100+y2-3));
						}
						else if(board[x2][y2]==player&&board[x2+3][y2-3]==player){
							ans.add(new Integer((x2+1)*100+y2-1));
							ans.add(new Integer((x2+2)*100+y2-2));
						}
						else if(board[x2+1][y2-1]==player&&board[x2+2][y2-2]==player){
							ans.add(new Integer(x2*100+y2));
							ans.add(new Integer((x2+3)*100+y2-3));
						}
						else if(board[x2+1][y2-1]==player&&board[x2+3][y2-3]==player){
							ans.add(new Integer(x2*100+y2));
							ans.add(new Integer((x2+2)*100+y2-2));
							ans.add(new Integer((x2+4)*100+y2-4));
						}
						else if(board[x2+2][y2-2]==player&&board[x2+3][y2-3]==player){
							ans.add(new Integer((x2+1)*100+y2-1));
							ans.add(new Integer((x2+4)*100+y2-4));
						}
						break;
					}
					if(player==2)
						ans.removeAll(info[0][0]);
					return ans;
				}
			}
		}
		
		return null;
		
	}
	
	
	
	public int getDoubleThree(int player){
		HashSet<Integer> s1 = new HashSet<Integer>();
		HashSet<Integer> s2 = new HashSet<Integer>();
		for(int i: info[player-1][1]){
			int j = i%10000;
			if(!s1.contains(j)){
				s1.add(new Integer(j));
				if(s2.contains(j/4)&&(player==2||!info[0][0].contains(j/4))){
					return j/4;
				}
				else
					s2.add(j/4);
			}
		}
		return -1;
	}
	
	
	public int getFakeDoubleThree(int player){
		if(player==1)
			return -1;
		if(info[0][0].size()==0)
			return -1;
		Board temp = new Board(this);
		for(int i: info[0][0])
			temp.set(i/100, i%100, 2);
		return temp.getDoubleThree(player);
	}
	
	public int getFakeDoubleFour(int player){
		if(player==1)
			return -1;
		if(info[0][0].size()==0)
			return -1;
		Board temp = new Board(this);
		for(int i: info[0][0])
			temp.set(i/100, i%100, 2);
		return temp.getDoubleFour(player);
	}
	
	public HashSet<Integer> getFour(int player){
		HashSet<Integer> s = new HashSet<Integer>();
		for(int i: info[player-1][2])
			s.add(new Integer((i%10000)/4));
		if(player==1)
			s.removeAll(info[0][0]);
		return s;
	}
	
	
	
	public HashSet<Integer> getThree(int player){
		HashSet<Integer> ans = new HashSet<Integer>();
		for(int i: info[player-1][1])
			ans.add(new Integer((i%10000)/4));
		if(player==1)
			ans.removeAll(info[0][0]);
		return ans;
	}
	
	
	public HashSet<Integer> getHeuristic(int player){
		HashSet<Integer> ans = new HashSet<Integer>();
		
		if(total==1 && player==2){
			for(int i=0;i<15;i++)
				for(int j=0;j<15;j++)
					if(board[i][j]==1){
						if(i<14&&board[i+1][j]==0){
							ans.add(new Integer((i+1)*100+j));
							return ans;
						}
						if(i>=1&&board[i-1][j]==0){
							ans.add(new Integer((i-1)*100+j));
							return ans;
						}
						if(j<14&&board[i][j+1]==0){
							ans.add(new Integer(i*100+j+1));
							return ans;
						}
						if(j>=1&&board[i][j-1]==0){
							ans.add(new Integer(i*100+j-1));
							return ans;
						}
					}
		}
		
		if(total==2 && player==1){
			if(board[6][6]==2){
				ans.add(new Integer(705));
				return ans;
			}
			else if(board[7][6]==2){
				ans.add(new Integer(606));
				return ans;
			}
			else if(board[8][6]==2){
				ans.add(new Integer(907));
				return ans;
			}
			else if(board[8][7]==2){
				ans.add(new Integer(808));
				return ans;
			}
			else if(board[8][8]==2){
				ans.add(new Integer(709));
				return ans;
			}
			else if(board[7][8]==2){
				ans.add(new Integer(808));
				return ans;
			}
			else if(board[6][8]==2){
				ans.add(new Integer(507));
				return ans;
			}
			else if(board[6][7]==2){
				ans.add(new Integer(606));
				return ans;
			}
			int m=-1, n=-1;
			for(int i=0;i<15;i++){
				for(int j=0;j<15;j++){
					if(board[i][j]==2){
						m=i;
						n=j;
						break;
					}
				}
				if(m!=-1)
					break;
			}
			if(m<=7&&n<=7){
				ans.add(new Integer(608));
				return ans;
			}
			else if(m<=7&&n>=7){
				ans.add(new Integer(606));
				return ans;
			}
			else if(m>=7&&n<=7){
				ans.add(new Integer(808));
				return ans;
			}
			else{
				ans.add(new Integer(806));
				return ans;
			}
		}
		
		int[][] temp = new int[15][];
		for(int i=0;i<15;i++)
			temp[i] = new int[15];
		
		for(int i=2;i<13;i++)
			for(int j=2;j<13;j++)
				if(board[i][j]==0){
					int m=0,n=0;
					for(int k=-2;k<=2;k++){
						if(board[i+k][j]==1)
							m++;
						else if(board[i+k][j]==2)
							n++;
					}
					if(m==1&&n==0){
						if(board[i+1][j]!=0||board[i-1][j]!=0)
							temp[i][j]+=20;
						else
							temp[i][j]+=15;
					}
					if(m==2&&n==0)
						temp[i][j]+=30;
					if(m==0&&n==1){
						if(board[i+1][j]!=0||board[i-1][j]!=0)
							temp[i][j]+=20;
						else
							temp[i][j]+=15;
					}
					if(m==0&&n==2)
						temp[i][j]+=30;
					
					m=0;
					n=0;
					for(int k=-2;k<=2;k++){
						if(board[i][j+k]==1)
							m++;
						else if(board[i][j+k]==2)
							n++;
					}
					if(m==1&&n==0){
						if(board[i][j+1]!=0||board[i][j-1]!=0)
							temp[i][j]+=20;
						else
							temp[i][j]+=15;
					}
					if(m==2&&n==0)
						temp[i][j]+=30;
					if(m==0&&n==1){
						if(board[i][j+1]!=0||board[i][j-1]!=0)
							temp[i][j]+=20;
						else
							temp[i][j]+=15;
					}
					if(m==0&&n==2)
						temp[i][j]+=30;
					
					
					m=0;
					n=0;
					for(int k=-2;k<=2;k++){
						if(board[i+k][j+k]==1)
							m++;
						else if(board[i+k][j+k]==2)
							n++;
					}
					if(m==1&&n==0){
						if(board[i+1][j+1]!=0||board[i-1][j-1]!=0)
							temp[i][j]+=20;
						else
							temp[i][j]+=15;
					}
					if(m==2&&n==0)
						temp[i][j]+=30;
					if(m==0&&n==1){
						if(board[i+1][j+1]!=0||board[i-1][j-1]!=0)
							temp[i][j]+=20;
						else
							temp[i][j]+=15;
					}
					if(m==0&&n==2)
						temp[i][j]+=30;
					
					m=0;
					n=0;
					for(int k=-2;k<=2;k++){
						if(board[i+k][j-k]==1)
							m++;
						else if(board[i+k][j-k]==2)
							n++;
					}
					if(m==1&&n==0){
						if(board[i+1][j-1]!=0||board[i-1][j+1]!=0)
							temp[i][j]+=20;
						else
							temp[i][j]+=15;
					}
					if(m==2&&n==0)
						temp[i][j]+=30;
					if(m==0&&n==1){
						if(board[i+1][j-1]!=0||board[i-1][j+1]!=0)
							temp[i][j]+=20;
						else
							temp[i][j]+=15;
					}
					if(m==0&&n==2)
						temp[i][j]+=30;
				}
		int max = 1;
		for(int i=2;i<13;i++)
			for(int j=2;j<13;j++){
				if(max<temp[i][j]){
					max = temp[i][j];
					ans.clear();
					ans.add(new Integer(i*100+j));
				}
				else if(max==temp[i][j])
					ans.add(new Integer(i*100+j));
			}
		
		if(ans.size()==0){
			for(int i=0;i<15;i++)
				for(int j=0;j<15;j++)
					if(board[i][j]==0){
						ans.add(new Integer(i*100+j));
						if(ans.size()>=3)
							break;
					}
		}
		return ans;
	}
	
	
	
	
	public void print(){
/*		System.out.println("�ڷ���");
		if(info[0][3].size()!=0){
			System.out.println("4-5");
			for(int i: info[0][3])
				System.out.print(i+"\t");
			System.out.println();
		}
		if(info[0][2].size()!=0){
			System.out.println("3-4");
			for(int i: info[0][2])
				System.out.print(i+"\t");
			System.out.println();
		}
		if(info[0][1].size()!=0){
			System.out.println("2-3");
			for(int i: info[0][1])
				System.out.print(i/4+"\t");
			System.out.println();
		}
		System.out.println("�׷���");
		if(info[1][3].size()!=0){
			System.out.println("4-5");
			for(int i: info[1][3])
				System.out.print(i+"\t");
			System.out.println();
		}
		if(info[1][2].size()!=0){
			System.out.println("3-4");
			for(int i: info[1][2])
				System.out.print(i+"\t");
			System.out.println();
		}
		if(info[1][1].size()!=0){
			System.out.println("2-3");
			for(int i: info[1][1])
				System.out.print(i/4+"\t");
			System.out.println();
		}*/
		
		for(int j=0;j<15;j++){
			for(int i=0;i<15;i++)
				System.out.print(board[i][j]);
			System.out.println();
		}
		
	}
	
	public boolean isBanned(int pos, int player){
		if(player==2)
			return false;
		return info[0][0].contains(pos);
	}
	
	public ArrayList<Integer> getBanned(int player){
		if(player==2){
			return new ArrayList<Integer>();
		}
		else{
			return info[0][0];
		}
	}
	
	public boolean isWin(int pos, int player){
		for(int i: info[player-1][3])
			if((i%10000)/4==pos)
				return true;
		return false;
	}
	
	
	public int evaluate(int player){
		int m = evaluate2(player);
		int n = evaluate2(3-player);
		int t = 0;
		if(player==2)
			t = 200*info[0][0].size()+100*alwaysBanned.size();
		return m-n+t;
	}
	
	public int evaluate2(int player){
		if(score[player-1]!=-1)
			return score[player-1];
		ArrayList<Integer>[] current = info[player-1];
		
		if(player==2){
			score[1] = current[3].size()*400+current[2].size()*100
					+current[1].size()*50;
			return score[1];
		}
		
		int count = 0;
		for(int i: current[3]){
			if(!current[0].contains((i%10000)/4))
				count+=400;
		}
		for(int i: current[2]){
			if(!current[0].contains((i%10000)/4))
				count+=100;
		}
		for(int i: current[1]){
			if(!current[0].contains((i%10000)/4))
				count+=50;
		}
		score[0] = count;
		return score[0];
	}
	
	
	
	public void set(int x, int y, int player){		
		score[0] = -1;
		score[1] = -1;
		total++;
		board[x][y] = player;
		int step = x*100+y;
		List<Integer>[] current = info[player-1];
		Iterator<Integer> ii = null;
		for(int i=1;i<=2;i++){
			ii = current[i].iterator();
			while(ii.hasNext()){
				if((((Integer)ii.next())%10000)/4==step)
					ii.remove();
			}
		}
		alwaysBanned.remove(new Integer(step));
		if(player==1){
			Iterator<Integer> i = info[0][3].iterator();
			while(i.hasNext()){
				int p = i.next();
				int j = p/10000;
				int m = j/100;
				int n = j%100;
				int t = p%4;
				if(m==x-5){
					if(n==y&&t==0||n==y-5&&t==2||n==y+5&&t==3){
						alwaysBanned.add(new Integer((p%10000)/4));
						i.remove();
					}
				}
				else if(m==x&&(n==y-5&&t==1||n==y+1&&t==1)){
					alwaysBanned.add(new Integer((p%10000)/4));
					i.remove();
				}
				else if(m==x+1){
					if(n==y-1&&t==3||n==y&&t==0||n==y+1&&t==2){
						alwaysBanned.add(new Integer((p%10000)/4));
						i.remove();
					}
				}
			}
			i = info[0][2].iterator();
			while(i.hasNext()){
				int p = i.next();
				int j = p/10000;
				int m = j/100;
				int n = j%100;
				int t = p%4;
				if(m==x-5){
					if(n==y&&t==0||n==y-5&&t==2||n==y+5&&t==3)
						i.remove();
				}
				else if(m==x&&(n==y-5&&t==1||n==y+1&&t==1))
					i.remove();
				else if(m==x+1){
					if(n==y-1&&t==3||n==y&&t==0||n==y+1&&t==2)
						i.remove();
				}
			}
		}
		
		//����
		int begin = Math.max(x-4, 0);
		int end = Math.min(x, 10);
		for(int i=begin;i<=end;i++){
			if(player==1&&((i>=1&&board[i-1][y]==player)||(i<10&&board[i+5][y]==player))){
				int count = 0;
				int pos = -1;
				for(int j=0;j<5;j++){
					if(board[i+j][y]==player)
						count++;
					else if(board[i+j][y]==3-player){
						count = -1;
						break;
					}
					else 
						pos = j;
				}
				if(count==4)
					alwaysBanned.add(new Integer((i+pos)*100+y));
				continue;
			}
			int count = 0;
			for(int j=0;j<5;j++){
				if(board[i+j][y]==player)
					count++;
				else if(board[i+j][y]==3-player){
					count = -1;
					break;
				}
			}
			if(count == 4){
				int j;
				for(j=0;j<5;j++)
					if(board[i+j][y]==0)
						break;
				current[3].add(new Integer(((i+j)*100+y)*4+(i*100+y)*10000));
				current[2].remove(new Integer(((i+j)*100+y)*4+(i*100+y)*10000));
				continue;
			}
			if(count == 3){
				for(int j=0;j<5;j++)
					if(board[i+j][y]==0){
						current[2].add(new Integer(((i+j)*100+y)*4+(i*100+y)*10000));
					}
				continue;
			}
		}
		
		
		
		//����
		begin = Math.max(y-4, 0);
		end = Math.min(y, 10);
		for(int i=begin;i<=end;i++){
			if(player==1&&((i>=1&&board[x][i-1]==player)||(i<10&&board[x][i+5]==player))){
				int count = 0;
				int pos = -1;
				for(int j=0;j<5;j++){
					if(board[x][i+j]==player)
						count++;
					else if(board[x][i+j]==3-player){
						count = -1;
						break;
					}
					else 
						pos = j;
				}
				if(count==4)
					alwaysBanned.add(new Integer(x*100+i+pos));
				continue;
			}
			int count = 0;
			for(int j=0;j<5;j++){
				if(board[x][i+j]==player){
					count++;
				}
				else if(board[x][i+j]==3-player){
					count = -1;
					break;
				}
			}
			if(count == 4){
				for(int j=0;j<5;j++){
					if(board[x][i+j]==0){
						current[3].add(new Integer((x*100+i+j)*4+1+(x*100+i)*10000));
						current[2].remove(new Integer((x*100+i+j)*4+1+(x*100+i)*10000));
						break;
					}
				}
				continue;
			}
			if(count == 3){
				for(int j=0;j<5;j++)
					if(board[x][i+j]==0){
						current[2].add(new Integer((x*100+i+j)*4+1+(x*100+i)*10000));
					}
				continue;
			}
		}
		
		//����
		begin = Math.min(Math.min(x,y),4);
		end = Math.min(10-Math.max(x, y),0);
		for(int i=-begin;i<=end;i++){
			if(player==1&&((x+i>=1&&y+i>=1&&board[x+i-1][y+i-1]==player)||(x+i<10&&y+i<10&&board[x+i+5][y+i+5]==player))){
				int count = 0;
				int pos = -1;
				for(int j=0;j<5;j++){
					if(board[x+i+j][y+i+j]==player)
						count++;
					else if(board[x+i+j][y+i+j]==3-player){
						count = -1;
						break;
					}
					else 
						pos = j;
				}
				if(count==4)
					alwaysBanned.add(new Integer((x+i+pos)*100+y+i+pos));
				continue;
			}
			int count = 0;
			for(int j=0;j<5;j++){
				if(board[x+i+j][y+i+j]==player)
					count++;
				else if(board[x+i+j][y+i+j]==3-player){
					count = -1;
					break;
				}
			}
			if(count == 4){
				for(int j=0;j<5;j++)
					if(board[x+i+j][y+i+j]==0){
						current[3].add(new Integer(((x+i+j)*100+y+i+j)*4+2+((x+i)*100+y+i)*10000));
						current[2].remove(new Integer(((x+i+j)*100+y+i+j)*4+2+((x+i)*100+y+i)*10000));
						break;
					}
				continue;
			}
			if(count == 3){
				for(int j=0;j<5;j++)
					if(board[x+i+j][y+i+j]==0){
						current[2].add(new Integer(((x+i+j)*100+y+i+j)*4+2+((x+i)*100+y+i)*10000));
					}
				continue;
			}
		}
		
		
		//����
		begin = Math.min(Math.min(x,14-y),4);
		end = Math.min(Math.min(14-x, y),4)-4;
		for(int i=-begin;i<=end;i++){
			if(player==1&&((x+i>=1&&y-i<14&&board[x+i-1][y-i+1]==player)||(x+i<10&&y-i>=5&&board[x+i+5][y-i-5]==player))){
				int count = 0;
				int pos = -1;
				for(int j=0;j<5;j++){
					if(board[x+i+j][y-i-j]==player)
						count++;
					else if(board[x+i+j][y-i-j]==3-player){
						count = -1;
						break;
					}
					else
						pos = j;
				}
				if(count==4)
					alwaysBanned.add(new Integer((x+i+pos)*100+y-i-pos));
				continue;
			}
			int count = 0;
			for(int j=0;j<5;j++){
				if(board[x+i+j][y-i-j]==player)
					count++;
				else if(board[x+i+j][y-i-j]==3-player){
					count = -1;
					break;
				}
			}
			if(count == 4){
				for(int j=0;j<5;j++)
					if(board[x+i+j][y-i-j]==0){
						current[3].add(new Integer(((x+i+j)*100+y-i-j)*4+3+((x+i)*100+y-i)*10000));
						current[2].remove(new Integer((((x+i+j)*100+y-i-j)*4+3)*4+3+((x+i)*100+y-i)*10000));
						break;
					}
				continue;
			}
			if(count == 3){
				for(int j=0;j<5;j++)
					if(board[x+i+j][y-i-j]==0){
						current[2].add(new Integer(((x+i+j)*100+y-i-j)*4+3+((x+i)*100+y-i)*10000));
					}
				continue;
			}
		}
		
		
		
		//���Ƕ���
		current = info[2-player];
		
		for(int i=1;i<=3;i++){
			ii = current[i].iterator();
			while(ii.hasNext()){
				if((((Integer)ii.next())%10000)/4==step)
					ii.remove();
			}
		}
		
		
		
		//����
		begin = Math.max(x-4, 0);
		end = Math.min(x, 10);
		for(int i=begin;i<=end;i++){
			int count = 0;
			for(int j=0;j<5;j++){
				if(i+j==x)
					continue;
				if(board[i+j][y]==3-player)
					count++;
				else if(board[i+j][y]==player){
					count = -1;
					break;
				}
			}
			if(count == 3){
				for(int j=0;j<5;j++)
					if(board[i+j][y]==0){
						current[2].remove(new Integer(((i+j)*100+y)*4+(i*100+y)*10000));
						break;
					}
			}
		}
		
		//����
		begin = Math.max(y-4, 0);
		end = Math.min(y, 10);
		for(int i=begin;i<=end;i++){
			int count = 0;
			for(int j=0;j<5;j++){
				if(i+j==y)
					continue;
				if(board[x][i+j]==3-player){
					count++;
				}
				else if(board[x][i+j]==player){
					count = -1;
					break;
				}
			}
			if(count == 3){
				for(int j=0;j<5;j++)
					if(board[x][i+j]==0){
						current[2].remove(new Integer((x*100+i+j)*4+1+(x*100+i)*10000));
						break;
					}
				continue;
			}
		}
		
		
		//����
		begin = Math.min(Math.min(x,y),4);
		end = Math.min(10-Math.max(x, y),0);
		for(int i=-begin;i<=end;i++){
			int count = 0;
			for(int j=0;j<5;j++){
				if(i+j==0)
					continue;
				if(board[x+i+j][y+i+j]==3-player)
					count++;
				else if(board[x+i+j][y+i+j]==player){
					count = -1;
					break;
				}
			}
			if(count == 3){
				for(int j=0;j<5;j++)
					if(board[x+i+j][y+i+j]==0){
						current[2].remove(new Integer(((x+i+j)*100+y+i+j)*4+2+((x+i)*100+y+i)*10000));
						break;
					}
				continue;
			}
		}
		
		//����
		begin = Math.min(Math.min(x,14-y),4);
		end = Math.min(Math.min(14-x, y),4)-4;
		for(int i=-begin;i<=end;i++){
			int count = 0;
			for(int j=0;j<5;j++){
				if(i+j==0)
					continue;
				if(board[x+i+j][y-i-j]==3-player)
					count++;
				else if(board[x+i+j][y-i-j]==player){
					count = -1;
					break;
				}
			}
			if(count == 3){
				for(int j=0;j<5;j++)
					if(board[x+i+j][y-i-j]==0){
						current[2].remove(new Integer(((x+i+j)*100+y-i-j)*4+3+((x+i)*100+y-i)*10000));
						break;
					}
				continue;
			}
		}
		
		
		
		//���ǻ���
		
		
		//����
		if((x-5)>=0&&board[x-5][y]==0){
			int m=0,n=0,t=0;
			int[] pos = new int[5];
			for(int i=x-4;i<=x-1;i++){
				if(board[i][y]==1){
					m++;
				}
				else if(board[i][y]==2){
					n++;
				}
				else{
					pos[t] = i;
					t++;
				}
			}
			if(m==2&&n==0){
				info[0][1].remove(new Integer((pos[0]*100+y)*4+((x-4)*100+y)*10000));
				info[0][1].remove(new Integer((pos[1]*100+y)*4+((x-4)*100+y)*10000));
			}
			else if(m==0&&n==2){
				info[1][1].remove(new Integer((pos[0]*100+y)*4+((x-4)*100+y)*10000));
				info[1][1].remove(new Integer((pos[1]*100+y)*4+((x-4)*100+y)*10000));
			}
		}
		
		if((x+5)<=14&&board[x+5][y]==0){
			int m=0,n=0,t=0;
			int[] pos = new int[5];
			for(int i=x+1;i<=x+4;i++){
				if(board[i][y]==1){
					m++;
				}
				else if(board[i][y]==2){
					n++;
				}
				else{
					pos[t] = i;
					t++;
				}
			}
			if(m==2&&n==0){
				info[0][1].remove(new Integer((pos[0]*100+y)*4+((x+1)*100+y)*10000));
				info[0][1].remove(new Integer((pos[1]*100+y)*4+((x+1)*100+y)*10000));
			}
			else if(m==0&&n==2){
				info[1][1].remove(new Integer((pos[0]*100+y)*4+((x+1)*100+y)*10000));
				info[1][1].remove(new Integer((pos[1]*100+y)*4+((x+1)*100+y)*10000));
			}
		}
		for(int i=4;i>=1;i--)
			if((x-i)>=0&&(x+5-i)<=14&&board[x-i][y]==0&&board[x+5-i][y]==0)
				check0(x,y,player,x-i+1);
		
		
		
		//����
		
		if((y-5)>=0&&board[x][y-5]==0){
			int m=0,n=0,t=0;
			int[] pos = new int[5];
			for(int i=y-4;i<=y-1;i++){
				if(board[x][i]==1){
					m++;
				}
				else if(board[x][i]==2){
					n++;
				}
				else{
					pos[t] = i;
					t++;
				}
			}
			if(m==2&&n==0){
				info[0][1].remove(new Integer((x*100+pos[0])*4+1+(x*100+y-4)*10000));
				info[0][1].remove(new Integer((x*100+pos[1])*4+1+(x*100+y-4)*10000));
			}
			else if(m==0&&n==2){
				info[1][1].remove(new Integer((x*100+pos[0])*4+1+(x*100+y-4)*10000));
				info[1][1].remove(new Integer((x*100+pos[1])*4+1+(x*100+y-4)*10000));
			}
		}
		if((y+5)<=14&&board[x][y+5]==0){
			int m=0,n=0,t=0;
			int[] pos = new int[5];
			for(int i=y+1;i<=y+4;i++){
				if(board[x][i]==1){
					m++;
				}
				else if(board[x][i]==2){
					n++;
				}
				else{
					pos[t] = i;
					t++;
				}
			}
			if(m==2&&n==0){
				info[0][1].remove(new Integer((x*100+pos[0])*4+1+(x*100+y+1)*10000));
				info[0][1].remove(new Integer((x*100+pos[1])*4+1+(x*100+y+1)*10000));
			}
			else if(m==0&&n==2){
				info[1][1].remove(new Integer((x*100+pos[0])*4+1+(x*100+y+1)*10000));
				info[1][1].remove(new Integer((x*100+pos[1])*4+1+(x*100+y+1)*10000));
			}
		}
		for(int i=4;i>=1;i--)
			if((y-i)>=0&&(y+5-i)<=14&&board[x][y-i]==0&&board[x][y+5-i]==0)
				check1(x,y,player,y-i+1);
		
		
		
		//����
		if((x-5)>=0&&(y-5)>=0&&board[x-5][y-5]==0){
			int m=0,n=0,t=0;
			int[] pos = new int[5];
			for(int i=-4;i<=-1;i++){
				if(board[x+i][y+i]==1){
					m++;
				}
				else if(board[x+i][y+i]==2){
					n++;
				}
				else{
					pos[t] = i;
					t++;
				}
			}
			if(m==2&&n==0){
				info[0][1].remove(new Integer(((x+pos[0])*100+pos[0]+y)*4+2+((x-4)*100+y-4)*10000));
				info[0][1].remove(new Integer(((x+pos[1])*100+pos[1]+y)*4+2+((x-4)*100+y-4)*10000));
			}
			else if(m==0&&n==2){
				info[1][1].remove(new Integer(((x+pos[0])*100+pos[0]+y)*4+2+((x-4)*100+y-4)*10000));
				info[1][1].remove(new Integer(((x+pos[1])*100+pos[1]+y)*4+2+((x-4)*100+y-4)*10000));
			}
		}
		if((x+5)<=14&&(y+5)<=14&&board[x+5][y+5]==0){
			int m=0,n=0,t=0;
			int[] pos = new int[5];
			for(int i=1;i<=4;i++){
				if(board[x+i][y+i]==1){
					m++;
				}
				else if(board[x+i][y+i]==2){
					n++;
				}
				else{
					pos[t] = i;
					t++;
				}
			}
			if(m==2&&n==0){
				info[0][1].remove(new Integer(((x+pos[0])*100+pos[0]+y)*4+2+((x+1)*100+y+1)*10000));
				info[0][1].remove(new Integer(((x+pos[1])*100+pos[1]+y)*4+2+((x+1)*100+y+1)*10000));
			}
			else if(m==0&&n==2){
				info[1][1].remove(new Integer(((x+pos[0])*100+pos[0]+y)*4+2+((x+1)*100+y+1)*10000));
				info[1][1].remove(new Integer(((x+pos[1])*100+pos[1]+y)*4+2+((x+1)*100+y+1)*10000));
			}
		}
		for(int i=4;i>=1;i--)
			if((x-i)>=0&&(y-i)>=0&&(x+5-i)<=14&&(y+5-i)<=14&&board[x-i][y-i]==0&&board[x+5-i][y+5-i]==0)
				check2(x,y,player,-i+1);
		
		
		
		
		//����
		if((x-5)>=0&&(y+5)<=14&&board[x-5][y+5]==0){
			int m=0,n=0,t=0;
			int[] pos = new int[5];
			for(int i=-4;i<=-1;i++){
				if(board[x+i][y-i]==1){
					m++;
				}
				else if(board[x+i][y-i]==2){
					n++;
				}
				else{
					pos[t] = i;
					t++;
				}
			}
			if(m==2&&n==0){
				info[0][1].remove(new Integer(((x+pos[0])*100-pos[0]+y)*4+3+((x-4)*100+y+4)*10000));
				info[0][1].remove(new Integer(((x+pos[1])*100-pos[1]+y)*4+3+((x-4)*100+y+4)*10000));
			}
			else if(m==0&&n==2){
				info[1][1].remove(new Integer(((x+pos[0])*100-pos[0]+y)*4+3+((x-4)*100+y+4)*10000));
				info[1][1].remove(new Integer(((x+pos[1])*100-pos[1]+y)*4+3+((x-4)*100+y+4)*10000));
			}
		}
		
		if((x+5)<=14&&(y-5)>=0&&board[x+5][y-5]==0){
			int m=0,n=0,t=0;
			int[] pos = new int[5];
			for(int i=1;i<=4;i++){
				if(board[x+i][y-i]==1){
					m++;
				}
				else if(board[x+i][y-i]==2){
					n++;
				}
				else{
					pos[t] = i;
					t++;
				}
			}
			if(m==2&&n==0){
				info[0][1].remove(new Integer(((x+pos[0])*100-pos[0]+y)*4+3+((x+1)*100+y-1)*10000));
				info[0][1].remove(new Integer(((x+pos[1])*100-pos[1]+y)*4+3+((x+1)*100+y-1)*10000));
			}
			else if(m==0&&n==2){
				info[1][1].remove(new Integer(((x+pos[0])*100-pos[0]+y)*4+3+((x+1)*100+y-1)*10000));
				info[1][1].remove(new Integer(((x+pos[1])*100-pos[1]+y)*4+3+((x+1)*100+y-1)*10000));
			}
		}
		for(int i=4;i>=1;i--)
			if((x-i)>=0&&(y+i)<=14&&(x+5-i)<=14&&(y-5+i)>=0&&board[x-i][y+i]==0&&board[x+5-i][y-5+i]==0)
				check3(x,y,player,-i+1);
		
		
		
		
		info[0][0].clear();
		info[0][0].addAll(alwaysBanned);
		HashSet<Integer> s= new HashSet<Integer>();
		HashSet<Integer> s2 = new HashSet<Integer>();
		
		for(int i: info[0][2]){
			int j = i%10000;
			if(!s2.contains(j)){
				s2.add(new Integer(j));
				if(s.contains(j/4))
					info[0][0].add(new Integer(j/4));
				else
					s.add(new Integer(j/4));
			}
		}
		s.clear();
		s2.clear();
		HashSet<Integer> s3 = new HashSet<Integer>();
		for(int i: info[0][2])
			s3.add(new Integer(i%10000));
		for(int i: info[0][1]){
			int j = i%10000;
			if(s3.contains(j))
				continue;
			if(!s2.contains(j)){
				s2.add(new Integer(j));
				if(s.contains(j/4))
					info[0][0].add(new Integer(j/4));
				else
					s.add(new Integer(j/4));
			}
		}
	}
	
	
	
	public void check3(int x, int y, int player, int begin){
		int m=0,n=0,t=0;
		int[] pos = new int[5];
		for(int i=begin;i<=begin+3;i++){
			if(board[x+i][y-i]==1){
				m++;
			}
			else if(board[x+i][y-i]==2){
				n++;
			}
			else{
				pos[t] = i;
				t++;
			}
		}
		if(player==1){
			if(m==1&&n==2){
				info[1][1].remove(new Integer(((x+pos[0])*100-pos[0]+y)*4+3+((x+begin)*100+y-begin)*10000));
				info[1][1].remove(new Integer((x*100+y)*4+3+((x+begin)*100+y-begin)*10000));
			}
			else if(m==2&&n==0){
				info[0][1].add(new Integer(((x+pos[0])*100-pos[0]+y)*4+3+((x+begin)*100+y-begin)*10000));
				info[0][1].add(new Integer(((x+pos[1])*100-pos[1]+y)*4+3+((x+begin)*100+y-begin)*10000));
			}
			else if(m==3&&n==0){
				info[0][1].remove(new Integer(((x+pos[0])*100-pos[0]+y)*4+3+((x+begin)*100+y-begin)*10000));
				info[0][1].remove(new Integer((x*100+y)*4+3+((x+begin)*100+y-begin)*10000));
			}
		}
		else{
			if(m==2&&n==1){
				info[0][1].remove(new Integer(((x+pos[0])*100-pos[0]+y)*4+3+((x+begin)*100+y-begin)*10000));
				info[0][1].remove(new Integer((x*100+y)*4+3+((x+begin)*100+y-begin)*10000));
			}
			else if(m==0&&n==2){
				info[1][1].add(new Integer(((x+pos[0])*100-pos[0]+y)*4+3+((x+begin)*100+y-begin)*10000));
				info[1][1].add(new Integer(((x+pos[1])*100-pos[1]+y)*4+3+((x+begin)*100+y-begin)*10000));
			}
			else if(m==0&&n==3){
				info[1][1].remove(new Integer(((x+pos[0])*100-pos[0]+y)*4+3+((x+begin)*100+y-begin)*10000));
				info[1][1].remove(new Integer((x*100+y)*4+3+((x+begin)*100+y-begin)*10000));
			}
		}
	}
	
	public void check2(int x, int y, int player, int begin){
		int m=0,n=0,t=0;
		int[] pos = new int[5];
		for(int i=begin;i<=begin+3;i++){
			if(board[x+i][y+i]==1){
				m++;
			}
			else if(board[x+i][y+i]==2){
				n++;
			}
			else{
				pos[t] = i;
				t++;
			}
		}
		
		if(player==1){
			if(m==1&&n==2){
				info[1][1].remove(new Integer(((x+pos[0])*100+pos[0]+y)*4+2+((x+begin)*100+y+begin)*10000));
				info[1][1].remove(new Integer((x*100+y)*4+2+((x+begin)*100+y+begin)*10000));
			}
			else if(m==2&&n==0){
				info[0][1].add(new Integer(((x+pos[0])*100+pos[0]+y)*4+2+((x+begin)*100+y+begin)*10000));
				info[0][1].add(new Integer(((x+pos[1])*100+pos[1]+y)*4+2+((x+begin)*100+y+begin)*10000));
			}
			else if(m==3&&n==0){
				info[0][1].remove(new Integer(((x+pos[0])*100+pos[0]+y)*4+2+((x+begin)*100+y+begin)*10000));
				info[0][1].remove(new Integer((x*100+y)*4+2+((x+begin)*100+y+begin)*10000));
			}
		}
		else{
			if(m==2&&n==1){
				info[0][1].remove(new Integer(((x+pos[0])*100+pos[0]+y)*4+2+((x+begin)*100+y+begin)*10000));
				info[0][1].remove(new Integer((x*100+y)*4+2+((x+begin)*100+y+begin)*10000));
			}
			else if(m==0&&n==2){
				info[1][1].add(new Integer(((x+pos[0])*100+pos[0]+y)*4+2+((x+begin)*100+y+begin)*10000));
				info[1][1].add(new Integer(((x+pos[1])*100+pos[1]+y)*4+2+((x+begin)*100+y+begin)*10000));
			}
			else if(m==0&&n==3){
				info[1][1].remove(new Integer(((x+pos[0])*100+pos[0]+y)*4+2+((x+begin)*100+y+begin)*10000));
				info[1][1].remove(new Integer((x*100+y)*4+2+((x+begin)*100+y+begin)*10000));
			}
		}
	}
	
	
	public void check1(int x, int y, int player, int begin){
		int m=0,n=0,t=0;
		int[] pos = new int[5];
		for(int i=begin;i<=begin+3;i++){
			if(board[x][i]==1){
				m++;
			}
			else if(board[x][i]==2){
				n++;
			}
			else{
				pos[t] = i;
				t++;
			}
		}
		if(player==1){
			if(m==1&&n==2){
				info[1][1].remove(new Integer((x*100+pos[0])*4+1+(x*100+begin)*10000));
				info[1][1].remove(new Integer((x*100+y)*4+1+(x*100+begin)*10000));
			}
			else if(m==2&&n==0){
				info[0][1].add(new Integer((x*100+pos[0])*4+1+(x*100+begin)*10000));
				info[0][1].add(new Integer((x*100+pos[1])*4+1+(x*100+begin)*10000));
			}
			else if(m==3&&n==0){
				info[0][1].remove(new Integer((x*100+pos[0])*4+1+(x*100+begin)*10000));
				info[0][1].remove(new Integer((x*100+y)*4+1+(x*100+begin)*10000));
			}
		}
		else{
			if(m==2&&n==1){
				info[0][1].remove(new Integer((x*100+pos[0])*4+1+(x*100+begin)*10000));
				info[0][1].remove(new Integer((x*100+y)*4+1+(x*100+begin)*10000));
			}
			else if(m==0&&n==2){
				info[1][1].add(new Integer((x*100+pos[0])*4+1+(x*100+begin)*10000));
				info[1][1].add(new Integer((x*100+pos[1])*4+1+(x*100+begin)*10000));
			}
			else if(m==0&&n==3){
				info[1][1].remove(new Integer((x*100+pos[0])*4+1+(x*100+begin)*10000));
				info[1][1].remove(new Integer((x*100+y)*4+1+(x*100+begin)*10000));
			}
		}
	}
	
	
	public void check0(int x, int y, int player, int begin){
		int m=0,n=0,t=0;
		int[] pos = new int[5];
		for(int i=begin;i<=begin+3;i++){
			if(board[i][y]==1){
				m++;
			}
			else if(board[i][y]==2){
				n++;
			}
			else{
				pos[t] = i;
				t++;
			}
		}
		if(player==1){
			if(m==1&&n==2){
				info[1][1].remove(new Integer((pos[0]*100+y)*4+(begin*100+y)*10000));
				info[1][1].remove(new Integer((x*100+y)*4+(begin*100+y)*10000));
			}
			else if(m==2&&n==0){
				info[0][1].add(new Integer((pos[0]*100+y)*4+(begin*100+y)*10000));
				info[0][1].add(new Integer((pos[1]*100+y)*4+(begin*100+y)*10000));
			}
			else if(m==3&&n==0){
				info[0][1].remove(new Integer((pos[0]*100+y)*4+(begin*100+y)*10000));
				info[0][1].remove(new Integer((x*100+y)*4+(begin*100+y)*10000));
			}
		}
		else{
			if(m==2&&n==1){
				info[0][1].remove(new Integer((pos[0]*100+y)*4+(begin*100+y)*10000));
				info[0][1].remove(new Integer((x*100+y)*4));
			}
			else if(m==0&&n==2){
				info[1][1].add(new Integer((pos[0]*100+y)*4+(begin*100+y)*10000));
				info[1][1].add(new Integer((pos[1]*100+y)*4+(begin*100+y)*10000));
			}
			else if(m==0&&n==3){
				info[1][1].remove(new Integer((pos[0]*100+y)*4+(begin*100+y)*10000));
				info[1][1].remove(new Integer((x*100+y)*4+(begin*100+y)*10000));
			}
		}
	}
}
