//Mayukh Banik CSE 214 R03 HW 7 114489797 mayukh.banik@stonybroo.edu
import java.util.*;
public class IslandNetwork
{
    public static Scanner input = new Scanner(System.in);
    public static String[] cities, roads;
    public static String[][] roadParts;
    public static HashMap<String, City> thisGraph = new HashMap<>();
    public static String[] menu = {"Menu:", "D) Destinations reachable", "F) Maximum Flow", "Q) Quit"};

    /**
     * cycles between the methods
     * @param args nothing
     */
    public static void main(String[] args)
    {
        System.out.println("Welcome to the Island Designer, because, when you're trying to stay above water, " +
                "Seas get degrees!");
        while (true)
        {
            try
            {
                System.out.println("Please enter an url: ");
                IslandDesigner.loadFromFile("https://www.cs.stonybrook.edu/~cse214/hw/hw7-images/hw7.xml");
                break;
            }
            catch (Exception e)
            {
                System.out.println("The url didn't work.");
            }
        }
        System.out.println("Map loaded. ");
        Arrays.sort(cities, String.CASE_INSENSITIVE_ORDER);
        System.out.println("Cities\n---------------------------");
        stringArrayPrint(cities);
        System.out.format("Road" + "%40s", "Capacity\n");
        System.out.println("-------------------------------------------");
        for (int counter = 0; counter < roads.length; counter++)
        {
            System.out.format(roadParts[counter][0] + " to " + roadParts[counter][1] + "%10s", roadParts[counter][2]);
            System.out.println();
        }
        while (true)
        {
            try
            {
                stringArrayPrint(menu);
                String inputString = input.nextLine().toUpperCase();
                switch (inputString)
                {
                    case "D":
                        D();
                        break;
                    case "F":
                        F();
                        break;
                    case "Q":
                        System.out.println("You can go your own way! Goodbye!");
                        System.exit(0);
                    default:
                        System.out.println("Enter a correct input. ");
                        break;
                }
            }
            catch (Exception e)
            {
                System.out.println("There was an exception. ");
            }

        }
    }

    /**
     * sees what cities are traversable
     * @throws Exception exception generally
     */
    public static void D() throws Exception
    {
        IslandDesigner islandDesigner = new IslandDesigner();
        islandDesigner.setGraph(thisGraph);
        System.out.println("Enter a starting city: ");
        System.out.println(islandDesigner.dfs(input.nextLine()));
        System.out.println("The above lines are the destinations reachable. If two brackets ([]) are the only" +
                "thing above, then there is no visitable cities.");
    }

    /**
     * sees how to traverse between two cities
     * @throws Exception generally
     */
    public static void F() throws Exception
    {
        IslandDesigner islandDesigner = new IslandDesigner();
        islandDesigner.setGraph(thisGraph);
        String[] fromAndTo = new String[2];
        System.out.println("Enter the starting city: ");
        fromAndTo[0] = input.nextLine();
        System.out.println("Enter the destination: ");
        fromAndTo[1] = input.nextLine();
        islandDesigner.maxFlow(fromAndTo[0], fromAndTo[1]);
    }

    /**
     * prints out every array of strings
     * @param strings array of strings to be printed
     */
    public static void stringArrayPrint(String[] strings)
    {
        for (String string : strings)
        {
            System.out.println(string);
        }
    }
}