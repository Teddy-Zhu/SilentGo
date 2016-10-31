package com.silentgo.orm.sqlparser.funcanalyse;

/**
 * Project : parent
 * Package : com.silentgo.orm.sqlparser.funcanalyse
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/30.
 */
public enum DaoKeyWord {
    Delete("delete"),
    Update("update"),
    Insert("insert"),
    Order("Order"),
    Query("query"),
    Group("Group"),
    Limit("Limit"),
    And("And"),
    Count("count"),
    By("By"),
    Where("Where"),
    Set("Set"),
    One("One"),
    Desc("Desc"),
    Asc("Asc"),
    List("List");

    public String innername;

    DaoKeyWord(String innername) {
        this.innername = innername;
    }

    public static String[] getValues() {
        String[] vals = new String[DaoKeyWord.values().length];
        for (int i = 0; i < DaoKeyWord.values().length; i++) {
            vals[i] = DaoKeyWord.values()[i].innername;
        }
        return vals;
    }

    public boolean equals(String m) {
        return innername.equals(m);
    }
}
