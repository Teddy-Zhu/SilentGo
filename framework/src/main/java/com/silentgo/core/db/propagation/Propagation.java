package com.silentgo.core.db.propagation;

/**
 * Project : SilentGo
 * Package : com.silentgo.core.db
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/11/25.
 */
public enum Propagation {
    //common nested
    PROPAGATION_REQUIRED,
    /**
     * Support a current transaction, execute non-transactionally if none exists.
     */
    PROPAGATION_SUPPORTS,
    /**
     *Create a new transaction, suspend the current transaction if one exists.
     */
    PROPAGATION_REQUIRES_NEW,
    /**
     * Execute non-transactionally, suspend the current transaction if one exists.
     */
    PROPAGATION_NOT_SUPPORTED
}
