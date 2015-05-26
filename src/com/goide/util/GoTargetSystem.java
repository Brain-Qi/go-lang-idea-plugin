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

package com.goide.util;

import com.intellij.util.ThreeState;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

public class GoTargetSystem {
  @NotNull public final String os;
  @NotNull public final String arch;
  @Nullable public final String goVersion;
  @Nullable public final String compiler;
  @NotNull public final ThreeState cgoEnabled;

  private final Set<String> customFlags = ContainerUtil.newHashSet();

  public GoTargetSystem(@NotNull String os, @NotNull String arch, @Nullable String goVersion, @Nullable String compiler,
                        @NotNull ThreeState cgoEnabled, @NotNull String... customFlags) {
    this.os = os;
    this.arch = arch;
    this.goVersion = goVersion;
    this.compiler = compiler;
    this.cgoEnabled = cgoEnabled;
    Collections.addAll(this.customFlags, customFlags);
  }

  public boolean supportsFlag(@NotNull String flag) {
    return customFlags.contains(flag);
  }
}
