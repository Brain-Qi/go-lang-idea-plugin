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

package com.goide.go;

import com.goide.psi.GoFile;
import com.goide.psi.GoNamedElement;
import com.goide.tree.GoStructureViewFactory;
import com.intellij.navigation.ChooseByNameContributorEx;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.intellij.util.ArrayUtil;
import com.intellij.util.ObjectUtils;
import com.intellij.util.Processor;
import com.intellij.util.indexing.FindSymbolParameters;
import com.intellij.util.indexing.IdFilter;
import org.jetbrains.annotations.NotNull;

public class GoGotoContributorBase<T extends GoNamedElement> implements ChooseByNameContributorEx {
  protected final StubIndexKey<String, T> myIndexKey;
  @NotNull private final Class<T> myClazz;

  public GoGotoContributorBase(@NotNull StubIndexKey<String, T> key, @NotNull Class<T> clazz) {
    myIndexKey = key;
    myClazz = clazz;
  }

  @NotNull
  @Override
  public String[] getNames(@NotNull Project project, boolean includeNonProjectItems) {
    return ArrayUtil.EMPTY_STRING_ARRAY;
  }

  @NotNull
  @Override
  public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {
    return NavigationItem.EMPTY_NAVIGATION_ITEM_ARRAY;
  }

  @Override
  public void processNames(@NotNull Processor<String> processor, @NotNull GlobalSearchScope scope, IdFilter filter) {
    StubIndex.getInstance().processAllKeys(myIndexKey, processor, scope, filter);
  }

  @Override
  public void processElementsWithName(@NotNull String s,
                                      @NotNull final Processor<NavigationItem> processor,
                                      @NotNull FindSymbolParameters parameters) {
    StubIndex.getInstance().processElements(myIndexKey, s, parameters.getProject(), parameters.getSearchScope(),
                                            parameters.getIdFilter(), myClazz, new Processor<GoNamedElement>() {
        @Override
        public boolean process(final GoNamedElement namedElement) {
          return processor.process(new GoStructureViewFactory.Element(namedElement) {
            @Override
            public String getLocationString() {
              GoFile file = namedElement.getContainingFile();
              String locationString = ObjectUtils.notNull(file.getImportPath(), ObjectUtils.notNull(file.getPackageName(), file.getName()));
              return "(in " + locationString + ")";
            }
          });
        }
      });
  }
}
