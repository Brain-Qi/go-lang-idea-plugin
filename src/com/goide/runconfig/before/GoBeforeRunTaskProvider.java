package com.goide.runconfig.before;

import com.goide.GoIcons;
import com.goide.runconfig.GoRunConfigurationBase;
import com.goide.sdk.GoSdkService;
import com.goide.util.GoExecutor;
import com.intellij.execution.BeforeRunTaskProvider;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.concurrency.Semaphore;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GoBeforeRunTaskProvider extends BeforeRunTaskProvider<GoCommandBeforeRunTask> {
  public static final Key<GoCommandBeforeRunTask> ID = Key.create("GoBeforeRunTask");

  @Override
  public Key<GoCommandBeforeRunTask> getId() {
    return ID;
  }

  @Override
  public String getName() {
    return "Go Command";
  }

  @Override
  public String getDescription(GoCommandBeforeRunTask task) {
    return "Run `" + task.toString() + "`";
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return GoIcons.APPLICATION_RUN;
  }

  @Override
  public boolean isConfigurable() {
    return true;
  }

  @Nullable
  @Override
  public Icon getTaskIcon(GoCommandBeforeRunTask task) {
    return getIcon();
  }

  @Nullable
  @Override
  public GoCommandBeforeRunTask createTask(RunConfiguration runConfiguration) {
    return runConfiguration instanceof GoRunConfigurationBase ? new GoCommandBeforeRunTask() : null;
  }

  @Override
  public boolean configureTask(RunConfiguration configuration, GoCommandBeforeRunTask task) {
    final Project project = configuration.getProject();
    if (!(configuration instanceof GoRunConfigurationBase)) {
      showAddingTaskErrorMessage(project, "Go Command task supports only Go Run Configurations");
      return false;
    }

    Module module = ((GoRunConfigurationBase)configuration).getConfigurationModule().getModule();
    if (!GoSdkService.getInstance(project).isGoModule(module)) {
      showAddingTaskErrorMessage(project, "Go Command task supports only Go Modules");
      return false;
    }

    GoCommandConfigureDialog dialog = new GoCommandConfigureDialog(project);
    if (dialog.showAndGet()) {
      task.setCommand(dialog.getCommand());
      return true;
    }
    return false;
  }

  @Override
  public boolean canExecuteTask(RunConfiguration configuration, GoCommandBeforeRunTask task) {
    if (configuration instanceof GoRunConfigurationBase) {
      Module module = ((GoRunConfigurationBase)configuration).getConfigurationModule().getModule();
      GoSdkService sdkService = GoSdkService.getInstance(configuration.getProject());
      if (sdkService.isGoModule(module)) {
        return StringUtil.isNotEmpty(sdkService.getSdkHomePath(module)) && StringUtil.isNotEmpty(task.getCommand());
      }
    }
    return false;
  }

  @Override
  public boolean executeTask(final DataContext context,
                             final RunConfiguration configuration,
                             ExecutionEnvironment env,
                             final GoCommandBeforeRunTask task) {
    final Semaphore done = new Semaphore();
    final Ref<Boolean> result = new Ref<Boolean>(false);

    GoRunConfigurationBase goRunConfiguration = (GoRunConfigurationBase)configuration;
    final Module module = goRunConfiguration.getConfigurationModule().getModule();
    final Project project = configuration.getProject();
    final String workingDirectory = goRunConfiguration.getWorkingDirectory();

    UIUtil.invokeAndWaitIfNeeded(new Runnable() {
      public void run() {
        if (StringUtil.isEmpty(task.getCommand())) return;
        if (project == null || project.isDisposed()) return;
        GoSdkService sdkService = GoSdkService.getInstance(project);
        if (!sdkService.isGoModule(module)) return;

        done.down();
        GoExecutor.in(module).withParameters(task.getCommand())
          .withWorkDirectory(workingDirectory)
          .showOutputOnError()
          .showNotifications(false)
          .withPresentableName("Executing `" + task.toString() + "`")
          .withProcessListener(new ProcessAdapter() {
            @Override
            public void processTerminated(ProcessEvent event) {
              done.up();
              result.set(event.getExitCode() == 0);
            }
          })
          .executeWithProgress(false);
      }
    });

    done.waitFor();
    return result.get();
  }

  private static void showAddingTaskErrorMessage(final Project project, final String message) {
    Messages.showErrorDialog(project, message, "Go Command Task");
  }
}