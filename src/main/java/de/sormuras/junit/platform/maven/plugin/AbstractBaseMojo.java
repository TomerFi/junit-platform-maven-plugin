/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.sormuras.junit.platform.maven.plugin;

import static java.lang.String.format;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;

/** Provides basic utility helpers. */
abstract class AbstractBaseMojo extends AbstractMojo {

  @Parameter(defaultValue = "${project}", readonly = true, required = true)
  private MavenProject project;

  /** The entry point to Maven Artifact Resolver, i.e. the component doing all the work. */
  @Component private RepositorySystem repositorySystem;

  /** The current repository/network configuration of Maven. */
  @Parameter(defaultValue = "${repositorySystemSession}", readonly = true)
  private RepositorySystemSession repositorySession;

  /** The project's remote repositories to use for the resolution. */
  @Parameter(defaultValue = "${project.remotePluginRepositories}", readonly = true)
  private List<RemoteRepository> repositories;

  MavenProject getMavenProject() {
    return project;
  }

  List<Artifact> resolve(String coordinates) throws Exception {
    var log = getLog();
    var artifact = new DefaultArtifact(coordinates);
    var request = new ArtifactRequest();
    request.setArtifact(artifact);
    request.setRepositories(repositories);

    log.debug(format("Resolving artifact %s from %s...", artifact, repositories));
    var resolved = repositorySystem.resolveArtifact(repositorySession, request);
    log.debug(format("Resolved %s from %s", artifact, resolved.getRepository()));
    log.debug(format("Stored %s to %s", artifact, resolved.getArtifact().getFile()));

    var collectRequest = new CollectRequest();
    collectRequest.setRoot(new Dependency(artifact, ""));
    collectRequest.setRepositories(repositories);

    var dependencyRequest = new DependencyRequest(collectRequest, (all, ways) -> true);
    var artifactResults =
        repositorySystem
            .resolveDependencies(repositorySession, dependencyRequest)
            .getArtifactResults();

    return artifactResults
        .stream()
        .map(ArtifactResult::getArtifact)
        .peek(a -> log.info(format("%s resolved to %s", a, a.getFile())))
        .collect(Collectors.toList());
  }
}