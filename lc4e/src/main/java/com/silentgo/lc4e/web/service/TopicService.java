package com.silentgo.lc4e.web.service;

import com.silentgo.core.SilentGo;
import com.silentgo.core.db.BaseTableInfo;
import com.silentgo.core.db.DaoFactory;
import com.silentgo.core.db.TableModel;
import com.silentgo.core.ioc.annotation.Inject;
import com.silentgo.core.ioc.annotation.Service;
import com.silentgo.core.plugin.db.bridge.mysql.SQLTool;
import com.silentgo.lc4e.dao.*;
import com.silentgo.orm.Page;

import java.util.List;

/**
 * Created by teddy on 2015/9/23.
 */
@Service
public class TopicService {

    @Inject
    VwTopicPwDao vwTopicPwDao;

    @Inject
    CurUserService curUserService;

    /**
     * filter topic with user like by pw calc
     *
     * @param userId
     * @param page
     * @param size
     * @param userTagPCT
     * @param topicStatusPCT
     * @param commentCountPCT
     * @param curCommentCountPCT
     * @return topic page
     */
    public Page<? extends TableModel> getTopicPw(String userId, int page, int size, double userTagPCT, double topicStatusPCT, double commentCountPCT, double curCommentCountPCT) {

        DaoFactory daoFactory = SilentGo.getInstance().getFactory(DaoFactory.class);
        BaseTableInfo baseTableInfo = daoFactory.getModelTableInfo(VwTopicPw.class);
        BaseTableInfo commentInfo = daoFactory.getModelTableInfo(SysComment.class);
        BaseTableInfo usertopicblockInfo = daoFactory.getModelTableInfo(UserTopicBlocked.class);
        BaseTableInfo userBlockInfo = daoFactory.getModelTableInfo(UserBlocked.class);
        String topicid = baseTableInfo.get("id");
        SQLTool sql = new SQLTool();
        sql.select(baseTableInfo.getTableName(),
                baseTableInfo.get("id")
                , baseTableInfo.get("areaAbbr")
                , baseTableInfo.get("areaName")
                , baseTableInfo.get("title")
                , baseTableInfo.get("author")
                , baseTableInfo.get("authorId")
                , baseTableInfo.get("count")
                , baseTableInfo.get("lastCommentId")
                , baseTableInfo.get("lastCommentOrder")
                , baseTableInfo.get("lastUser")
                , baseTableInfo.get("lastUserNick")
                , baseTableInfo.get("authorAvatar"))
                .leftJoin(commentInfo.getTableName(), commentInfo.get("topicId") + " = " + topicid)
                .where(SQLTool.OR("curTagUser = ?", "ISNULL(curTagUser)")
                        , SQLTool.NOTIN(topicid, SQLTool.SELECT(usertopicblockInfo.get("topicId"))
                                + SQLTool.FROM(usertopicblockInfo.getTableName())
                                + SQLTool.WHERE(usertopicblockInfo.get("userId") + " = ?"))
                        , SQLTool.NOTIN(baseTableInfo.get("authorId"),
                                SQLTool.SELECT(userBlockInfo.get("blockedUserId")) +
                                        SQLTool.FROM(userBlockInfo.getTableName()) + SQLTool.WHERE(userBlockInfo.get("userId") + " = ?"))
                        , commentInfo.get("userId") + " =  ?")
                .groupBy(baseTableInfo.get("id")
                        , baseTableInfo.get("areaAbbr")
                        , baseTableInfo.get("areaName")
                        , baseTableInfo.get("title")
                        , baseTableInfo.get("author")
                        , baseTableInfo.get("authorId")
                        , baseTableInfo.get("count")
                        , baseTableInfo.get("lastCommentId")
                        , baseTableInfo.get("lastCommentOrder")
                        , baseTableInfo.get("lastUser")
                        , baseTableInfo.get("lastUserNick")
                        , baseTableInfo.get("authorAvatar"))
                .orderByDesc(baseTableInfo.get("utPw") + " * ? + " +
                        baseTableInfo.get("tspw") + " * ? + " +
                        baseTableInfo.get("count") + " * ? + " +
                        SQLTool.COUNT(commentInfo.get("id")) + " * ?");
        sql.limit(page, size);
        sql.appendParam(userId, userId, userId, userId, userTagPCT, topicStatusPCT, commentCountPCT, curCommentCountPCT);

        SQLTool countSQL = new SQLTool(sql.getCountSQL(), sql.getParamList());

        int count = vwTopicPwDao.countCustom(countSQL);

        List<VwTopicPw> list = vwTopicPwDao.queryCustom(sql);
        Page<VwTopicPw> result = new Page<>();
        result.setResult(list);
        result.setTotalPage((int) Math.ceil((double) count / (double) size));
        result.setPageNumber(page);
        result.setPageSize(size);
        return result;
    }

    /**
     * all topic in time range(3 month)
     *
     * @param order 1:  by user favorite tag
     *              2: by topic publish time
     *              3: by last reply time
     * @return
     */
    public Page<? extends TableModel> getTopic(int order, int page, int size, double userTagPCT, double topicStatusPCT, double commentCountPCT, double curCommentCountPCT) {
        boolean isLogin;
        page = page < 1 ? 1 : page;
        String userId = "";
        User user = curUserService.getCurrentUser();
        isLogin = user != null;
        if (isLogin) {
            userId = user.getId().toString();
        }
        DaoFactory daoFactory = SilentGo.getInstance().getFactory(DaoFactory.class);
        BaseTableInfo baseTableInfo = daoFactory.getModelTableInfo(VwTopic.class);
        BaseTableInfo usertopicblockInfo = daoFactory.getModelTableInfo(UserTopicBlocked.class);
        BaseTableInfo userBlockInfo = daoFactory.getModelTableInfo(UserBlocked.class);

        SQLTool sql = new SQLTool();
        sql.select(
                baseTableInfo.getTableName(),
                baseTableInfo.get("id")
                , baseTableInfo.get("areaAbbr")
                , baseTableInfo.get("areaName")
                , baseTableInfo.get("title")
                , baseTableInfo.get("author")
                , baseTableInfo.get("authorId")
                , baseTableInfo.get("count")
                , baseTableInfo.get("lastCommentId")
                , baseTableInfo.get("lastCommentOrder")
                , baseTableInfo.get("lastUser")
                , baseTableInfo.get("lastUserNick")
                , baseTableInfo.get("authorAvatar"));
        if (isLogin) {
            sql.where(" AND ", SQLTool.NOTIN(baseTableInfo.get("id"),
                    SQLTool.SELECT(usertopicblockInfo.get("topicId"))
                            + SQLTool.FROM(usertopicblockInfo.getTableName())
                            + SQLTool.WHERE(usertopicblockInfo.get("userId") + " = ? ")),
                    SQLTool.NOTIN(baseTableInfo.get("authorId"),
                            SQLTool.SELECT(userBlockInfo.get("blockedUserId"))
                                    + SQLTool.FROM(userBlockInfo.getTableName())
                                    + SQLTool.WHERE(userBlockInfo.get("userId") + " = ? ")));
            sql.appendParam(userId, userId);
        }
        switch (order) {
            case 1: {
                if (isLogin) {
                    return getTopicPw(userId, page, size, userTagPCT, topicStatusPCT, commentCountPCT, curCommentCountPCT);
                } else {
                    sql.orderByDesc(baseTableInfo.get("pubTime"));
                }
                break;
            }
            case 3: {
                sql.orderByDesc(baseTableInfo.get("lastCommentTime"));
                break;
            }
            default: {
                sql.orderByDesc(baseTableInfo.get("pubTime"));
                break;
            }
        }

        SQLTool countSQL = new SQLTool(sql.getCountSQL(), sql.getParamList());

        int count = vwTopicPwDao.countCustom(countSQL);

        List<VwTopicPw> list = vwTopicPwDao.queryCustom(sql);
        Page<VwTopicPw> result = new Page<>();
        result.setResult(list);
        result.setTotalPage((int) Math.ceil((double) count / (double) size));
        result.setPageNumber(page);
        result.setPageSize(size);

        return result;
    }

}
