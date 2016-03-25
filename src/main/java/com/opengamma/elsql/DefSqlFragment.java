/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.elsql;

/**
 * Representation of DEF(key).
 * <p>
 * This renders a table or column name based on a defined @NAME block with the name {@code D_tableKey} or
 * {@code D_tableKey_columnKey}.
 */
final class DefSqlFragment extends SqlFragment {

  /**
   * The table key.
   */
  private final String _tableKey;

  /**
   * The table key.
   */
  private final String _columnKey;

  /**
   * Creates an instance.
   *
   * @param tableKey  the table definition key, not null
   */
  DefSqlFragment(String tableKey, String columnKey) {
    if (tableKey == null) {
      throw new IllegalArgumentException("Def key must be specified");
    }
    _tableKey = tableKey;
    _columnKey = columnKey;
  }

  //-------------------------------------------------------------------------
  @Override
  void toSQL(StringBuilder buf, SqlFragments fragments, SqlParams params, int[] loopIndex) {
    String fragmentName = "D_" + parseKey(_tableKey, params, loopIndex);
    if (_columnKey != null) {
      fragmentName += '_' + parseKey(_columnKey, params, loopIndex);
    }

    NameSqlFragment unit = fragments.getFragment(fragmentName);
    unit.toSQL(buf, fragments, params, loopIndex);
  }

  private String parseKey(String key, SqlParams params, int[] loopIndex) {
    if (key.startsWith(":")) {
      String var = applyLoopIndex(key, loopIndex);
      var = extractVariableName(var);

      if (params.contains(var)) {
        return params.get(var).toString();
      }
    }

    return key;
  }

  //-------------------------------------------------------------------------
  @Override
  public String toString() {
    return getClass().getSimpleName() + ":" + _tableKey + ":" + _columnKey;
  }

}
