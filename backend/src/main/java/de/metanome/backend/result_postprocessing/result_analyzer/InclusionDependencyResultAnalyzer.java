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

package de.metanome.backend.result_postprocessing.result_analyzer;

import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.InclusionDependency;
import de.metanome.backend.result_postprocessing.result_ranking.InclusionDependencyRanking;
import de.metanome.backend.result_postprocessing.results.InclusionDependencyResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Analyzes Inclusion Dependency Results.
 */
public class InclusionDependencyResultAnalyzer
    extends ResultAnalyzer<InclusionDependency, InclusionDependencyResult> {

  public InclusionDependencyResultAnalyzer(List<RelationalInputGenerator> inputGenerators,
                                           boolean useDataIndependentStatistics)
      throws InputGenerationException, InputIterationException {
    super(inputGenerators, useDataIndependentStatistics);
  }

  @Override
  protected List<InclusionDependencyResult> analyzeResultsDataIndependent(
      List<InclusionDependency> prevResults) {
    List<InclusionDependencyResult> results = convertResults(prevResults);

    if (!this.tableInformationMap.isEmpty()) {
      InclusionDependencyRanking
          ranking =
          new InclusionDependencyRanking(results, tableInformationMap);
      ranking.calculateDataIndependentRankings();
    }

    return results;
  }

  @Override
  protected List<InclusionDependencyResult> analyzeResultsDataDependent(
      List<InclusionDependency> prevResults) {
    List<InclusionDependencyResult> results = convertResults(prevResults);

    if (!this.tableInformationMap.isEmpty()) {
      InclusionDependencyRanking ranking =
          new InclusionDependencyRanking(results, tableInformationMap);
      ranking.calculateDataIndependentRankings();
      ranking.calculateDataDependentRankings();
    }

    return results;
  }

  @Override
  public void printResultsToFile() {

  }

  @Override
  protected List<InclusionDependencyResult> convertResults(List<InclusionDependency> prevResults) {
    List<InclusionDependencyResult> results = new ArrayList<>();

    for (InclusionDependency prevResult : prevResults) {
      InclusionDependencyResult result = new InclusionDependencyResult(prevResult);
      results.add(result);
    }

    return results;
  }

}
