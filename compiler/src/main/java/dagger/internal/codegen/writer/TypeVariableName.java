package dagger.internal.codegen.writer;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.util.Set;

public class TypeVariableName implements TypeName {
  private final String name;
  private final Optional<TypeName> extendsBound;
  private final Optional<TypeName> superBound;
  TypeVariableName(String name, Optional<TypeName> extendsBound,
      Optional<TypeName> superBound) {
    this.name = name;
    this.extendsBound = extendsBound;
    this.superBound = superBound;
  }

  public String name() {
    return name;
  }

  @Override
  public Set<ClassName> referencedClasses() {
    ImmutableSet.Builder<ClassName> builder = new ImmutableSet.Builder<ClassName>();
    if (extendsBound.isPresent()) {
      builder.addAll(extendsBound.get().referencedClasses());
    }
    if (superBound.isPresent()) {
      builder.addAll(superBound.get().referencedClasses());
    }
    return builder.build();
  }

  @Override
  public Appendable write(Appendable appendable, Context context) throws IOException {
    appendable.append(name);
    if (extendsBound.isPresent()) {
      appendable.append(' ');
      extendsBound.get().write(appendable, context);
    }
    if (superBound.isPresent()) {
      appendable.append(' ');
      superBound.get().write(appendable, context);
    }
    return appendable;
  }

  @Override
  public String toString() {
    return Writables.writeToString(this);
  }

  static TypeVariableName named(String name) {
    return new TypeVariableName(
        name, Optional.<TypeName>absent(), Optional.<TypeName>absent());
  }
}
