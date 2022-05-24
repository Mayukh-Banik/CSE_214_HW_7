//Mayukh Banik CSE 214 R03 HW 7 114489797 mayukh.banik@stonybroo.edu
import java.util.*;
import big.data.*;
public class IslandDesigner
{
    private HashMap<String, City> graph = new HashMap<>();
    private static final HashMap<String, City> staticGraph = new HashMap<>();
    private Set<String> allTownsVisited = new LinkedHashSet<>();
    /**
     * loads from the url provided
     * @param url the online destination
     * @return a type of island network
     */
    public static IslandNetwork loadFromFile(String url)
    {
        DataSource dataSource = DataSource.connectXML(url);
        dataSource.load();
        IslandNetwork.cities = dataSource.fetchString("cities").substring( 1 , dataSource.fetchString("cities")
                        .length() -1 ).replace("\"","").split(",");
        IslandNetwork.roads = dataSource.fetchString("roads").substring( 1, dataSource.fetchString("roads")
                        .length() - 1).split("\",\"");
        for (int counter = 0; counter < IslandNetwork.cities.length; counter++)
        {
            IslandNetwork.cities[counter] = IslandNetwork.cities[counter].replace("\"", "");
        }
        IslandNetwork.roadParts = new String[IslandNetwork.roads.length][3];
        for (int counter = 0; counter < IslandNetwork.roads.length; counter++)
        {
            IslandNetwork.roads[counter] = IslandNetwork.roads[counter].replace("\"", "");
            IslandNetwork.roadParts[counter] = IslandNetwork.roads[counter].split(",");
        }
        for (String string : IslandNetwork.cities)
        {
            HashMap<String, Integer> tempNeighborHashMap = new HashMap<>();
            City tempCity = new City();
            tempCity.setName(string);
            for (int counter = 0; counter < IslandNetwork.roadParts.length; counter++)
            {
                if (IslandNetwork.roadParts[counter][0].equalsIgnoreCase(string))
                {
                    tempNeighborHashMap.put(IslandNetwork.roadParts[counter][1], Integer.parseInt(IslandNetwork.roadParts[counter][2]));
                }
            }
            tempCity.setNeighbors(tempNeighborHashMap);
            staticGraph.put(string, tempCity);
        }
        IslandNetwork.thisGraph = staticGraph;
        return null;
    }

    /**
     * sees the routes available between the destinations that don't overlap and the max capacity
     * @param from the starting city
     * @param to the ending city
     * @throws Exception in case there's an error
     */
    public void maxFlow(String from, String to) throws Exception
    {
        boolean t = graph.get(from).getNeighbors().containsKey(to);
        if (t)
        {
            System.out.println("Route: " + from + "->" + to + ": " + graph.get(from).getNeighbors().get(to));
            return;
        }
        if (graph.get(from).getNeighbors() == null)
        {
            System.out.println("There are no neighbors to go to. ");
            throw new Exception();
        }
        truncateGraph(from, to);
        ArrayList<String> strings = new ArrayList<>(graph.get(from).getNeighbors().keySet());
        ArrayList<ArrayList<String>> matrix = new ArrayList<>();
        for (int counter = strings.size() - 1; counter >= 0; counter--)
        {
            matrix.add(recursionHandler(strings.get(counter), to));
        }
        ordersAndPrintsFlowAlongWithValues(matrix, from, to);
    }

    /**
     * takes in the matrix with all values and orders them for printing along
     * @param arrayLists matrix of all values
     * @param from from
     * @param to to
     */
    public void ordersAndPrintsFlowAlongWithValues(ArrayList<ArrayList<String>> arrayLists, String from, String to)
    {
        for (ArrayList<String> strings : arrayLists)
        {
            Collections.reverse(strings);
            strings.add(0, from);
            strings.add(to);
            System.out.print("Route: ");
            int x = 100;
            for (int counter = 0; counter < strings.size(); counter++)
            {
                System.out.print(strings.get(counter) + "->");
                if (counter < strings.size() - 1)
                {
                    if (graph.get(strings.get(counter)).getNeighbors().get(strings.get(counter + 1)) < x)
                    {
                        x = graph.get(strings.get(counter)).getNeighbors().get(strings.get(counter + 1));
                    }
                }
            }
            System.out.println("End of trip. Value: " + x);
        }
    }

    /**
     * handles recursive function of maxFlow cause otherwise im losing my mind
     * @param from init string
     * @param to end string
     * @return arraylist of strings
     */
    public ArrayList<String> recursionHandler(String from, String to)
    {
        boolean x = graph.get(from).getNeighbors().containsKey(to);
        boolean y = graph.get(from).getNeighbors() == null;
        if (y && from.equalsIgnoreCase(to))
        {
            ArrayList<String> e = new ArrayList<>();
            e.add(to);
            return e;
        }
        if (x)
        {
            ArrayList<String> te = new ArrayList<>();
            te.add(from);
            graph.get(from).setVisited(true);
            return te;
        }
        ArrayList<String> temp = new ArrayList<>(graph.get(from).getNeighbors().keySet());
        temp.removeIf(string -> graph.get(string) == null);
        ArrayList<String> temp2 = new ArrayList<>();
        int counter = 0;
        while (!areAllNeighborsVisited(from))
        {
            if (!graph.get(temp.get(counter)).getVisited())
            {
                graph.get(temp.get(counter)).setVisited(true);
                temp2.addAll(recursionHandler(temp.get(counter), to));
                graph.get(from).setVisited(true);
                temp2.add(from);
            }
            counter++;
        }
        return temp2;
    }

    /**
     * determines all possible traversable cities
     * @param from the starting city
     * @return all traversable cities
     * @throws Exception any exceptions
     */
    public List<String> dfs(String from) throws Exception
    {
        try
        {
            Set<String> listOfAllNeighbors = new LinkedHashSet<>(graph.get(from).getNeighbors().keySet());
            if (graph.get(from).getNeighbors() == null)
            {
                return null;
            }
            else
            {
                for (String string : listOfAllNeighbors)
                {
                    dfs(string);
                    allTownsVisited.addAll(listOfAllNeighbors);
                }
            }
        }
        catch (Exception e)
        {
            throw new Exception();
        }
        return new ArrayList<>(allTownsVisited);
    }

    /**
     * sees whether a specific city is reachable
     * @param from city to start from
     * @param to city to end from
     * @return boolean as to whether it is traversable
     * @throws Exception exception
     */
    private boolean isItContained(String from, String to) throws Exception
    {
        allTownsVisited = new LinkedHashSet<>();
        return dfs(from).contains(to);
    }

    /**
     * sees if all neighbors have been visited
     * @param string to check if they have been visited
     * @return boolean on whether they have or not
     */
    private boolean areAllNeighborsVisited(String string)
    {
        boolean pass = true;
        for (String strings : new ArrayList<>(graph.get(string).getNeighbors().keySet()))
        {
            if (graph.get(strings) == null)
            {
                continue;
            }
            if (!graph.get(strings).getVisited())
            {
                pass = false;
            }
        }
        return pass;
    }

    /**
     * sets the graph
     * @param graph to be set
     */
    public void setGraph(HashMap<String, City> graph)
    {
        this.graph = graph;
    }

    /**
     * deletes loads of stuff from the graph to make it easier
     */
    public void truncateGraph(String from, String to) throws Exception
    {
        List<String> list = new ArrayList<>(graph.keySet());
        List<String> tempList1 = dfs(from);
        tempList1.add(from);
        List<String> unions = new ArrayList<>(list);
        unions.removeAll(tempList1);
        unions.forEach(graph.keySet()::remove);
        for (String string : graph.keySet())
        {
            if (!isItContained(string, to))
            {
                unions.add(string);
            }
        }
        unions.forEach(graph.keySet()::remove);
    }
}