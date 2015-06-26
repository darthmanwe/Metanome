/*
 * Copyright 2015 by the Metanome project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.metanome.backend.result_postprocessing.visualization;

import de.metanome.backend.result_postprocessing.visualization.UniqueColumnCombination.UniqueColumnCombinationVisualizationData;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Allows to print result structures for visualizations as JSON to use them in D3 later.
 */
@SuppressWarnings("unchecked")
public class JSONPrinter {

  /**
   * Prints information about clusters to file.
   *
   * @param filePath File path to the output file
   * @param clusters List of clusters
   */
  public static void printCluster(String filePath,
                                  List<HashMap<String, Double>> clusters) {
    JSONArray jsonCluster = new JSONArray();

    for (int i = 0; i < clusters.size(); i++) {
      HashMap<String, Double> info = clusters.get(i);
      JSONObject jsonEntry = new JSONObject();

      jsonEntry.put("ClusterNr", i);
      for (Map.Entry<String, Double> infoEntry : info.entrySet()) {
        jsonEntry.put(infoEntry.getKey(), infoEntry.getValue());
      }
      jsonCluster.add(jsonEntry);
    }

    writeToFile(filePath, jsonCluster);
  }

  /**
   * Prints the contained data of all clusters to file.
   *
   * @param filePath    File path to the output file
   * @param clusterData Data of the cluster
   */
  public static void printClusterData(String filePath,
                                      List<List<UniqueColumnCombinationVisualizationData>> clusterData) {
    JSONArray jsonData = new JSONArray();
    int id = 0;

    for (int i = 0; i < clusterData.size(); i++) {
      List<UniqueColumnCombinationVisualizationData> dataList = clusterData.get(i);

      for (UniqueColumnCombinationVisualizationData data : dataList) {
        HashMap<String, Double> info = data.getValues();
        JSONObject jsonEntry = new JSONObject();

        //Insert Cluster Number
        jsonEntry.put("ClusterNr", i);
        //Insert UCC ID
        jsonEntry.put("UCCid", id);

        //Insert all info items
        for (Map.Entry<String, Double> infoEntry : info.entrySet()) {
          jsonEntry.put(infoEntry.getKey(), infoEntry.getValue());
        }

        jsonData.add(jsonEntry);
        id++;
      }
    }
    writeToFile(filePath, jsonData);
  }


  /**
   * Prints the data of the cluster for a histogram.
   *
   * @param filePath File path to the output file
   * @param clusters Clusters containing the information for plotting histograms
   */
  public static void printColumnCombinations(String filePath,
                                             List<List<HashMap<String, Double>>> clusters) {
    JSONArray json = new JSONArray();
    int id = 0;

    for (List<HashMap<String, Double>> cluster : clusters) {
      for (HashMap<String, Double> data : cluster) {
        JSONObject entryJSON = new JSONObject();

        // Insert UCC ID
        entryJSON.put("UCCid", id);

        // Insert Histogram Data
        JSONArray jsonData = new JSONArray();

        for (Map.Entry<String, Double> dataPoint : data.entrySet()) {
          JSONObject jsonEntry = new JSONObject();
          jsonEntry.put("Column Name", dataPoint.getKey());
          jsonEntry.put("Uniqueness", dataPoint.getValue());
          jsonData.add(jsonEntry);
        }

        entryJSON.put("histogramData", jsonData);
        json.add(entryJSON);
        id++;
      }
    }
    writeToFile(filePath, json);
  }

  /**
   * Writes a JSON structure to file
   *
   * @param filePath   File path to the output file
   * @param jsonObject JSON structure which should be printed
   */
  private static void writeToFile(String filePath, JSONAware jsonObject) {
    try {
      File file = new File(filePath);
      file.mkdirs();
      if (file.exists()) {
        file.delete();
      }
      FileWriter fileWriter = new FileWriter(file);
      fileWriter.write(jsonObject.toJSONString());
      fileWriter.flush();
      fileWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
