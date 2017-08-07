package com.silentgo.orm.sqlparser.daoresolve;

import com.silentgo.orm.base.BaseTableInfo;
import com.silentgo.orm.base.DaoResolveEntity;
import com.silentgo.orm.base.TableModel;
import com.silentgo.orm.sqlparser.SQLKit;
import com.silentgo.orm.sqlparser.annotation.Where;
import com.silentgo.orm.sqlparser.annotation.WhereGroup;
import com.silentgo.orm.sqlparser.annotation.WhereJudge;
import com.silentgo.orm.sqlparser.funcanalyse.DaoKeyWord;
import com.silentgo.orm.sqltool.SqlTokenGroup;
import com.silentgo.orm.sqltool.sqltoken.IfSqlToken;
import com.silentgo.utils.StringKit;

import java.util.Collection;
import java.util.List;

/**
 * Project : SilentGo
 * Package : com.silentgo.orm.sqlparser.daoresolve
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 2016/10/28.
 */
public class WhereDaoResolver implements DaoResolver {
    @Override
    public boolean handle(DaoResolveEntity daoResolveEntity) {
        return daoResolveEntity.getParsedMethod().contains(DaoKeyWord.Where.innername);
    }

    @Override
    public <T extends TableModel> void processSQL(DaoResolveEntity daoResolveEntity) {
        List<String> parsedMethod = daoResolveEntity.getParsedMethod();
        BaseTableInfo tableInfo = daoResolveEntity.getTableInfo();

        int index = parsedMethod.indexOf(DaoKeyWord.Where.innername);
        String tfield = DaoResolveKit.getField(parsedMethod, index + 1);
        if (DaoResolveKit.isField(tfield, tableInfo))
            setWhere(index, DaoKeyWord.And.innername, parsedMethod, daoResolveEntity);
        Where queryBy = daoResolveEntity.getSgMethod().getAnnotation(Where.class);
        if (queryBy != null) {
            daoResolveEntity.getSqlTool().where(queryBy.value());
        }

        WhereGroup whereGroup = daoResolveEntity.getSgMethod().getAnnotation(WhereGroup.class);
        if (whereGroup != null) {
            for (WhereJudge whereJudge : whereGroup.value()) {
                setWhereGroup(whereJudge, daoResolveEntity);
            }
        }
    }

    private void setWhereGroup(WhereJudge whereJudge, DaoResolveEntity daoResolveEntity) {
        SqlTokenGroup sqlTokenGroup = new SqlTokenGroup();
        sqlTokenGroup.appendToken(new IfSqlToken(whereJudge.value(), whereJudge.condition()));
        daoResolveEntity.getSqlTool().where(sqlTokenGroup);

    }

    private void setWhere(int index, String string, List<String> parsedMethod, DaoResolveEntity daoResolveEntity) {
        if (DaoKeyWord.And.equals(string)) {
            String f = DaoResolveKit.getField(parsedMethod, daoResolveEntity.getTableInfo(), index + 1);
            ResolvedParam resolvedParam = SQLKit.resolveObject(daoResolveEntity);
            Object arg = daoResolveEntity.getObjects()[resolvedParam.getIndex()];
            String key = " = ", suffix = "";
            if (arg instanceof Collection || arg.getClass().isArray()) {
                key = " in (";
                suffix = " ) ";
            }
            daoResolveEntity.getSqlTool().where(f + key + "<#" + resolvedParam.getName() + "/>" + suffix);
            Integer nextIndex = index + 2;
            setWhere(nextIndex, DaoResolveKit.getField(parsedMethod, nextIndex), parsedMethod, daoResolveEntity);
        }
    }

}
