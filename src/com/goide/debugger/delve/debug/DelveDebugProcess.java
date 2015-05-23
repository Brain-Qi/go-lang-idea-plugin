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

package com.goide.debugger.delve.debug;

import com.goide.debugger.delve.dlv.Delve;
import com.goide.debugger.delve.run.DelveExecutionResult;
import com.goide.debugger.delve.run.DelveRunConfiguration;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ExecutionConsole;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider;
import org.jetbrains.annotations.NotNull;

public class DelveDebugProcess extends XDebugProcess {
  private static final Logger LOG = Logger.getInstance(DelveDebugProcess.class);

  private final DelveDebuggerEditorsProvider myEditorsProvider = new DelveDebuggerEditorsProvider();
  @NotNull private final ConsoleView myConsole;
  private final DelveRunConfiguration myConfiguration;

  @NotNull private final Delve myDelve;

  /**
   * Constructor; launches delve
   */
  public DelveDebugProcess(@NotNull XDebugSession session, DelveExecutionResult executionResult) {
    super(session);

    myConfiguration = executionResult.getConfiguration();
    myConsole = (ConsoleView)executionResult.getExecutionConsole();

    myDelve = new Delve();
  }

  @NotNull
  @Override
  public XDebuggerEditorsProvider getEditorsProvider() {
    return myEditorsProvider;
  }

  @Override
  public void startStepOver() {
    LOG.warn("startStepOver: stub");
  }

  @Override
  public void startStepInto() {
    LOG.warn("startStepInto: stub");
  }

  @Override
  public void startStepOut() {
    LOG.warn("startStepOut: stub");
  }

  @Override
  public void resume() {
    LOG.warn("resume: stub");
  }

  @Override
  public void runToPosition(@NotNull XSourcePosition position) {
    LOG.warn("runToPosition: stub");
  }

  @NotNull
  @Override
  public ExecutionConsole createConsole() {
    return myConsole;
  }

  @NotNull
  public Delve getDelve() {
    return myDelve;
  }
}
