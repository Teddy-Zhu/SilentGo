package com.silentgo.orm.sqlparser.daoresolve;

import com.silentgo.orm.base.DaoResolveEntity;
import com.silentgo.orm.base.Pager;
import com.silentgo.orm.base.TableModel;
import com.silentgo.orm.sqlparser.SQLKit;
import com.silentgo.orm.sqlparser.funcanalyse.DaoKeyWord;
import com.silentgo.orm.sqltool.SqlTokenGroup;
import com.silentgo.orm.sqltool.condition.CommonSqlCondition;
import com.silentgo.orm.sqltool.condition.SqlCondition;
import com.silentgo.orm.sqltool.sqltoken.ParamSqlToken;
import com.silentgo.orm.sqltool.sqltoken.StringSqlToken;

/**
 * Project : parent
 * Package : com.silentgo.orm.sqlparser.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 2016/10/2.
 */
public class LimitDaoResovler implements DaoResolver {
    @Override
    public boolean handle(DaoResolveEntity daoResolveEntity) {
        return daoResolveEntity.getParsedMethod().contains(DaoKeyWord.Limit.innername);
    }

    @Override
    public <T extends TableModel> void processSQL(DaoResolveEntity daoResolveEntity) {

        Pager pager = (Pager) daoResolveEntity.getNameObjects().get(SQLKit.PagerName);

        if (pager == null) throw new IllegalArgumentException("limit need pager parameter");
        SqlTokenGroup sqlTokenGroup = new SqlTokenGroup();
        SqlCondition sqlCondition = new CommonSqlCondition(sqlTokenGroup);
        sqlTokenGroup.appendToken(new StringSqlToken("limit "));
        sqlTokenGroup.appendToken(new ParamSqlToken(SQLKit.PagerName + ".start"));
        sqlTokenGroup.appendToken(new StringSqlToken(","));
        sqlTokenGroup.appendToken(new ParamSqlToken(SQLKit.PagerName + ".pageSize"));
        daoResolveEntity.getSqlTool().limit(sqlCondition);
    }
}
