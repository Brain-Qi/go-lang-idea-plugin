// This is a generated file. Not intended for manual editing.
package com.goide.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.goide.stubs.GoTypeSpecStub;
import com.intellij.psi.ResolveState;

public interface GoTypeSpec extends GoNamedElement, StubBasedPsiElement<GoTypeSpecStub> {

  @Nullable
  GoType getType();

  @NotNull
  PsiElement getIdentifier();

  @Nullable
  GoType getGoType(ResolveState context);

  @NotNull
  List<GoMethodDeclaration> getMethods();

  boolean shouldGoDeeper();

}
