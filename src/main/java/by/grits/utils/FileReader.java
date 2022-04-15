package by.grits.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReader {
  public List<Integer> getCarsTypes(String file) throws FileNotFoundException {
    Scanner scanner = new Scanner(new File(file));
    List<Integer> sizes = new ArrayList<>();
    while (scanner.hasNextInt()) {
      sizes.add(scanner.nextInt());
    }
    return sizes;
  }
}
