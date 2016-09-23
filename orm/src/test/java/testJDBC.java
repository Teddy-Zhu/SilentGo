import com.silentgo.orm.SilentGoOrm;
import com.silentgo.orm.base.DBConfig;
import com.silentgo.orm.jdbc.JDBCManager;
import com.silentgo.orm.kit.configKit;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Project : silentgo
 * PACKAGE_NAME
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/21.
 */
public class testJDBC {


    public static void main(String[] args) throws SQLException, InterruptedException {
        DBConfig config = configKit.getConfig("config.properties");
        config.setName("jdbc");
        JDBCManager.getInstance().initial(config);

        int o = 100;
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < o; i++) {
            Thread thread = new Thread(() -> {
                List<Menu> menus = null;
                try {
                    menus = SilentGoOrm.queryList(JDBCManager.getInstance().getConnect("jdbc"), "select * from sys_menu", Menu.class);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println(menus.toString());
                System.out.println("end thread");
            });
            thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.join();
        }
        JDBCManager.getInstance().destory();
    }
}
