/*
 * Copyright 2013-2015 Sergey Ignatov, Alexander Zolotov, Mihai Toader, Florin Patan
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

package com.goide.project;

import com.goide.GoConstants;
import com.goide.sdk.GoSdkUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;

@State(
  name = GoConstants.GO_LIBRARIES_SERVICE_NAME,
  storages = @Storage(file = StoragePathMacros.APP_CONFIG + "/" + GoConstants.GO_LIBRARIES_CONFIG_FILE)
)
public class GoApplicationLibrariesService extends GoLibrariesService {
  private boolean myUseGoPathFromSystemEnvironment = true;

  public static GoApplicationLibrariesService getInstance() {
    return ServiceManager.getService(GoApplicationLibrariesService.class);
  }

  public boolean isUseGoPathFromSystemEnvironment() {
    return myUseGoPathFromSystemEnvironment;
  }

  public void setUseGoPathFromSystemEnvironment(boolean useGoPathFromSystemEnvironment) {
    if (myUseGoPathFromSystemEnvironment != useGoPathFromSystemEnvironment) {
      myUseGoPathFromSystemEnvironment = useGoPathFromSystemEnvironment;
      if (!GoSdkUtil.getGoPathsRootsFromEnvironment().isEmpty()) {
        incModificationCount();
        ApplicationManager.getApplication().getMessageBus().syncPublisher(LIBRARIES_TOPIC).librariesChanged(getLibraryRootUrls());
      }
    }
  }
}
