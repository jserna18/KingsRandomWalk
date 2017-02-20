import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by juaker505 on 2/19/2017.
 */
public class Main
{
  private ArrayList<String>[][] results;
  private boolean startPos = true;
  Random rand = new Random();
  FileWriter writer;
  BufferedWriter bufferedWriter;

  private void start(int n, int row, int col)
  {
    int move;
    int startRow = row;
    int startCol = col;
    String dir = "Did Not Fall";

    for(int i = 0;i < n; i++)
    {
      move = rand.nextInt(8);

      switch (move)
      {
        case 0:
          row--;
          col--;
          break;
        case 1:
          row--;
          break;
        case 2:
          row--;
          col++;
          break;
        case 3:
          col--;
          break;
        case 4:
          col++;
          break;
        case 5:
          row++;
          col--;
          break;
        case 6:
          row++;
          break;
        case 7:
          row++;
          col++;
      }

      if (row >= n)
      {
        dir = "South";

        if (col < 0)
        {
          dir += "West " + i;
        }
        else if (col >= n)
        {
          dir += "East " + i;
        }
        else
        {
          dir += " " + i;
        }
        break;
      }
      else if (row < 0)
      {
        dir = "North";

        if (col < 0)
        {
          dir += "West " + i;
        }
        else if (col >= n)
        {
          dir += "East " + i;
        }
        else
        {
          dir += " " + i;
        }
        break;
      }
      else if(col >= n)
      {
        dir = "East " + i;
        break;
      }
      else if(col < 0)
      {
        dir = "West " + i;
        break;
      }
    }
    System.out.println(row + " " +col + "-> " +dir);
    results[startRow][startCol].add(dir);
  }

  private void readResults(int n)
  {
    int north = 0;
    int south = 0;
    int east = 0;
    int west = 0;
    int notFall = 0;

    for(int i = 0; i < n; i++)
      for(int j = 0; j < n; j++)
      {
        for (String str: results[i][j])
        {
          int space = str.lastIndexOf(" ");
          String dir = str.substring(0, space);
          String steps = str.substring(space+1, str.length());
          switch (dir)
          {
            case "North": north++; break;
            case "South": south++; break;
            case "East": east++; break;
            case "West": west++; break;
            case "NorthWest": north++; west++; break;
            case "NorthEast": north++; east++; break;
            case "SouthWest": south++; west++; break;
            case "SouthEast": south++; east++; break;
            default: notFall++;
          }
        }
        writeResult(i, j, north, south, east, west, notFall);

        north = 0;
        south = 0;
        east = 0;
        west = 0;
        notFall = 0;
      }
  }
  private void eachEdgeProb(int total, int dir)
  {

  }
  private void writeResult(int row, int col, int n, int s, int ea, int w, int not)
  {
    int total = n + s + ea + w + not;
    double probN = n/total;
    double probS = s/total;
    double probE = ea/total;
    double probW = w/total;
    double probNot = not/total;

    try
    {
      bufferedWriter.write("Testing " + total + "Trials");
      bufferedWriter.newLine();
      bufferedWriter.write("Row: " + row + " Column: " + col);
      bufferedWriter.newLine();
      bufferedWriter.write("Fell North "+n+" Times with Probability: "+probN);
      bufferedWriter.write("Fell South "+n+" Times with Probability: "+probN);
      bufferedWriter.write("Fell East "+n+" Times with Probability: "+probN);
      bufferedWriter.write("Fell West "+n+" Times with Probability: "+probN);

    } catch (IOException e)
    {
      e.printStackTrace();
    }


  }

  private void initBoard(int n)
  {
    results = new ArrayList[n][n];
    for(int i = 0 ; i < results.length; i++)
      for(int j = 0; j < results[i].length; j++)
      {
        results[i][j] = new ArrayList<>();
      }

    try
    {
      writer = new FileWriter("KingRandomWalk.txt");
    } catch (IOException e)
    {
      e.printStackTrace();
    }
    bufferedWriter = new BufferedWriter(writer);

    for(int row = 0; row < n; row++)
      for(int col = 0; col < n; col++)
        for(int trial = 0; trial < 100; trial++)
        {
          start(n, row, col);
        }

    readResults(n);
    try
    {
      bufferedWriter.close();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public static void main(String args[])
  {
    Main game = new Main();
    game.initBoard(20);
  }
}
