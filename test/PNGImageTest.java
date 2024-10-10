import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;

import javax.imageio.ImageIO;

import controller.Controller;
import model.ImageModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

/**
 * The `PNGImageTest` class contains JUnit tests for the `Model.PNGImage` class.
 */
public class PNGImageTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private static ImageModel pngJpgImage;
  private static String imageName = "output";
  private static String imagePath = "output.png";

  private static String image2Name = "output2";
  private static String image2Path = "output2.png";

  private static String image3Name = "output3";
  private static String image3Path = "output3.png";
  private Reader reader;


  int[][][] rgbMatrix = {
          {{255, 0, 0}, {0, 255, 0}},
          {{0, 0, 255}, {255, 255, 255}}
  };
  int[][][] rgbMatrix3 = {
          {{255, 200, 80}, {100, 255, 150}, {80, 90, 255}},
          {{255, 150, 150}, {100, 255, 80}, {110, 110, 255}},
          {{190, 170, 255}, {255, 255, 255}, {255, 255, 255}}
  };

  int[][][] rgbMatrix2 = {
          {{128, 64, 192}, {255, 128, 32}, {64, 192, 128}, {32, 96, 255}, {192, 64, 128},
                  {160, 32, 192}, {96, 255, 128}, {224, 64, 32}, {128, 160, 160}, {192, 128, 96}},
          {{96, 224, 160}, {128, 192, 64}, {160, 255, 32}, {224, 64, 160}, {255, 128, 96},
                  {32, 192, 96}, {128, 64, 224}, {160, 32, 128}, {96, 160, 64}, {160, 224, 255}},
          {{32, 192, 96}, {128, 64, 224}, {160, 32, 128}, {96, 160, 64}, {160, 224, 255},
                  {128, 64, 192}, {255, 128, 32}, {64, 192, 128}, {32, 96, 255}, {192, 64, 128}},
          {{160, 32, 192}, {96, 255, 128}, {224, 64, 32}, {128, 160, 160}, {192, 128, 96},
                  {96, 224, 160}, {128, 192, 64}, {160, 255, 32}, {224, 64, 160}, {255, 128, 96}},
          {{64, 128, 96}, {255, 160, 192}, {96, 96, 128}, {192, 32, 128}, {32, 192, 255},
                  {128, 64, 192}, {255, 128, 32}, {64, 192, 128}, {32, 96, 255}, {192, 64, 128}},
          {{192, 64, 128}, {160, 32, 192}, {96, 255, 128}, {224, 64, 32}, {128, 160, 160},
                  {32, 192, 96}, {128, 64, 224}, {160, 32, 128}, {96, 160, 64}, {160, 224, 255}},
          {{96, 255, 128}, {224, 64, 32}, {128, 160, 160}, {192, 128, 96}, {96, 224, 160},
                  {128, 192, 64}, {160, 255, 32}, {224, 64, 160}, {255, 128, 96}, {32, 192, 96}},
          {{224, 64, 32}, {128, 160, 160}, {192, 128, 96}, {96, 224, 160}, {128, 192, 64},
                  {160, 255, 32}, {224, 64, 160}, {255, 128, 96}, {32, 192, 96}, {128, 64, 224}},
          {{128, 160, 160}, {192, 128, 96}, {96, 224, 160}, {128, 192, 64}, {160, 255, 32},
                  {224, 64, 160}, {255, 128, 96}, {32, 192, 96}, {128, 64, 224}, {160, 32, 128}},
          {{192, 128, 96}, {96, 224, 160}, {128, 192, 64}, {160, 255, 32}, {224, 64, 160},
                  {255, 128, 96}, {32, 192, 96}, {128, 64, 224}, {160, 32, 128}, {96, 255, 128}}
  };

  /**
   * Sets up the test fixture. Called before every test case method.
   */
  @Before
  public void setUp() {
    pngJpgImage = new ImageModel();
    createAndSavePNG(rgbMatrix, imageName, imagePath);
    createAndSavePNG(rgbMatrix2, image2Name, image2Path);
    createAndSavePNG(rgbMatrix3, image3Name, image3Path);
    System.setOut(new PrintStream(outContent));
  }

  /**
   * Tests the `loadImage` method by loading an image and printing the RGB data.
   */
  @Test
  public void testLoadImageAndPrintRGB() {

    // Load the image
    pngJpgImage.loadImageInMap(imagePath, rgbMatrix);

    // Check if the image was loaded successfully
    assertTrue(pngJpgImage.getImageMap().containsKey(imageName));

    // Get the RGB data
    int[][][] rgbData = pngJpgImage.getRgbDataMap(imageName);


    // Print the RGB data
    for (int y = 0; y < rgbData.length; y++) {
      for (int x = 0; x < rgbData[y].length; x++) {
        int r = rgbData[y][x][0];
        int g = rgbData[y][x][1];
        int b = rgbData[y][x][2];
        System.out.println("RGB at (" + x + ", " + y + "): R=" + r + " G=" + g + " B=" + b);
        assertEquals(rgbMatrix[y][x][0], r);
        assertEquals(rgbMatrix[y][x][1], g);
        assertEquals(rgbMatrix[y][x][2], b);
      }
    }
  }

  /**
   * Creates a PNG image with the specified RGB data and saves it to a file at the specified file
   * path.
   *
   * @param matrix   The RGB data of the image.
   * @param fileName The name of the image file.
   * @param filePath The path where the image should be saved.
   */
  private static void createAndSavePNG(int[][][] matrix, String fileName, String filePath) {

    int width = matrix[0].length;
    int height = matrix.length;

    // Create a BufferedImage with the specified width and height
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int r = matrix[y][x][0];
        int g = matrix[y][x][1];
        int b = matrix[y][x][2];

        // Create an RGB color from the values
        int rgb = (r << 16) | (g << 8) | b;

        // Set the pixel color in the BufferedImage
        image.setRGB(x, y, rgb);
      }
    }

    try {
      // Save the BufferedImage as a PNG image at the specified file path
      File output = new File(filePath);
      ImageIO.write(image, "png", output);
      System.out.println("Image saved as " + filePath);
      pngJpgImage.loadImageInMap(filePath, matrix);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Failed to save the image as " + filePath);
    }
  }


  @Test
  public void testVerticalFlipImage() throws IOException {

    // Perform vertical-flip on the image
    pngJpgImage.verticalFlipImage(imageName, "vertical-flip-img");

    // Get the flipped image data
    int[][][] flippedImageData = pngJpgImage.getRgbDataMap("vertical-flip-img");

    // Check if the flipped image matches the expected result
    int[][][] expectedFlippedImageData = new int[2][2][3];
    expectedFlippedImageData[0][0] = new int[]{0, 0, 255};
    expectedFlippedImageData[0][1] = new int[]{255, 255, 255};
    expectedFlippedImageData[1][0] = new int[]{255, 0, 0};
    expectedFlippedImageData[1][1] = new int[]{0, 255, 0};

    for (int y = 0; y < expectedFlippedImageData.length; y++) {
      for (int x = 0; x < expectedFlippedImageData[y].length; x++) {
        for (int c = 0; c < 3; c++) {

          assertEquals(expectedFlippedImageData[y][x][c], flippedImageData[y][x][c]);
        }
      }
    }
  }

  @Test
  public void testBrightenImage() throws IOException {


    pngJpgImage.brightenImage(imageName, "brighten-img", 50);

    int[][][] brightenedImageData = pngJpgImage.getRgbDataMap("brighten-img");

    // Check if the brightened image matches the expected result
    // You need to define expected RGB values after brightening with an increment of 50.
    int[][][] expectedBrightenedImageData = new int[2][2][3];
    expectedBrightenedImageData[0][0] = new int[]{255, 50, 50};
    expectedBrightenedImageData[0][1] = new int[]{50, 255, 50};
    expectedBrightenedImageData[1][0] = new int[]{50, 50, 255};
    expectedBrightenedImageData[1][1] = new int[]{255, 255, 255};


    for (int y = 0; y < expectedBrightenedImageData.length; y++) {
      for (int x = 0; x < expectedBrightenedImageData[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          System.out.println("Expected: " + expectedBrightenedImageData[y][x][c] + " Actual: "
                  + brightenedImageData[y][x][c]);
          assertEquals(expectedBrightenedImageData[y][x][c], brightenedImageData[y][x][c]);
        }
      }
    }
  }

  @Test
  public void testDarkenImage() throws IOException {
    // Perform a brightness adjustment
    pngJpgImage.brightenImage(imageName, "darken-img", -50);

    // Get the brightened image data
    int[][][] brightenedImageData = pngJpgImage.getRgbDataMap("darken-img");

    // Check if the brightened image matches the expected result
    // You need to define expected RGB values after brightening with an increment of 50.
    int[][][] expectedBrightenedImageData = new int[2][2][3];
    expectedBrightenedImageData[0][0] = new int[]{205, 0, 0};
    expectedBrightenedImageData[0][1] = new int[]{0, 205, 0};
    expectedBrightenedImageData[1][0] = new int[]{0, 0, 205};
    expectedBrightenedImageData[1][1] = new int[]{205, 205, 205};

    for (int y = 0; y < expectedBrightenedImageData.length; y++) {
      for (int x = 0; x < expectedBrightenedImageData[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          assertEquals(expectedBrightenedImageData[y][x][c], brightenedImageData[y][x][c]);
        }
      }
    }
  }

  @Test
  public void testHorizontalFlipImage() throws IOException {


    // Perform a horizontal flip
    pngJpgImage.horizontalFlipImage(imageName, "horizontal-flip-img");

    // Get the flipped image data
    int[][][] flippedImageData = pngJpgImage.getRgbDataMap("horizontal-flip-img");

    // Check if the flipped image matches the expected result
    int[][][] expectedFlippedImageData = new int[2][2][3];
    expectedFlippedImageData[0][0] = new int[]{0, 255, 0};
    expectedFlippedImageData[0][1] = new int[]{255, 0, 0};
    expectedFlippedImageData[1][0] = new int[]{255, 255, 255};
    expectedFlippedImageData[1][1] = new int[]{0, 0, 255};


    for (int y = 0; y < expectedFlippedImageData.length; y++) {
      for (int x = 0; x < expectedFlippedImageData[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          //System.out.println(flippedImageData[y][x][c]);
          assertEquals(expectedFlippedImageData[y][x][c], flippedImageData[y][x][c]);

        }
      }
    }
  }

  @Test
  public void testHorizontalVerticalFlipImage() throws IOException {

    pngJpgImage.horizontalFlipImage(imageName, "horizontal-flip-img");

    // Perform a horizontal flip
    pngJpgImage.verticalFlipImage("horizontal-flip-img",
            "horizontal-vertical-flip-img");

    // Get the flipped image data
    int[][][] flippedImageData = pngJpgImage.getRgbDataMap("horizontal-vertical-flip-img");

    // Check if the flipped image matches the expected result
    int[][][] expectedFlippedImageData = new int[2][2][3];
    expectedFlippedImageData[0][0] = new int[]{255, 255, 255};
    expectedFlippedImageData[0][1] = new int[]{0, 0, 255};
    expectedFlippedImageData[1][0] = new int[]{0, 255, 0};
    expectedFlippedImageData[1][1] = new int[]{255, 0, 0};


    for (int y = 0; y < expectedFlippedImageData.length; y++) {
      for (int x = 0; x < expectedFlippedImageData[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          //System.out.println(flippedImageData[y][x][c]);
          assertEquals(expectedFlippedImageData[y][x][c], flippedImageData[y][x][c]);

        }
      }
    }
  }

  @Test
  public void testVerticalHorizontalFlipImage() throws IOException {

    pngJpgImage.verticalFlipImage(imageName, "vertical-flip-img");

    // Perform a horizontal flip
    pngJpgImage.horizontalFlipImage("vertical-flip-img",
            "vertical-horizontal-flip-img");

    // Get the flipped image data
    int[][][] flippedImageData = pngJpgImage.getRgbDataMap("vertical-horizontal-flip-img");

    // Check if the flipped image matches the expected result
    int[][][] expectedFlippedImageData = new int[2][2][3];
    expectedFlippedImageData[0][0] = new int[]{255, 255, 255};
    expectedFlippedImageData[0][1] = new int[]{0, 0, 255};
    expectedFlippedImageData[1][0] = new int[]{0, 255, 0};
    expectedFlippedImageData[1][1] = new int[]{255, 0, 0};


    for (int y = 0; y < expectedFlippedImageData.length; y++) {
      for (int x = 0; x < expectedFlippedImageData[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          //System.out.println(flippedImageData[y][x][c]);
          assertEquals(expectedFlippedImageData[y][x][c], flippedImageData[y][x][c]);

        }
      }
    }
  }


  @Test
  public void testSharpenImage() throws IOException {

    // Perform sharpening on the image
    pngJpgImage.sharpenImage(image2Name, "sharp-img", 100);

    // Get the sharpened image data
    int[][][] sharpenedImageData = pngJpgImage.getRgbDataMap("sharp-img");

    // Define the expected RGB values for the sharpened image
    int[][][] expectedSharpenedImageData = {
            {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0},
                    {0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0},
                    {0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}, {180, 75, 40}, {220, 167, 0}, {160, 255, 251}, {163, 136, 180},
                    {195, 175, 0}, {68, 179, 84}, {0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}, {223, 12, 52}, {132, 96, 163}, {116, 148, 239}, {143, 255, 191},
                    {179, 255, 0}, {155, 255, 0}, {0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}, {179, 75, 92}, {196, 31, 83}, {8, 152, 255}, {71, 88, 239},
                    {203, 135, 0}, {68, 155, 104}, {0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}, {191, 135, 112}, {168, 64, 103}, {80, 108, 199}, {0, 220, 163},
                    {136, 12, 188}, {171, 0, 131}, {0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}, {180, 108, 136}, {172, 192, 88}, {56, 255, 80}, {64, 255, 64},
                    {232, 198, 8}, {255, 51, 108}, {0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}, {184, 124, 112}, {56, 255, 160}, {116, 255, 32}, {204, 255, 0},
                    {255, 115, 88}, {255, 132, 68}, {0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0},
                    {0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0},
                    {0, 0, 0}, {0, 0, 0}, {0, 0, 0}}
    };

    for (int y = 0; y < sharpenedImageData.length; y++) {
      for (int x = 0; x < sharpenedImageData[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          //System.out.print(sharpenedImageData[y][x][c]+ " ");
          assertEquals(expectedSharpenedImageData[y][x][c], sharpenedImageData[y][x][c]);
        }
      }
      System.out.println(" ");
    }
  }

  @Test
  public void testBlurImage() {
    // Perform blurring on the image
    pngJpgImage.blurImage(image2Name, "blurred-img", 0);

    // Get the blurred image data
    int[][][] blurredImageData = pngJpgImage.getRgbDataMap("blurred-img");

    // Check if the blurred image matches the expected result
    int[][][] expectedBlurredImageData = {
            {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0},
                    {0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {135, 161, 106}, {143, 151, 103}, {159, 127, 135}, {165, 122, 147},
                    {135, 125, 145}, {135, 113, 138}, {135, 107, 123}, {126, 132, 139}, {0, 0, 0}},
            {{0, 0, 0}, {124, 135, 134}, {152, 117, 108}, {159, 135, 119}, {153, 156, 155},
                    {143, 144, 145}, {147, 139, 110}, {127, 141, 119}, {125, 123, 155}, {0, 0, 0}},
            {{0, 0, 0}, {141, 131, 140}, {157, 109, 114}, {148, 122, 127}, {134, 152, 163},
                    {139, 154, 143}, {151, 171, 88}, {135, 171, 103}, {147, 119, 151}, {0, 0, 0}},
            {{0, 0, 0}, {157, 129, 146}, {157, 119, 124}, {148, 109, 129}, {118, 136, 163},
                    {119, 142, 149}, {147, 139, 110}, {127, 141, 119}, {125, 123, 155}, {0, 0, 0}},
            {{0, 0, 0}, {159, 115, 140}, {153, 131, 124}, {154, 129, 115}, {120, 150, 137},
                    {105, 157, 133}, {141, 123, 126}, {147, 105, 129}, {131, 132, 139}, {0, 0, 0}},
            {{0, 0, 0}, {164, 123, 112}, {158, 141, 118}, {150, 161, 116}, {128, 185, 108},
                    {126, 193, 94}, {165, 149, 106}, {189, 113, 118}, {155, 136, 119}, {0, 0, 0}},
            {{0, 0, 0}, {164, 137, 110}, {150, 160, 122}, {134, 189, 112}, {138, 203, 88},
                    {169, 181, 84}, {197, 143, 102}, {177, 131, 116}, {131, 126, 134}, {0, 0, 0}},
            {{0, 0, 0}, {150, 160, 122}, {134, 189, 112}, {138, 203, 88}, {169, 181, 84},
                    {197, 143, 102}, {177, 131, 116}, {131, 126, 134}, {117, 103, 154}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0},
                    {0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0},
                    {0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0},
                    {0, 0, 0}, {0, 0, 0}, {0, 0, 0}}
    };

    for (int y = 0; y < blurredImageData.length; y++) {
      for (int x = 0; x < blurredImageData[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          //System.out.print(blurredImageData[y][x][c] + " ");
          assertEquals(expectedBlurredImageData[y][x][c], blurredImageData[y][x][c]);
        }
      }
    }
  }


  @Test
  public void testSepiaImage() {

    pngJpgImage.sepiaImage(imageName, "sepia-img", 0);

    // Get the sepia image data
    int[][][] sepiaImageData = pngJpgImage.getRgbDataMap("sepia-img");

    // Define the expected RGB values for the sepia-toned image
    int[][][] expectedSepiaImageData = new int[2][2][3];

    // Define the expected RGB values for the sepia image
    expectedSepiaImageData[0][0] = new int[]{100, 88, 69};
    expectedSepiaImageData[0][1] = new int[]{196, 174, 136};
    expectedSepiaImageData[1][0] = new int[]{48, 42, 33};
    expectedSepiaImageData[1][1] = new int[]{255, 255, 238};

    // Compare the actual sepia-toned image data with the expected result
    for (int y = 0; y < expectedSepiaImageData.length; y++) {
      for (int x = 0; x < expectedSepiaImageData[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          //System.out.println(sepiaImageData[y][x][c]);
          assertEquals(expectedSepiaImageData[y][x][c], sepiaImageData[y][x][c]);
        }
      }
    }
  }

  @Test
  public void testExtractRedComponent() {
    // Define the expected result after extracting the red component
    // Define the expected RGB values for the sepia-toned image
    int[][][] expectedRedComponent = {
            {{255, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {255, 0, 0}}
    };


    // Perform the extraction of the "red" component
    pngJpgImage.extractComponent(imageName, "red-component-img", "red");

    // Get the extracted red component data
    int[][][] extractedRedComponent = pngJpgImage.getRgbDataMap("red-component-img");

    // Check if the extracted red component matches the expected result
    for (int y = 0; y < expectedRedComponent.length; y++) {
      for (int x = 0; x < expectedRedComponent[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          //System.out.println(extractedRedComponent[y][x][c]);
          assertEquals(expectedRedComponent[y][x][c], extractedRedComponent[y][x][c]);
        }
      }
    }
  }

  @Test
  public void testExtractGreenComponent() {
    // Define the expected result after extracting the red component
    // Define the expected RGB values for the sepia-toned image
    int[][][] expectedGreenComponent = {
            {{0, 0, 0}, {0, 255, 0}},
            {{0, 0, 0}, {0, 255, 0}}
    };


    // Perform the extraction of the "red" component
    pngJpgImage.extractComponent(imageName, "green-component-img", "green");

    // Get the extracted red component data
    int[][][] extractedGreenComponent = pngJpgImage.getRgbDataMap("green-component-img");

    // Check if the extracted red component matches the expected result
    for (int y = 0; y < expectedGreenComponent.length; y++) {
      for (int x = 0; x < expectedGreenComponent[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          assertEquals(expectedGreenComponent[y][x][c], extractedGreenComponent[y][x][c]);
        }
      }
    }
  }

  @Test
  public void testExtractBlueComponent() {
    // Define the expected result after extracting the red component
    // Define the expected RGB values for the sepia-toned image
    int[][][] expectedBlueComponent = {
            {{0, 0, 0}, {0, 0, 0}},
            {{0, 0, 255}, {0, 0, 255}}
    };


    // Perform the extraction of the "red" component
    pngJpgImage.extractComponent(imageName, "blue-component-img", "blue");

    // Get the extracted red component data
    int[][][] extractedBlueComponent = pngJpgImage.getRgbDataMap("blue-component-img");

    // Check if the extracted red component matches the expected result
    for (int y = 0; y < expectedBlueComponent.length; y++) {
      for (int x = 0; x < expectedBlueComponent[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          assertEquals(expectedBlueComponent[y][x][c], extractedBlueComponent[y][x][c]);
        }
      }
    }
  }

  @Test
  public void testExtractValueComponent() {
    // Define the expected result after extracting the red component
    // Define the expected RGB values for the sepia-toned image
    int[][][] expectedValueComponent = {
            {{255, 255, 255}, {255, 255, 255}},
            {{255, 255, 255}, {255, 255, 255}}
    };


    // Perform the extraction of the "red" component
    pngJpgImage.extractComponent(imageName, "value-component-img", "value");

    // Get the extracted red component data
    int[][][] extractedValueComponent = pngJpgImage.getRgbDataMap("value-component-img");

    // Check if the extracted red component matches the expected result
    for (int y = 0; y < expectedValueComponent.length; y++) {
      for (int x = 0; x < expectedValueComponent[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          //System.out.println(extractedValueComponent[y][x][c] + " ");
          assertEquals(expectedValueComponent[y][x][c], extractedValueComponent[y][x][c]);
        }
      }
    }
  }

  @Test
  public void testExtractLumaComponent() {
    // Define the expected result after extracting the red component
    // Define the expected RGB values for the sepia-toned image
    int[][][] expectedLumaComponent = {
            {{54, 54, 54}, {182, 182, 182}},
            {{18, 18, 18}, {254, 254, 254}}
    };


    // Perform the extraction of the "red" component
    pngJpgImage.extractComponent(imageName, "luma-component-img", "luma");

    // Get the extracted red component data
    int[][][] extractedLumaComponent = pngJpgImage.getRgbDataMap("luma-component-img");

    // Check if the extracted red component matches the expected result
    for (int y = 0; y < expectedLumaComponent.length; y++) {
      for (int x = 0; x < expectedLumaComponent[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          //System.out.println(extractedLumaComponent[y][x][c] + " ");
          assertEquals(expectedLumaComponent[y][x][c], extractedLumaComponent[y][x][c]);
        }
      }
    }
  }

  @Test
  public void testExtractIntensityComponent() {
    // Define the expected result after extracting the red component
    // Define the expected RGB values for the sepia-toned image
    int[][][] expectedIntensityComponent = {
            {{85, 85, 85}, {85, 85, 85}},
            {{85, 85, 85}, {255, 255, 255}}
    };


    // Perform the extraction of the "red" component
    pngJpgImage.extractComponent(imageName, "intensity-component-img",
            "intensity");

    // Get the extracted red component data
    int[][][] extractedIntensityComponent =
            pngJpgImage.getRgbDataMap("intensity-component-img");

    // Check if the extracted red component matches the expected result
    for (int y = 0; y < expectedIntensityComponent.length; y++) {
      for (int x = 0; x < expectedIntensityComponent[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          //System.out.println(extractedLumaComponent[y][x][c] + " ");
          assertEquals(expectedIntensityComponent[y][x][c], extractedIntensityComponent[y][x][c]);
        }
      }
    }
  }

  @Test
  public void testRGBSplit() {
    // Perform RGB splitting on the image
    pngJpgImage.rgbSplitImage(imageName, "red-component-img",
            "green-component-img", "blue-component-img");

    // Get the split RGB components
    int[][][] extractedRedComponent = pngJpgImage.getRgbDataMap("red-component-img");
    int[][][] extractedGreenComponent = pngJpgImage.getRgbDataMap("green-component-img");
    int[][][] extractedBlueComponent = pngJpgImage.getRgbDataMap("blue-component-img");

    // Check if the split components are not null
    assertNotNull(extractedRedComponent);
    assertNotNull(extractedGreenComponent);
    assertNotNull(extractedBlueComponent);

    // Check if the dimensions match the original image
    assertEquals(rgbMatrix.length, extractedRedComponent.length);
    assertEquals(rgbMatrix[0].length, extractedRedComponent[0].length);

    assertEquals(rgbMatrix.length, extractedGreenComponent.length);
    assertEquals(rgbMatrix[0].length, extractedGreenComponent[0].length);

    assertEquals(rgbMatrix.length, extractedBlueComponent.length);
    assertEquals(rgbMatrix[0].length, extractedBlueComponent[0].length);

    int[][][] expectedRedComponent = {
            {{255, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {255, 0, 0}}
    };

    int[][][] expectedGreenComponent = {
            {{0, 0, 0}, {0, 255, 0}},
            {{0, 0, 0}, {0, 255, 0}}
    };

    int[][][] expectedBlueComponent = {
            {{0, 0, 0}, {0, 0, 0}},
            {{0, 0, 255}, {0, 0, 255}}
    };

    // Compare the extracted components with the original image
    for (int y = 0; y < rgbMatrix.length; y++) {
      for (int x = 0; x < rgbMatrix[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          assertEquals(expectedRedComponent[y][x][c], extractedRedComponent[y][x][c]);
          assertEquals(expectedGreenComponent[y][x][c], extractedGreenComponent[y][x][c]);
          assertEquals(expectedBlueComponent[y][x][c], extractedBlueComponent[y][x][c]);
        }
      }
    }
  }

  @Test
  public void testRGBCombine() {
    // Create separate red, green, and blue components from the original image
    pngJpgImage.extractComponent(imageName, "red-component-img2", "red");
    pngJpgImage.extractComponent(imageName, "green-component-img2", "green");
    pngJpgImage.extractComponent(imageName, "blue-component-img2", "blue");

    // Perform RGB combining on the separated components
    pngJpgImage.combineRGBImages("combined-img", "red-component-img2",
            "green-component-img2", "blue-component-img2");

    // Get the combined RGB data
    int[][][] combinedRGBData = pngJpgImage.getRgbDataMap("combined-img");

    // Check if the combined RGB data is not null
    assertNotNull(combinedRGBData);

    // Check if the dimensions match the original image
    assertEquals(rgbMatrix.length, combinedRGBData.length);
    assertEquals(rgbMatrix[0].length, combinedRGBData[0].length);

    // Compare the combined RGB data with the original image
    for (int y = 0; y < rgbMatrix.length; y++) {
      for (int x = 0; x < rgbMatrix[y].length; x++) {
        for (int c = 0; c < 3; c++) {
          assertEquals(rgbMatrix[y][x][c], combinedRGBData[y][x][c]);
        }
      }
    }
  }

  @Test
  public void testSaveImage() throws IOException {
    // Create a new image content to save
    Controller img = new Controller(reader);
    img.saveImage("save 'output.png' img");

    File savedFile = new File(imagePath);
    assertTrue("Image file should exist after saving.", savedFile.exists());

    // Clean up: Delete the saved file after the test
    savedFile.delete();
    //  assertEquals("expectedErrorMessage", outContent.toString().trim());
  }

  @Test
  public void testExtractComponentWithInvalidComponent() {

    pngJpgImage.extractComponent(imageName, "destName", "invalidComponent");

    String expectedErrorMessage = "Invalid component parameter."
            + "Invalid component parameter.Invalid component parameter."
            + "Invalid component parameter.";

    assertEquals(expectedErrorMessage, outContent.toString().trim());
  }

  @Test
  public void testExtractComponentWithInvalidSourceName() {
    pngJpgImage.extractComponent("nonExistentImage", "destName", "red");


    String expectedErrorMessage = "Source image not found: nonExistentImage";


    assertEquals(expectedErrorMessage, outContent.toString().trim());
  }

  @Test
  public void testCombineRGBImagesWithMismatchedDimensions() {
    pngJpgImage.extractComponent(imageName, "red-component-img2", "red");
    pngJpgImage.extractComponent(imageName, "green-component-img2", "green");
    pngJpgImage.extractComponent(image2Name, "blue-component-img2", "blue");
    pngJpgImage.combineRGBImages("combinedName", "red-component-img2",
            "green-component-img2", "blue-component-img2");
    String expectedErrorMessage = "red component image created from 'output' and saved as "
            + "'red-component-img2'green component image created from 'output' and saved as "
            + "'green-component-img2'blue component image created from 'output2' and saved as "
            + "'blue-component-img2'Source images have different dimensions.RGB channels combined. "
            + "Combined image saved as combinedName";


    assertEquals(expectedErrorMessage, outContent.toString().trim());
  }


  @Test
  public void testRGBSplitImageWithInvalidSourceName() {
    pngJpgImage.rgbSplitImage("ghy", "destNameRed", "destNameGreen", "destNameBlue");

    String expectedErrorMessage = "Source image not found: ghy";


    assertEquals(expectedErrorMessage, outContent.toString().trim());
  }


  @Test
  public void testSaveImageWithInvalidPath() throws IOException {
    String invalidPath = "invalid_path/invalid_file.ppm";
    Controller img = new Controller(reader);
    img.saveImage("save " + invalidPath + " img");

    String expectedErrorMessage = "Error in saving File";


    assertEquals(expectedErrorMessage, outContent.toString().trim());
  }

  @Test
  public void testSaveImageWithInvalidFilename() {
    String invalidFilename = "bac#";
    Controller img = new Controller(reader);
    img.saveImage("save 'output.png' " + invalidFilename);

    String expectedErrorMessage = "Image not found: bac#";


    assertEquals(expectedErrorMessage, outContent.toString().trim());
  }

  @Test
  public void testCompressby90() throws IOException {

    pngJpgImage.compress(image3Name, "compressed-90-img", 90);


    int[][][] compressedImage = pngJpgImage.getRgbDataMap("compressed-90-img");


    int[][][] expectedCompressedImage = {
            {{0, 215, 0}, {0, 215, 0}, {0, 0, 0}},
            {{0, 215, 0}, {0, 215, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}
    };


    for (int y = 0; y < compressedImage.length; y++) {
      for (int x = 0; x < compressedImage[y].length; x++) {
        for (int c = 0; c < 3; c++) {

          assertEquals(expectedCompressedImage[y][x][c], compressedImage[y][x][c]);

        }
      }
    }


  }

  @Test
  public void testCompressby50() throws IOException {


    pngJpgImage.compress(image3Name, "compressed-50-img", 50);

    int[][][] compressedImage = pngJpgImage.getRgbDataMap("compressed-50-img");


    int[][][] expectedCompressedImage = {
            {{255, 215, 115}, {100, 215, 115}, {0, 0, 255}},
            {{255, 215, 115}, {100, 215, 115}, {0, 0, 255}},
            {{222, 212, 255}, {222, 212, 255}, {255, 255, 255}}
    };


    for (int y = 0; y < compressedImage.length; y++) {
      for (int x = 0; x < compressedImage[y].length; x++) {
        for (int c = 0; c < 3; c++) {

          assertEquals(expectedCompressedImage[y][x][c], compressedImage[y][x][c]);

        }
      }
    }


  }

  @Test
  public void testCompressby10() throws IOException {

    pngJpgImage.compress(image3Name, "compressed-10-img", 10);


    int[][][] compressedImage = pngJpgImage.getRgbDataMap("compressed-10-img");


    int[][][] expectedCompressedImage = {
            {{255, 200, 80}, {100, 255, 150}, {95, 100, 255}},
            {{255, 150, 150}, {100, 255, 80}, {95, 100, 255}},
            {{190, 170, 255}, {255, 255, 255}, {255, 255, 255}}
    };


    for (int y = 0; y < compressedImage.length; y++) {
      for (int x = 0; x < compressedImage[y].length; x++) {
        for (int c = 0; c < 3; c++) {

          assertEquals(expectedCompressedImage[y][x][c], compressedImage[y][x][c]);

        }
      }
    }


  }

  @Test
  public void testCompressby0() throws IOException {

    pngJpgImage.compress(image3Name, "compressed-0-img", 0);


    int[][][] compressedImage = pngJpgImage.getRgbDataMap("compressed-0-img");


    int[][][] expectedCompressedImage = {
            {{255, 200, 80}, {100, 255, 150}, {0, 0, 0}},
            {{255, 150, 150}, {100, 255, 80}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}
    };


    for (int y = 0; y < compressedImage.length; y++) {
      for (int x = 0; x < compressedImage[y].length; x++) {
        for (int c = 0; c < 3; c++) {

          assertEquals(expectedCompressedImage[y][x][c], compressedImage[y][x][c]);

        }
      }
    }

  }

  @Test
  public void testCompressbyNegative() throws IOException {

    pngJpgImage.compress(image3Name, "compressed-0-img", -5);
    assertEquals("Compression percentage must be between 0 and 100.\n" +
            "Error in compressing output3 by -5.0 %", outContent.toString().trim());
  }

  @Test
  public void testCompressWithMaxPercentage() throws IOException {
    // Compression with 100% should result in a completely black image
    pngJpgImage.compress(image3Name, "compressed-100-img", 100);

    int[][][] compressedImage = pngJpgImage.getRgbDataMap("compressed-100-img");

    int[][][] expectedCompressedImage = {
            {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}
    };


    for (int y = 0; y < compressedImage.length; y++) {
      for (int x = 0; x < compressedImage[y].length; x++) {
        for (int c = 0; c < 3; c++) {

          assertEquals(expectedCompressedImage[y][x][c], compressedImage[y][x][c]);
        }
      }
    }

  }

  @Test
  public void testCompressWithNonPowerOfTwoDimensions() throws IOException {
    // Test compressing an image with non-power-of-two dimensions
    // For simplicity, you can create a small image with dimensions like 3x3
    int[][][] nonPowerOfTwoImage = {
            {{255, 200, 80}, {100, 255, 150}, {80, 90, 255}},
            {{255, 150, 150}, {100, 255, 80}, {110, 110, 255}},
            {{190, 170, 255}, {255, 255, 255}, {255, 255, 255}}
    };

    createAndSavePNG(nonPowerOfTwoImage, "nonPowerOfTwoImage", "nonPowerOfTwoImage.png");

    // Try compressing the non-power-of-two image with 50% compression
    pngJpgImage.compress("nonPowerOfTwoImage", "compressed-non-power-of-two-img", 50);

    // Verify that the compression was successful and the image dimensions remain the same
    int[][][] compressedImage = pngJpgImage.getRgbDataMap("compressed-non-power-of-two-img");

    assertEquals(nonPowerOfTwoImage.length, compressedImage.length);
    assertEquals(nonPowerOfTwoImage[0].length, compressedImage[0].length);
  }

  @Test
  public void testCompressWithInvalidPercentage() throws IOException {
    // Attempting to compress with an invalid percentage should result in an error message
    pngJpgImage.compress(image3Name, "invalid-percentage-img", 115);
    assertEquals("Compression percentage must be between 0 and 100.\n" +
            "Error in compressing output3 by 115.0 %", outContent.toString().trim());
  }

  @Test
  public void testCompressWithLargeImage() throws IOException {
    // Test compressing a large image to ensure efficiency
    int[][][] largeImage = new int[1000][1000][3];

    createAndSavePNG(largeImage, "largeImage", "largeImage.png");

    // Try compressing the non-power-of-two image with 50% compression
    pngJpgImage.compress("largeImage", "compressedLargeImage", 50);

    int[][][] result = pngJpgImage.getRgbDataMap("compressedLargeImage");

    // Ensure that the dimensions are still correct
    assertEquals(1000, result.length);
    assertEquals(1000, result[0].length);
  }

  @Test
  public void testBlurImageWith50Split() {
    // Perform blurring on the image
    pngJpgImage.blurImage(image2Name, "blurred-img", 50);

    // Get the blurred image data
    int[][][] blurredImageData = pngJpgImage.getRgbDataMap("blurred-img");

    // Check if the blurred image matches the expected result
    int[][][] expectedBlurredImageData = {
            {{145, 114, 148,}, {163, 149, 92,}, {119, 161, 119,}, {113, 115, 171,}, {155, 80, 159,},
                    {160, 32, 192,}, {96, 255, 128,}, {224, 64, 32,}, {128, 160, 160,},
                    {192, 128, 96,},},
            {{105, 168, 138,}, {135, 161, 106,}, {143, 151, 103,}, {159, 127, 135,},
                    {165, 122, 147,}, {32, 192, 96,}, {128, 64, 224,}, {160, 32, 128,},
                    {96, 160, 64,}, {160, 224, 255,},},
            {{90, 155, 142,}, {124, 135, 134,}, {152, 117, 108,}, {159, 135, 119,},
                    {153, 156, 155,}, {128, 64, 192,}, {255, 128, 32,}, {64, 192, 128,},
                    {32, 96, 255,}, {192, 64, 128,},},
            {{113, 117, 150,}, {141, 131, 140,}, {157, 109, 114,}, {148, 122, 127,},
                    {134, 152, 163,}, {96, 224, 160,}, {128, 192, 64,}, {160, 255, 32,},
                    {224, 64, 160,}, {255, 128, 96,},},
            {{137, 103, 140,}, {157, 129, 146,}, {157, 119, 124,}, {148, 109, 129,},
                    {118, 136, 163,}, {128, 64, 192,}, {255, 128, 32,}, {64, 192, 128,},
                    {32, 96, 255,}, {192, 64, 128,},},
            {{151, 113, 128,}, {159, 115, 140,}, {153, 131, 124,}, {154, 129, 115,},
                    {120, 150, 137,}, {32, 192, 96,}, {128, 64, 224,}, {160, 32, 128,},
                    {96, 160, 64,}, {160, 224, 255,},},
            {{160, 139, 104,}, {164, 123, 112,}, {158, 141, 118,}, {150, 161, 116,},
                    {128, 185, 108,}, {128, 192, 64,}, {160, 255, 32,}, {224, 64, 160,},
                    {255, 128, 96,}, {32, 192, 96,},},
            {{168, 133, 94,}, {164, 137, 110,}, {150, 160, 122,}, {134, 189, 112,},
                    {138, 203, 88,}, {160, 255, 32,}, {224, 64, 160,}, {255, 128, 96,},
                    {32, 192, 96,}, {128, 64, 224,},},
            {{164, 136, 116,}, {150, 160, 122,}, {134, 189, 112,}, {138, 203, 88,},
                    {169, 181, 84,}, {224, 64, 160,}, {255, 128, 96,}, {32, 192, 96,},
                    {128, 64, 224,}, {160, 32, 128,},},
            {{162, 152, 120,}, {134, 184, 122,}, {128, 209, 90,}, {158, 197, 74,},
                    {203, 143, 102,}, {255, 128, 96,}, {32, 192, 96,}, {128, 64, 224,},
                    {160, 32, 128,}, {96, 255, 128,},}
    };

    for (int y = 0; y < blurredImageData.length; y++) {
      System.out.print("{");
      for (int x = 0; x < blurredImageData[y].length; x++) {
        System.out.print("{");
        for (int c = 0; c < 3; c++) {
          System.out.print(blurredImageData[y][x][c] + ", ");
          assertEquals(expectedBlurredImageData[y][x][c], blurredImageData[y][x][c]);
        }
        System.out.print("},");
      }
      System.out.println("},");
    }
  }


  @Test
  public void testBlurImageWith0Split() {
    // Perform blurring on the image
    pngJpgImage.blurImage(image2Name, "blurred-img", 0);

    // Get the blurred image data
    int[][][] blurredImageData = pngJpgImage.getRgbDataMap("blurred-img");

    // Check if the blurred image matches the expected result
    int[][][] expectedBlurredImageData = {
            {{145, 114, 148,}, {163, 149, 92,}, {119, 161, 119,}, {113, 115, 171,}, {155, 80, 159,},
                    {141, 107, 152,}, {136, 135, 132,}, {160, 119, 100,}, {158, 132, 115,},
                    {168, 154, 135,},},
            {{105, 168, 138,}, {135, 161, 106,}, {143, 151, 103,}, {159, 127, 135,},
                    {165, 122, 147,}, {135, 125, 145,}, {135, 113, 138,}, {135, 107, 123,},
                    {126, 132, 139,}, {154, 156, 171,},},
            {{90, 155, 142,}, {124, 135, 134,}, {152, 117, 108,}, {159, 135, 119,},
                    {153, 156, 155,}, {143, 144, 145,}, {147, 139, 110,}, {127, 141, 119,},
                    {125, 123, 155,}, {173, 116, 159,},},
            {{113, 117, 150,}, {141, 131, 140,}, {157, 109, 114,}, {148, 122, 127,},
                    {134, 152, 163,}, {139, 154, 143,}, {151, 171, 88,}, {135, 171, 103,},
                    {147, 119, 151,}, {199, 92, 135,},},
            {{137, 103, 140,}, {157, 129, 146,}, {157, 119, 124,}, {148, 109, 129,},
                    {118, 136, 163,}, {119, 142, 149,}, {147, 139, 110,}, {127, 141, 119,},
                    {125, 123, 155,}, {173, 116, 159,},},
            {{151, 113, 128,}, {159, 115, 140,}, {153, 131, 124,}, {154, 129, 115,},
                    {120, 150, 137,}, {105, 157, 133,}, {141, 123, 126,}, {147, 105, 129,},
                    {131, 132, 139,}, {131, 166, 167,},},
            {{160, 139, 104,}, {164, 123, 112,}, {158, 141, 118,}, {150, 161, 116,},
                    {128, 185, 108,}, {126, 193, 94,}, {165, 149, 106,}, {189, 113, 118,},
                    {155, 136, 119,}, {105, 164, 147,},},
            {{168, 133, 94,}, {164, 137, 110,}, {150, 160, 122,}, {134, 189, 112,}, {138, 203, 88,},
                    {169, 181, 84,}, {197, 143, 102,}, {177, 131, 116,}, {131, 126, 134,},
                    {111, 102, 158,},},
            {{164, 136, 116,}, {150, 160, 122,}, {134, 189, 112,}, {138, 203, 88,}, {169, 181, 84,},
                    {197, 143, 102,}, {177, 131, 116,}, {131, 126, 134,}, {117, 103, 154,},
                    {130, 93, 156,},},
            {{162, 152, 120,}, {134, 184, 122,}, {128, 209, 90,}, {158, 197, 74,}, {203, 143, 102,},
                    {197, 127, 112,}, {131, 140, 124,}, {111, 102, 158,}, {130, 93, 156,},
                    {122, 159, 134,}}
    };

    for (int y = 0; y < blurredImageData.length; y++) {
      System.out.print("{");
      for (int x = 0; x < blurredImageData[y].length; x++) {
        System.out.print("{");
        for (int c = 0; c < 3; c++) {
          //System.out.print(blurredImageData[y][x][c] + ", ");
          assertEquals(expectedBlurredImageData[y][x][c], blurredImageData[y][x][c]);
        }
        System.out.print("},");
      }
      System.out.println("},");
    }
    // assertEquals("Compression percentage must be between 0 and 100.\n" +
    //     "Error in compressing output3 by 115.0 %", outContent.toString().trim());
  }

  @Test
  public void testBlurImageWith100Split() {
    // Perform blurring on the image
    pngJpgImage.blurImage(image2Name, "blurred-img", 100);

    // Get the blurred image data
    int[][][] blurredImageData = pngJpgImage.getRgbDataMap("blurred-img");

    // Check if the blurred image matches the expected result
    int[][][] expectedBlurredImageData = {
            {{145, 114, 148,}, {163, 149, 92,}, {119, 161, 119,}, {113, 115, 171,}, {155, 80, 159,},
                    {141, 107, 152,}, {136, 135, 132,}, {160, 119, 100,}, {158, 132, 115,},
                    {168, 154, 135,},},
            {{105, 168, 138,}, {135, 161, 106,}, {143, 151, 103,}, {159, 127, 135,},
                    {165, 122, 147,}, {135, 125, 145,}, {135, 113, 138,}, {135, 107, 123,},
                    {126, 132, 139,}, {154, 156, 171,},},
            {{90, 155, 142,}, {124, 135, 134,}, {152, 117, 108,}, {159, 135, 119,},
                    {153, 156, 155,}, {143, 144, 145,}, {147, 139, 110,}, {127, 141, 119,},
                    {125, 123, 155,}, {173, 116, 159,},},
            {{113, 117, 150,}, {141, 131, 140,}, {157, 109, 114,}, {148, 122, 127,},
                    {134, 152, 163,}, {139, 154, 143,}, {151, 171, 88,}, {135, 171, 103,},
                    {147, 119, 151,}, {199, 92, 135,},},
            {{137, 103, 140,}, {157, 129, 146,}, {157, 119, 124,}, {148, 109, 129,},
                    {118, 136, 163,}, {119, 142, 149,}, {147, 139, 110,}, {127, 141, 119,},
                    {125, 123, 155,}, {173, 116, 159,},},
            {{151, 113, 128,}, {159, 115, 140,}, {153, 131, 124,}, {154, 129, 115,},
                    {120, 150, 137,}, {105, 157, 133,}, {141, 123, 126,}, {147, 105, 129,},
                    {131, 132, 139,}, {131, 166, 167,},},
            {{160, 139, 104,}, {164, 123, 112,}, {158, 141, 118,}, {150, 161, 116,},
                    {128, 185, 108,}, {126, 193, 94,}, {165, 149, 106,}, {189, 113, 118,},
                    {155, 136, 119,}, {105, 164, 147,},},
            {{168, 133, 94,}, {164, 137, 110,}, {150, 160, 122,}, {134, 189, 112,}, {138, 203, 88,},
                    {169, 181, 84,}, {197, 143, 102,}, {177, 131, 116,}, {131, 126, 134,},
                    {111, 102, 158,},},
            {{164, 136, 116,}, {150, 160, 122,}, {134, 189, 112,}, {138, 203, 88,}, {169, 181, 84,},
                    {197, 143, 102,}, {177, 131, 116,}, {131, 126, 134,}, {117, 103, 154,},
                    {130, 93, 156,},},
            {{162, 152, 120,}, {134, 184, 122,}, {128, 209, 90,}, {158, 197, 74,}, {203, 143, 102,},
                    {197, 127, 112,}, {131, 140, 124,}, {111, 102, 158,}, {130, 93, 156,},
                    {122, 159, 134,}}
    };

    for (int y = 0; y < blurredImageData.length; y++) {
      System.out.print("{");
      for (int x = 0; x < blurredImageData[y].length; x++) {
        System.out.print("{");
        for (int c = 0; c < 3; c++) {
          //System.out.print(blurredImageData[y][x][c] + ", ");
          assertEquals(expectedBlurredImageData[y][x][c], blurredImageData[y][x][c]);
        }
        System.out.print("},");
      }
      System.out.println("},");
    }
    // assertEquals("Compression percentage must be between 0 and 100.\n" +
    //     "Error in compressing output3 by 115.0 %", outContent.toString().trim());
  }


  @Test
  public void testSharpenImageWith50Split() throws IOException {

    // Perform sharpening on the image
    pngJpgImage.sharpenImage(image2Name, "sharp-img", 50);

    // Get the sharpened image data
    int[][][] sharpenedImageData = pngJpgImage.getRgbDataMap("sharp-img");

    // Define the expected RGB values for the sharpened image
    int[][][] expectedSharpenedImageData = {
            {{0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,},
                    {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,},
                    {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {180, 75, 40,}, {220, 167, 0,}, {160, 255, 251,},
                    {128, 64, 192,}, {255, 128, 32,}, {64, 192, 128,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {223, 12, 52,}, {132, 96, 163,}, {116, 148, 239,},
                    {96, 224, 160,}, {128, 192, 64,}, {160, 255, 32,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {179, 75, 92,}, {196, 31, 83,}, {8, 152, 255,},
                    {128, 64, 192,}, {255, 128, 32,}, {64, 192, 128,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {191, 135, 112,}, {168, 64, 103,}, {80, 108, 199,},
                    {32, 192, 96,}, {128, 64, 224,}, {160, 32, 128,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {180, 108, 136,}, {172, 192, 88,}, {56, 255, 80,},
                    {128, 192, 64,}, {160, 255, 32,}, {224, 64, 160,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {184, 124, 112,}, {56, 255, 160,}, {116, 255, 32,},
                    {160, 255, 32,}, {224, 64, 160,}, {255, 128, 96,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,},
                    {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,},
                    {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,},}
    };

    for (int y = 0; y < expectedSharpenedImageData.length; y++) {
      System.out.print("{");
      for (int x = 0; x < expectedSharpenedImageData[y].length; x++) {
        System.out.print("{");
        for (int c = 0; c < 3; c++) {
          //System.out.print(sharpenedImageData[y][x][c] + ", ");
          assertEquals(expectedSharpenedImageData[y][x][c], sharpenedImageData[y][x][c]);
        }
        System.out.print("},");
      }
      System.out.println("},");
    }
  }

  @Test
  public void testSharpenImageWith0Split() throws IOException {

    // Perform sharpening on the image
    pngJpgImage.sharpenImage(image2Name, "sharp-img", 0);

    // Get the sharpened image data
    int[][][] sharpenedImageData = pngJpgImage.getRgbDataMap("sharp-img");

    // Define the expected RGB values for the sharpened image
    int[][][] expectedSharpenedImageData = {
            {{0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,},
                    {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,},
                    {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {180, 75, 40,}, {220, 167, 0,}, {160, 255, 251,},
                    {163, 136, 180,}, {195, 175, 0,}, {68, 179, 84,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {223, 12, 52,}, {132, 96, 163,}, {116, 148, 239,},
                    {143, 255, 191,}, {179, 255, 0,}, {155, 255, 0,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {179, 75, 92,}, {196, 31, 83,}, {8, 152, 255,}, {71, 88, 239,},
                    {203, 135, 0,}, {68, 155, 104,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {191, 135, 112,}, {168, 64, 103,}, {80, 108, 199,},
                    {0, 220, 163,}, {136, 12, 188,}, {171, 0, 131,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {180, 108, 136,}, {172, 192, 88,}, {56, 255, 80,},
                    {64, 255, 64,}, {232, 198, 8,}, {255, 51, 108,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {184, 124, 112,}, {56, 255, 160,}, {116, 255, 32,},
                    {204, 255, 0,}, {255, 115, 88,}, {255, 132, 68,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,},
                    {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,},
                    {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}}
    };

    for (int y = 0; y < expectedSharpenedImageData.length; y++) {
      System.out.print("{");
      for (int x = 0; x < expectedSharpenedImageData[y].length; x++) {
        System.out.print("{");
        for (int c = 0; c < 3; c++) {
          //System.out.print(sharpenedImageData[y][x][c] + ", ");
          assertEquals(expectedSharpenedImageData[y][x][c], sharpenedImageData[y][x][c]);
        }
        System.out.print("},");
      }
      System.out.println("},");
    }
  }

  @Test
  public void testSharpenImageWith100Split() throws IOException {

    // Perform sharpening on the image
    pngJpgImage.sharpenImage(image2Name, "sharp-img", 100);

    // Get the sharpened image data
    int[][][] sharpenedImageData = pngJpgImage.getRgbDataMap("sharp-img");

    // Define the expected RGB values for the sharpened image
    int[][][] expectedSharpenedImageData = {
            {{0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,},
                    {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,},
                    {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {180, 75, 40,}, {220, 167, 0,}, {160, 255, 251,},
                    {163, 136, 180,}, {195, 175, 0,}, {68, 179, 84,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {223, 12, 52,}, {132, 96, 163,}, {116, 148, 239,},
                    {143, 255, 191,}, {179, 255, 0,}, {155, 255, 0,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {179, 75, 92,}, {196, 31, 83,}, {8, 152, 255,}, {71, 88, 239,},
                    {203, 135, 0,}, {68, 155, 104,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {191, 135, 112,}, {168, 64, 103,}, {80, 108, 199,},
                    {0, 220, 163,}, {136, 12, 188,}, {171, 0, 131,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {180, 108, 136,}, {172, 192, 88,}, {56, 255, 80,},
                    {64, 255, 64,}, {232, 198, 8,}, {255, 51, 108,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {184, 124, 112,}, {56, 255, 160,}, {116, 255, 32,},
                    {204, 255, 0,}, {255, 115, 88,}, {255, 132, 68,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,},
                    {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,},},
            {{0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,},
                    {0, 0, 0,}, {0, 0, 0,}, {0, 0, 0,}}
    };

    for (int y = 0; y < expectedSharpenedImageData.length; y++) {
      System.out.print("{");
      for (int x = 0; x < expectedSharpenedImageData[y].length; x++) {
        System.out.print("{");
        for (int c = 0; c < 3; c++) {
          //System.out.print(sharpenedImageData[y][x][c] + ", ");
          assertEquals(expectedSharpenedImageData[y][x][c], sharpenedImageData[y][x][c]);
        }
        System.out.print("},");
      }
      System.out.println("},");
    }
  }

  @Test
  public void testSepiaImageWith0Split() {

    pngJpgImage.sepiaImage(imageName, "sepia-img", 0);

    // Get the sepia image data
    int[][][] sepiaImageData = pngJpgImage.getRgbDataMap("sepia-img");

    // Define the expected RGB values for the sepia-toned image
    int[][][] expectedSepiaImageData = {
            {{100, 88, 69,}, {196, 174, 136,},},
            {{48, 42, 33,}, {255, 255, 238,},}
    };


    // Compare the actual sepia-toned image data with the expected result
    for (int y = 0; y < sepiaImageData.length; y++) {
      System.out.print("{");
      for (int x = 0; x < sepiaImageData[y].length; x++) {
        System.out.print("{");
        for (int c = 0; c < 3; c++) {
          //System.out.print(sepiaImageData[y][x][c] + ", ");
          assertEquals(expectedSepiaImageData[y][x][c], sepiaImageData[y][x][c]);
        }
        System.out.print("},");
      }
      System.out.println("},");
    }
  }

  @Test
  public void testSepiaImageWith50Split() {

    pngJpgImage.sepiaImage(imageName, "sepia-img", 50);

    // Get the sepia image data
    int[][][] sepiaImageData = pngJpgImage.getRgbDataMap("sepia-img");

    // Define the expected RGB values for the sepia-toned image
    int[][][] expectedSepiaImageData = {
            {{255, 0, 0,}, {196, 174, 136,},},
            {{0, 0, 255,}, {255, 255, 238,},}
    };


    // Compare the actual sepia-toned image data with the expected result
    for (int y = 0; y < sepiaImageData.length; y++) {
      System.out.print("{");
      for (int x = 0; x < sepiaImageData[y].length; x++) {
        System.out.print("{");
        for (int c = 0; c < 3; c++) {
          //System.out.print(sepiaImageData[y][x][c] + ", ");
          assertEquals(expectedSepiaImageData[y][x][c], sepiaImageData[y][x][c]);
        }
        System.out.print("},");
      }
      System.out.println("},");
    }
  }

  @Test
  public void testSepiaImageWith100Split() {

    pngJpgImage.sepiaImage(imageName, "sepia-img", 100);

    // Get the sepia image data
    int[][][] sepiaImageData = pngJpgImage.getRgbDataMap("sepia-img");

    // Define the expected RGB values for the sepia-toned image
    int[][][] expectedSepiaImageData = {
            {{255, 0, 0,}, {0, 255, 0,},},
            {{0, 0, 255,}, {255, 255, 255,},}
    };


    // Compare the actual sepia-toned image data with the expected result
    for (int y = 0; y < sepiaImageData.length; y++) {
      System.out.print("{");
      for (int x = 0; x < sepiaImageData[y].length; x++) {
        System.out.print("{");
        for (int c = 0; c < 3; c++) {
          //System.out.print(sepiaImageData[y][x][c] + ", ");
          assertEquals(expectedSepiaImageData[y][x][c], sepiaImageData[y][x][c]);
        }
        System.out.print("},");
      }
      System.out.println("},");
    }
  }


  @Test
  public void testColorCorrectionWith0Split() {

    pngJpgImage.colorCorrectImage(imageName, "color-correct-img", 0);

    // Get the sepia image data
    int[][][] sepiaImageData = pngJpgImage.getRgbDataMap("color-correct-img");

    // Define the expected RGB values for the sepia-toned image
    int[][][] expectedSepiaImageData = {
            {{255, 0, 0,}, {0, 255, 0,},},
            {{0, 0, 255,}, {255, 255, 255,},}
    };


    // Compare the actual sepia-toned image data with the expected result
    for (int y = 0; y < sepiaImageData.length; y++) {
      System.out.print("{");
      for (int x = 0; x < sepiaImageData[y].length; x++) {
        System.out.print("{");
        for (int c = 0; c < 3; c++) {
          //System.out.print(sepiaImageData[y][x][c] + ", ");
          assertEquals(expectedSepiaImageData[y][x][c], sepiaImageData[y][x][c]);
        }
        System.out.print("},");
      }
      System.out.println("},");
    }
  }


  @Test
  public void testColorCorrectionWith50Split() {

    pngJpgImage.colorCorrectImage(imageName, "color-correct-img", 50);

    // Get the sepia image data
    int[][][] sepiaImageData = pngJpgImage.getRgbDataMap("color-correct-img");

    // Define the expected RGB values for the sepia-toned image
    int[][][] expectedSepiaImageData = {
            {{255, 0, 0,}, {0, 255, 0,},},
            {{0, 0, 255,}, {255, 255, 255,}}
    };


    // Compare the actual sepia-toned image data with the expected result
    for (int y = 0; y < sepiaImageData.length; y++) {
      System.out.print("{");
      for (int x = 0; x < sepiaImageData[y].length; x++) {
        System.out.print("{");
        for (int c = 0; c < 3; c++) {
          // System.out.print(sepiaImageData[y][x][c] + ", ");
          assertEquals(expectedSepiaImageData[y][x][c], sepiaImageData[y][x][c]);
        }
        System.out.print("},");
      }
      System.out.println("},");
    }
  }

  @Test
  public void testColorCorrectionWith100Split() {

    pngJpgImage.colorCorrectImage(imageName, "color-correct-img", 100);

    // Get the sepia image data
    int[][][] sepiaImageData = pngJpgImage.getRgbDataMap("color-correct-img");

    // Define the expected RGB values for the sepia-toned image
    int[][][] expectedSepiaImageData = {
            {{255, 0, 0,}, {0, 255, 0,},},
            {{0, 0, 255,}, {255, 255, 255,}}
    };


    // Compare the actual sepia-toned image data with the expected result
    for (int y = 0; y < sepiaImageData.length; y++) {
      System.out.print("{");
      for (int x = 0; x < sepiaImageData[y].length; x++) {
        System.out.print("{");
        for (int c = 0; c < 3; c++) {
          //System.out.print(sepiaImageData[y][x][c] + ", ");
          assertEquals(expectedSepiaImageData[y][x][c], sepiaImageData[y][x][c]);
        }
        System.out.print("},");
      }
      System.out.println("},");
    }
  }


}
