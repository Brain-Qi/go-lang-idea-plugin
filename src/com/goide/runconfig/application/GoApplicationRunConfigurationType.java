/*
 * Copyright 2013-2014 Sergey Ignatov, Alexander Zolotov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.goide.runconfig.application;

import com.goide.GoIcons;
import com.goide.runconfig.GoConfigurationFactoryBase;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class GoApplicationRunConfigurationType extends ConfigurationTypeBase {
  public GoApplicationRunConfigurationType() {
    super("GoApplicationRunConfiguration", "Go Application", "Go application run configuration", GoIcons.APPLICATION_RUN);
    addFactory(new GoConfigurationFactoryBase(this) {
      @NotNull
      public RunConfiguration createTemplateConfiguration(Project project) {
        return new GoApplicationConfiguration(project, "Go", getInstance());
      }
    });
  }

  @NotNull
  public static GoApplicationRunConfigurationType getInstance() {
    return Extensions.findExtension(CONFIGURATION_TYPE_EP, GoApplicationRunConfigurationType.class);
  }
}
