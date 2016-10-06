package com.silentgo.core.db.funcanalyse;

/**
 * Project : parent
 * Package : com.silentgo.core.db.funcanalyse
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/30.
 */
public enum DaoKeyWord {
    Delete("Delete"),
    Update("Update"),
    Insert("Insert"),
    Order("Order"),
    Query("Query"),
    Group("Group"),
    Limit("Limit"),
    By("By"),
    One("One"),
    Desc("desc"),
    Asc("asc"),
    List("List");

    public String innername;

    DaoKeyWord(String innername) {
        this.innername = innername;
    }

    public boolean match(String m) {
        return this.innername.equals(m);
    }

    public static String[] getValues() {
        String[] vals = new String[DaoKeyWord.values().length];
        for (int i = 0; i < DaoKeyWord.values().length; i++) {
            vals[i] = DaoKeyWord.values()[i].innername;
        }
        return vals;
    }
}
