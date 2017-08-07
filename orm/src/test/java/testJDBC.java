import com.silentgo.utils.ReflectKit;
import com.silentgo.utils.reflect.SGClass;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Project : silentgo
 * PACKAGE_NAME
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by teddyzhu on 16/9/21.
 */
public class testJDBC {


    public String gett() {
        return "";
    }

    public Integer[] aaaa() {
        return new Integer[]{0};
    }

    public List<String> getss() {
        return new ArrayList<>();
    }

    public static void main(String[] args) throws SQLException, InterruptedException, NoSuchMethodException {

       // String[] ccc = new DefaultParameterNameDiscoverer().getParameterNames(Menu.class.getMethod("setCreateTime", Date.class));

        //String[] aaa = new DefaultParameterNameDiscoverer().getParameterNames(VwTopicDetailDao.class.getMethod("queryUserLikeWhereOrderByLimit", String.class, Date.class, boolean.class, boolean.class, String.class, Pager.class));
       // SGClass sgClass = ReflectKit.getSGClass(VwTopicDetailDao.class);
    }
}
