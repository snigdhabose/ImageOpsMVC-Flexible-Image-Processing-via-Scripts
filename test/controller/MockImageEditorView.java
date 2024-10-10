package controller;

import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JComboBox;


import view.ImageEditorView;

/**
 * Mock view class for testing purposes.
 */
public class MockImageEditorView extends ImageEditorView {
  private int updateImageCallCount;
  JButton fileOpenButtonForLoad;
  JButton buttonForSave;

  JButton buttonForApply;
  String selectedFilter = null;
  String filePath = null;
  JComboBox<String> emptyComboBox;
  String message = null;

  int b = 0;
  int m = 0;
  int w = 0;

  int compressionPercentage = 0;
  int slider = 0;

  /**
   * Constructor for the mock view.
   */
  public MockImageEditorView() {
    fileOpenButtonForLoad = new JButton("Open File");
    buttonForSave = new JButton("Save File");
    buttonForApply = new JButton("Save File");
    updateImageCallCount = 0;
    emptyComboBox = new JComboBox<>();
  }


  public void setFilePath(String path) {
    filePath = "'" + path + "'";
  }

  /**
   * Sets the values for the levels adjust filter.
   *
   * @param bV value for b
   * @param mV value for m
   * @param wV value for w
   */
  public void setBMW(int bV, int mV, int wV) {
    b = bV;
    m = mV;
    w = wV;
  }

  public void setCompressionPercentage(int c) {
    compressionPercentage = c;
  }

  public void setFilterOptions(String filter) {
    selectedFilter = filter;
  }

  public String getSelectedFilter() {
    return selectedFilter;
  }

  public String getMessage() {
    return message;
  }

  // Inside the MockImageEditorView class
  public JComboBox<String> getEmptyComboBox() {
    return emptyComboBox;
  }

  JButton getButtonForLoad() {
    updateImageCallCount = 0;
    compressionPercentage = 0;
    b = 0;
    m = 0;
    w = 0;
    slider = 0;
    return fileOpenButtonForLoad;
  }

  JButton getButtonForApply() {
    return buttonForApply;
  }

  JButton getButtonForSave() {
    return buttonForSave;
  }

  @Override
  public void updateImageForIndex(int[][][] rgbValues, int index) {
    // Count number of times it simulates updating the image in the view
    updateImageCallCount++;


  }

  // Getter for testing purposes
  public int getUpdateImageCallCount() {
    return updateImageCallCount;
  }

  public void setSlider(int s) {
    slider = s;
  }

  @Override
  public void addFeatures(ControllerFeatures features) {

    emptyComboBox.addActionListener(evt -> {
      System.out.println("ComboBox is triggered. Selected Filter:" + evt.getActionCommand());
      selectedFilter = evt.getActionCommand();
    });

    buttonForApply.addActionListener(evt -> {
      String source = evt.getActionCommand();
      String msg = null;
      if (slider != 0) {
        msg = features.applyFeatures(selectedFilter + " " + source + " " + selectedFilter
                + "-img-split split " + slider, selectedFilter + "-img-split");
      } else {
        if (Objects.equals(selectedFilter, "levels-adjust")) {
          msg = features.applyFeatures(selectedFilter + " " + b + " " + m + " " + w + " "
                          + source + " " + selectedFilter + "-img",
                  selectedFilter + "-img");
        } else if (Objects.equals(selectedFilter, "compress")) {
          msg = features.applyFeatures(selectedFilter + " " + compressionPercentage
                          + " " + source + " " + selectedFilter + "-img",
                  selectedFilter + "-img");
        } else {
          msg = features.applyFeatures(selectedFilter + " " + source + " "
                  + selectedFilter + "-img", selectedFilter + "-img");
        }
      }
      System.out.println("Applying Filter from Mock View: " + selectedFilter);
      slider = 0;
      message = msg;

    });
    fileOpenButtonForLoad.addActionListener(evt -> {
      String msg = features.applyFeatures("load " + filePath + " img",
              "img");
      System.out.println("Loading from Mock View: " + selectedFilter);
      message = msg;

    });
    buttonForSave.addActionListener(evt -> {
      String msg = features.applyFeatures("save " + filePath + " img",
              "img");
      System.out.println("Saving from Mock View: " + selectedFilter);
      message = msg;
    });
  }
}

