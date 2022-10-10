import java.util.*;
import java.util.List;

public class ConnectComponents {
  static final int NPASS = 11;
  private static int UID = 0;

  static void preparation(int[][] sliceArray, int width, int height) {
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int ptr = y * width + x;
        sliceArray[0][ptr] = (sliceArray[0][ptr] == 0) ? -1 : ptr;
      }
    }
  }

  static int CCLSub(
    int[][] sliceArray,
    int pass,
    int x0,
    int y0,
    int width,
    int height
  ) {
    int g = sliceArray[pass - 1][y0 * width + x0];

    for (int y = -1; y <= 1; y++) {
      if (y + y0 < 0 || y + y0 >= height) continue;
      for (int x = -1; x <= 1; x++) {
        if (x + x0 < 0 || x + x0 >= width) continue;
        int q = (y + y0) * width + x + x0;
        if (sliceArray[pass - 1][q] != -1 && sliceArray[pass - 1][q] < g) g =
          sliceArray[pass - 1][q];
      }
    }

    return g;
  }

  static void propagation(int[][] sliceArray, int pass, int width, int height) {
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int ptr = y * width + x;

        sliceArray[pass][ptr] = sliceArray[pass - 1][ptr];

        int h = sliceArray[pass - 1][ptr];
        int g = CCLSub(sliceArray, pass, x, y, width, height);

        if (g != -1) {
          for (int i = 0; i < 6; i++) g = sliceArray[pass - 1][g];

          sliceArray[pass][h] =
            sliceArray[pass][h] < g ? sliceArray[pass][h] : g; // !! Atomic, referring result of current pass
          sliceArray[pass][ptr] =
            sliceArray[pass][ptr] < g ? sliceArray[pass][ptr] : g; // !! Atomic
        }
      }
    }
  }

  static void labelSlice(int[][] sliceArray, int width, int height) {
    preparation(sliceArray, width, height);

    for (int pass = 1; pass < NPASS; pass++) {
      propagation(sliceArray, pass, width, height);
    }
  }

  public HashMap<Integer, FractureObject> fractureDetect(
    int[][] sliceArray,
    int width,
    int height,
    int filenumber
  ) {
    labelSlice(sliceArray, width, height); // call the labelSlice method to labe connected component of the label
    Set<Integer> set = new HashSet<Integer>();
    List<Integer> list = new ArrayList<Integer>();
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (!set.add(sliceArray[NPASS - 1][y * width + x])) {
          list.add(sliceArray[NPASS - 1][y * width + x]);
        }
      }
    }
    List<Integer> newList = new ArrayList<>(set);

    HashMap<Integer, FractureObject> map = new HashMap<>();

    for (int i = 0; i < newList.size(); i++) {
      ArrayList<PVector> fracturesList = new ArrayList<PVector>();
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          if (
            sliceArray[NPASS - 1][y * width + x] != -1 ||
            sliceArray[NPASS - 1][y * width + x] != 0
          ) {
            if (sliceArray[NPASS - 1][y * width + x] == newList.get(i)) {
              fracturesList.add(new PVector(x, y, filenumber));
            }
          }
        }
      }
      map.put(i, new FractureObject(UID, fracturesList));
      UID++; // increment the unique id of the fracture object
    }
    return map;
  }

  public HashMap<Integer, ArrayList<PVector>> ConnectedComponent(
    ArrayList<PVector> points,
    int sideLength
  ) {
    HashMap<Integer, ArrayList<PVector>> Fractures = new HashMap<Integer, ArrayList<PVector>>();
    int zValue = points.get(0).getZ(); // Records the zValue of the image we're on i.e., the image number
    int width = sideLength, height = sideLength;
    int[][] newArray = new int[width][height];
    Arrays.stream(newArray).forEach(a -> Arrays.fill(a, 0));
    // update each point on the array with a new given point
    for (PVector eachPoint : points) {
      newArray[eachPoint.getX()][eachPoint.getY()] = 1;
    }

    int[][] sliceArray = new int[NPASS][width * height];
    // new array of of point to be passed on the algorithm
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        sliceArray[0][y * width + x] = newArray[x][y];
      }
    }

    labelSlice(sliceArray, width, height);
    // create a new set which groups unique elements
    Set<Integer> set = new HashSet<Integer>();

    List<Integer> list = new ArrayList<Integer>();
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        // add all the points of unique elements in the lists
        if (!set.add(sliceArray[NPASS - 1][y * width + x])) {
          list.add(sliceArray[NPASS - 1][y * width + x]);
        }
      }
    }

    List<Integer> newList = new ArrayList<>(set);

    for (int i = 1; i < newList.size(); i++) {
      // FractureObject fractures = new FractureObject(i, null);
      ArrayList<PVector> fracturesList = new ArrayList<PVector>();
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          // store each fracture pixel in an arraylist of fractures
          if (
            sliceArray[NPASS - 1][y * width + x] != -1 ||
            sliceArray[NPASS - 1][y * width + x] != 0
          ) {
            if (sliceArray[NPASS - 1][y * width + x] == newList.get(i)) {
              fracturesList.add(new PVector(x, y, zValue));
            }
          }
        }
      }
      Fractures.put(i - 1, fracturesList);
    }

    return Fractures;
  }

  public static boolean checkProximity(PVector p1, PVector p2) {
    // checking if the two
    // objects are close to each other values
    return (
      (Math.abs(p1.getX() - p2.getX()) < 2) &&
      (Math.abs(p1.getY() - p2.getY()) < 2)
    );
  }
}
