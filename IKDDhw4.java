import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

class rank
{
	int index;
	double score;
	public rank(int i,double s)
	{
		index = i;
		score = s;
	}
}
class cmp implements Comparator<rank>
{
	public int compare(rank r1, rank r2)
	{
		if(r1.score < r2.score) return 1;
		if(r1.score > r2.score) return -1;
		return 0;
	}
}
public class IKDDhw4
{
	public static ArrayList<Integer>[] graph = new ArrayList[6];
	public static ArrayList<Integer>[] Graph = new ArrayList[6];
	public static ArrayList<Integer> DeadEnd = new ArrayList<Integer>();
	public static ArrayList<rank> pagerank = new ArrayList<rank>();
	public static boolean[] rec = new boolean[6];
	public static boolean[] IsContain = new boolean[6];
	public static double[][] matrix = new double[6][6];
	public static double[] var = new double[6];
	public static String readTxt(String filename) throws IOException
	{
		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();
		StringBuilder sb = new StringBuilder();
		while(line != null)
		{
			sb.append(line);
			line = br.readLine();
		}
		return sb.toString();
	}
	public static void removeDeadEnd()
	{
		for(int i=1;i<6;i++)
		{
			if( graph[i].size() == 0 && rec[i] == false)
			{
				DeadEnd.add(i);
				rec[i] = true;
				for(int j=1;j<6;j++)
					for(int k=0;k<graph[j].size();k++)
						if(graph[j].get(k) == i)
							graph[j].remove(k);
				removeDeadEnd();
			}
		}
	}
	public static void ComputePageRank()
	{
		int cnt = 0;
		for(int i=1;i<6;i++)
		{
			double p = graph[i].size();
			if( graph[i].size() == 0)
			{
				rec[i] = false;
				cnt++;
			}
			else
				p = 1/(double) graph[i].size();
			for(int j=1;j<6;j++)
				matrix[j][i] = 0;
			for(int j=0;j<graph[i].size();j++)
				matrix[graph[i].get(j)][i] = p;
		}
		for(int i=1;i<6;i++)
		{
			if(rec[i])
				var[i] = 1/(double)(5-cnt);
		}
		while(true)
		{
			double[] tmp = new double[6];
			for(int i=1;i<6;i++)
			{
				double v = 0;
				for(int j=1;j<6;j++)
				{
					if(rec[i] && rec[j])
					{
						v += matrix[i][j] * var[j];
					}
				}
				tmp[i] = v;
			}
			int check = 0;
			for(int i=1;i<6;i++)
			{
				if( rec[i])
				{
					if( Math.abs(tmp[i] - var[i]) > 0.00000001)
					{
						check=1;
						break;
					}
				}
			}
			if(check == 1)
				for(int i=1;i<6;i++)
					var[i] = tmp[i];
			else
				break;
		}
	}
	public static void main(String[] args) throws IOException
	{
		
		Arrays.fill(rec, Boolean.FALSE);
		String[] pg = new String[6];
		String[] file = new String[6];
		Arrays.fill(var, 0);
		pg[1] = "http://page1.txt"; file[1] = "page1.txt";
		pg[2] = "http://page2.txt"; file[2] = "page2.txt";
		pg[3] = "http://page3.txt"; file[3] = "page3.txt";
		pg[4] = "http://page4.txt"; file[4] = "page4.txt";
		pg[5] = "http://page5.txt"; file[5] = "page5.txt";
		String page;
		Arrays.fill(IsContain, Boolean.FALSE);
		for(int i=1;i<6;i++)
		{
			graph[i] = new ArrayList<Integer>();
			Graph[i] = new ArrayList<Integer>();
			page = readTxt(file[i]);
			if(page.contains(args[0]))
				IsContain[i] = true;
			for(int j=1;j<6;j++)
			{
				if( page.contains(pg[j]))
				{
					graph[i].add(j);
					Graph[i].add(j);
				}
			}
		}
		removeDeadEnd();
		Arrays.fill(rec, Boolean.TRUE);
		ComputePageRank();
		for(int i=DeadEnd.size()-1;i>=0;i--)
			for(int j=1;j<6;j++)
				for(int k=0;k<Graph[j].size();k++)
					if(Graph[j].get(k) == DeadEnd.get(i))
						var[ DeadEnd.get(i) ] += var[j] * 1/(double) Graph[j].size();
		for(int i=1;i<6;i++)
			pagerank.add(new rank(i,var[i]));
		Collections.sort(pagerank,new cmp());
		int rak = 1;
		System.out.println("+---------------+---------------+");
		System.out.println("|Rank           |Filename       |");
		System.out.println("+---------------+---------------+");
		for(int i=0;i<pagerank.size();i++)
		{
			if(IsContain[pagerank.get(i).index])
			{
				System.out.println("|"+rak+"              |"+"Page"+pagerank.get(i).index+".txt"+"      |");
				System.out.println("+---------------+---------------+");
				rak++;
			}
		}
		
	}
}
