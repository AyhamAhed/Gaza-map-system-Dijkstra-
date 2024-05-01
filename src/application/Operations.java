package application;



import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Operations {
    final static int constValueForDraw = 500;
    static int numOfClicks = 0;

    public static Graph readFile(File file, Pane pane, ComboBox<String> comboBoxCitiesNamesSrc, ComboBox<String> comboBoxCitiesNamesDest) throws Exception {

        try {
            Scanner scanner = new Scanner(file);

            String[] arrLengths = scanner.nextLine().trim().split(" ");


            int numOfCities = Integer.parseInt(arrLengths[0]); // means the Vertices
            int numOfEdges = Integer.parseInt(arrLengths[1]); // means the adjacent (edges)

            Graph graph = new Graph(numOfCities);
            int i = 0;
            while (i < numOfCities) {

                String line = scanner.nextLine().trim();
                String[] arr = line.split(" ");
                City city = new City(arr[0], Double.parseDouble(arr[2]), Double.parseDouble(arr[1]), scaleX(Double.parseDouble(arr[2])), scaleY(Double.parseDouble(arr[1])));
                Vertex vi = new Vertex(city, i);
                graph.insertVertex(vi);
                i++;
                
                char checkChar = arr[0].charAt(0);
                if(Character.isUpperCase(checkChar)) {
                HelloApplication.addCircleToPane(city, pane);
                comboBoxCitiesNamesSrc.getItems().add(city.getName());
                comboBoxCitiesNamesDest.getItems().add(city.getName());
                }

            }


            i = 0;
            while (i < numOfEdges) {
                String[] arr = scanner.nextLine().trim().split(" ");

                // Graph.Edge edge = new Graph.Edge();
                City citySrc = new City();
                citySrc.setName(arr[0]);

                City cityDest = new City();
                cityDest.setName(arr[1]);

                Vertex srcVertex = new Vertex();
                Vertex destVertex = new Vertex();

                ArrayList<Vertex> arrList = graph.getArrayList();
                boolean foundDest = false;
                boolean foundSrc = false;

                for (int j = 0; j < arrList.size(); j++) {
                    if (!foundDest) {
                        if (cityDest.equals(arrList.get(j).getCity())) {
                            destVertex = arrList.get(j);
                            foundDest = true;
                        }
                    }
                    if (!foundSrc) {
                        if (citySrc.equals(arrList.get(j).getCity())) {
                            srcVertex = arrList.get(j);
                            foundSrc = true;
                        }
                    }
                    //if (foundDest && foundSrc)
                    //  break;

                }
                if (!foundSrc || !foundDest) {
                    System.out.println("no:" + i);
                    // throw new NoSuchFieldException();
                    throw new IllegalArgumentException();
                }

               // double xSrc = srcVertex.getCity().getLongitude();
                //double ySrc = srcVertex.getCity().getLatitude();

                //double xDest = destVertex.getCity().getLongitude();
                //double yDest = destVertex.getCity().getLatitude();

                // double weight = Math.sqrt(Math.pow((xSrc - xDest), 2) + Math.pow((ySrc - yDest), 2));


                double weight = getDistanceFromLatLonInKm(srcVertex.getCity().getLatitude(),
                        srcVertex.getCity().getLongitude(), destVertex.getCity().getLatitude(), destVertex.getCity().getLongitude());
                graph.addEdge(srcVertex, destVertex, weight);

                i++;

            }
            return graph;

        } catch (Exception e) {
            System.out.println(e);

        }
        return null;

    }

    
    
    public static double scaleY(double latitude) {
    	double MIN_LATITUDE = 31.216038;
    	double MAX_LATITUDE = 31.613611;
//        double latitudeRange = MAX_LATITUDE - MIN_LATITUDE;
//        double scaleY = 490 / latitudeRange;
        return (MAX_LATITUDE - latitude) / (MAX_LATITUDE - MIN_LATITUDE ) * 380;//كل ما يزيد بروح ل y-
//        return -scaleY * (latitude - MAX_LATITUDE);
    }
    
//    private static double scaleY(double latitude) {
//    	double maxLatitude = 31.206586940790557;
//    	double minLatitude = 31.60599969119915;
//    	 return (-712.0 / (maxLatitude - minLatitude)) * latitude + 712.0 * (maxLatitude / (maxLatitude - minLatitude));
//
////    	return (maxlong - latitude) / (maxlong - minlong)*490 ;
//    	
////        return -178.59 * latitude + 5950; //i am here
//        //return -178.69 * v.getCity().getLatitude() + 5950;
//
//    }
    public static double scaleX(double longitude) {
    	double MIN_LONGITUDE = 34.161113;
    	double MAX_LONGITUDE = 34.568197;
    	return (longitude - MIN_LONGITUDE ) / (MAX_LONGITUDE - MIN_LONGITUDE) * 575;//كل ما يزيد بروح لليمين x+
//        double longitudeRange = MAX_LONGITUDE - MIN_LONGITUDE;
//        double scaleX = 630 / longitudeRange;
//        return scaleX * (longitude - MIN_LONGITUDE);
    }

//    private static double scaleX(double longitude) {
//    	double maxLongitude = 34.179100191012514;
//    	double minLongitude = 34.57869220377377;
//    	 return (570.0 / (maxLongitude - minLongitude)) * longitude - 570.0 * (minLongitude / (maxLongitude - minLongitude));
//
////    	return (longitude - minlong) / (maxlong - minlong)*630;
//    	
////        return 331.77932 * longitude - 11292.2733;//i am here
//        //return 330.77932 * v.getCity().getLongitude() - 11292.2733;
//
//    }

    private static double getDistanceFromLatLonInKm(double lat1, double lon1, double lat2, double lon2) {
        int R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2 - lat1);
        double dLon = deg2rad(lon2 - lon1);
        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                                Math.sin(dLon / 2) * Math.sin(dLon / 2);//Haversine formula's 
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));// angular distance
        double ditance = R * c; // Distance in km
        return ditance;
    }

    private static double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }
}



