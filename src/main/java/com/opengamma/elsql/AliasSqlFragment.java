/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.elsql;

/**
 * Representation of ALIAS(alias).
 * <p>
 * This replaces all occurences of '{alias}' with the given alias in the contents.
 * This alias may be an identifier (consisting of 0-9, a-z, A-Z, _), or a variable.
 */
final class AliasSqlFragment extends ContainerSqlFragment {

  private static final String SEARCH = "{alias}";

  private final String _alias;


  /**
   * Creates an instance.
   *
   * @param alias   the alias value, not null
   */
  AliasSqlFragment(String alias) {
    if (alias == null) {
      throw new IllegalArgumentException("Alias must be specified");
    }
    _alias = alias;
  }

  //-------------------------------------------------------------------------
  @Override
  void toSQL(StringBuilder buf, SqlFragments fragments, SqlParams params, int[] loopIndex) {
    StringBuilder tmpBuf = new StringBuilder();
    super.toSQL(tmpBuf, fragments, params, loopIndex);

    String replacement = _alias;
    if (replacement.startsWith(":")) {
      String var = extractVariableName(replacement);
      if (params.contains(var)) {
        replacement = params.get(var).toString();
      } else {
        throw new IllegalArgumentException("Can not resolve alias variable '" + replacement + "'.");
      }
    }

    buf.append(tmpBuf.toString().replace(SEARCH, replacement));
  }

  //-------------------------------------------------------------------------
  @Override
  public String toString() {
    return getClass().getSimpleName() + ":" + _alias + " " + getFragments();
  }
}
