import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Joaquin Serna on 2/19/2017.
 */
public class Main
{
  private ArrayList<String>[][] results;
  private ArrayList<Integer>[][] stepList;
  private ArrayList<Point> points = new ArrayList<>();
  DecimalFormat df = new DecimalFormat("#.###");
  Random rand = new Random();
  FileWriter writer;
  FileWriter writer2;
  FileWriter writer3;
  BufferedWriter bufferedWriter3;
  BufferedWriter bufferedWriter;
  BufferedWriter bufferedWriter2;
  int totalTrials = 100;
  boolean flag = false;

  private void start(int n, int row, int col)
  {
    int move;
    int startRow = row;
    int startCol = col;
    int prevRow = row;
    int prevCol = col;
    String dir = "Did Not Fall";

    for(int step = 1; step <= (n*5); step++)
    {
      move = rand.nextInt(8);
      prevRow = row;
      prevCol = col;

      switch (move)
      {
        case 0: row--; col--; break;
        case 1: row--; break;
        case 2: row--; col++; break;
        case 3: col--; break;
        case 4: col++; break;
        case 5: row++; col--; break;
        case 6: row++; break;
        case 7: row++; col++;
      }
      if (row >= (n-1))
      {
        stepList[startRow][startCol].add(step);
        dir = "South";

        if (col < 0)
        {
          dir += "West";
          break;
        }
        else if (col >= (n-1))
        {
          dir += "East";
          break;
        }
        break;
      }
      else if (row < 0)
      {
        stepList[startRow][startCol].add(step);
        dir = "North";

        if (col < 0)
        {
          dir += "West";
          break;
        }
        else if (col >= (n-1))
        {
          dir += "East";
          break;
        }
        break;
      }
      else if(col >= (n-1))
      {
        stepList[startRow][startCol].add(step);
        dir = "East";
        break;
      }
      else if(col < 0)
      {
        stepList[startRow][startCol].add(step);
        dir = "West";
        break;
      }
    }
    if(flag && (!dir.equals("Did Not Fall")))
    {
      Point point = new Point(prevRow, prevCol);
      points.add(point);
    }
    else
    {
      results[startRow][startCol].add(dir);
    }
  }

  private void getSquares(int n)
  {
    int count = 0;

    for(int i = 0; i < n; i++)
      for(int j = 0; j < n; j++)
      {
        for(Point pnt: points)
        {
          if((pnt.x == i) && (pnt.y == j))
          {
            count++;
          }
        }
        try
        {
          bufferedWriter3.write(i+"");
          bufferedWriter3.append(',');
          bufferedWriter3.write(j+"");
          bufferedWriter3.append(',');
          bufferedWriter3.write(count+"");
          bufferedWriter3.newLine();
        } catch (IOException e)
        {
          e.printStackTrace();
        }
        count = 0;
      }
  }

  private void writeProbHeader()
  {
    try
    {
      bufferedWriter.write("Testing " + totalTrials + " Trials");
      bufferedWriter.newLine();
      bufferedWriter.write("X");
      bufferedWriter.append(',');
      bufferedWriter.write("Y");
      bufferedWriter.append(',');
      bufferedWriter.write("North");
      bufferedWriter.append(',');
      bufferedWriter.write("South");
      bufferedWriter.append(',');
      bufferedWriter.write("East");
      bufferedWriter.append(',');
      bufferedWriter.write("West");
      bufferedWriter.append(',');
      bufferedWriter.write("No Fall");
      bufferedWriter.newLine();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void writeExpHeader()
  {
    try
    {
      bufferedWriter2.write("Row");
      bufferedWriter2.append(',');
      bufferedWriter2.write("Column");
      bufferedWriter2.append(',');
      bufferedWriter2.write("Total Steps");
      bufferedWriter2.append(',');
      bufferedWriter2.write("Times Fell");
      bufferedWriter2.append(',');
      bufferedWriter2.write("Expected Number of Steps to Fall");
      bufferedWriter2.newLine();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void writeSquareHeader()
  {
    try
    {
      bufferedWriter3.write("Starting at [2][4]");
      bufferedWriter3.newLine();
      bufferedWriter3.write("Row");
      bufferedWriter3.append(',');
      bufferedWriter3.write("Column");
      bufferedWriter3.append(',');
      bufferedWriter3.write("Times Fell");
      bufferedWriter3.newLine();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  private void readResults(int n)
  {
    int north = 0;
    int south = 0;
    int east = 0;
    int west = 0;
    int notFall = 0;


    for (int i = 0; i < n; i++)
    {
      for (int j = 0; j < n; j++)
      {
        for (String str : results[i][j])
        {
          switch (str)
          {
            case "North": north++; break;
            case "South": south++; break;
            case "East": east++; break;
            case "West": west++; break;
            case "NorthWest": north++; west++; break;
            case "NorthEast": north++; east++; break;
            case "SouthWest": south++; west++; break;
            case "SouthEast": south++; east++; break;
            default:
              notFall++;
          }
        }
        writeProbResult(i, j, north, south, east, west, notFall);
        writeExpectedResult(i, j);
        north = 0;
        south = 0;
        east = 0;
        west = 0;
        notFall = 0;
      }
    }
  }

  private void writeExpectedResult(int i, int j)
  {
    int fellTotal = stepList[i][j].size();
    int totalSteps = 0;
    int expectedSteps = 0;

    if(!stepList[i][j].isEmpty())
    {
      for (int count : stepList[i][j])
      {
        totalSteps += count;
      }
      expectedSteps = (int) Math.ceil(totalSteps / fellTotal);
    }
    try
    {
      bufferedWriter2.write(i+"");
      bufferedWriter2.append(',');
      bufferedWriter2.write(j+"");
      bufferedWriter2.append(',');
      bufferedWriter2.write(totalSteps+"");
      bufferedWriter2.append(',');
      bufferedWriter2.write(fellTotal+"");
      bufferedWriter2.append(',');
      bufferedWriter2.write(expectedSteps+"");
      bufferedWriter2.append(',');
      bufferedWriter2.newLine();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }


  private void writeProbResult(int row, int col, int n, int s,
                           int ea, int w, int not)
  {
    int total = n + s + ea + w + not;
    float probN = (float)n/(float)total;
    float probS = (float)s/(float)total;
    float probE = (float)ea/(float)total;
    float probW = (float)w/(float)total;
    float probNot = (float)not/(float)total;

    try
    {
      bufferedWriter.write(""+row);
      bufferedWriter.append(',');
      bufferedWriter.write(col+"");
      bufferedWriter.append(',');
      bufferedWriter.append(""+df.format(probN));
      bufferedWriter.append(',');
      bufferedWriter.write(""+ df.format(probS));
      bufferedWriter.append(',');
      bufferedWriter.write(""+df.format(probE));
      bufferedWriter.append(',');
      bufferedWriter.write(""+df.format(probW));
      bufferedWriter.append(',');
      bufferedWriter.write(""+df.format(probNot));
      bufferedWriter.newLine();

    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void initHeaders()
  {
    try
    {
      writer = new FileWriter("EdgeProbability_1.csv");
      writer2 = new FileWriter("ExpectedSteps.csv");
      writer3 = new FileWriter("WhichSquare.csv");
    } catch (IOException e)
    {
      e.printStackTrace();
    }
    bufferedWriter = new BufferedWriter(writer);
    bufferedWriter2 = new BufferedWriter(writer2);
    bufferedWriter3 = new BufferedWriter(writer3);

    writeProbHeader();
    writeExpHeader();
    writeSquareHeader();
  }

  private void initBoard(int n)
  {
    results = new ArrayList[n][n];
    stepList = new ArrayList[n][n];

    for(int i = 0 ; i < results.length; i++)
      for(int j = 0; j < results[i].length; j++)
      {
        results[i][j] = new ArrayList<>();
        stepList[i][j] = new ArrayList<>();
      }

    for(int row = 0; row < n; row++)
    {
      for (int col = 0; col < n; col++)
      {
        for (int trial = 0; trial < totalTrials; trial++)
        {
          start(n, row, col);
        }
      }
    }
    readResults(n);
  }
  private void close()
  {
    try
    {
      bufferedWriter.close();
      bufferedWriter2.close();
      bufferedWriter3.close();
    } catch (IOException e)
    {
      e.printStackTrace();
    }

  }

  private void whichSquare(int n)
  {
    flag = true;
    for(int i = 0; i < totalTrials*5; i++)
    {
      start(8, 2, 4);
    }
    getSquares(8);
    close();
  }

  public static void main(String args[])
  {
    Main game = new Main();
    game.initHeaders();
    game.initBoard(25);
    game.whichSquare(8);
  }
}
