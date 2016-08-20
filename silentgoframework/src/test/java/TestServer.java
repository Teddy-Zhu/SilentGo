/**
 * Project : silentgo
 * PACKAGE_NAME
 *
 * @author    <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 * <p>
 * Created by  on 16/7/18.
 */
public class TestServer {

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.start("/");
    }
}
