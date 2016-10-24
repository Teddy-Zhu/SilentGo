package com.silentgo.orm.generate;

import com.silentgo.utils.StringKit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Project : parent
 * Package : com.silentgo.orm.generate
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/9/27.
 */
public class Generate {


    private GenerateConfig config;

    public Generate(GenerateConfig config) {
        this.config = config;
    }

    public void GenerateModelAndDao() throws SQLException, ClassNotFoundException, IOException {
        List<TableMeta> tables = new TableMetaGenerate().getTables(config);

        if (tables.size() > 0) {
            File file = new File(config.getOutPath());
            if (!file.isDirectory()) return;
            file.mkdirs();
        }
        for (TableMeta table : tables) {
            String a = new TableModelGenerate().getModelString(table, config.getPackagePath());
            a = a.replace(ClassConst.newline, "\n").replace(ClassConst.tab, "\t");
            File tb = new File(config.getOutPath() + "/" + StringKit.firstToUpper(table.getName()) + ".java");
            if (tb.exists()) {
                tb.delete();
            }
            tb.createNewFile();
            FileWriter fileWritter = new FileWriter(tb, true);
            fileWritter.write(a);
            fileWritter.close();

            String m = new TableDaoGenerate().run(table, config.getPackagePath(), config.getPackagePath());
            m = m.replace(ClassConst.newline, "\n").replace(ClassConst.tab, "\t");
            File file = new File(config.getOutPath() + "/" + StringKit.firstToUpper(table.getName()) + "Dao.java");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileWriter fileWritter2 = new FileWriter(file, true);
            fileWritter2.write(m);
            fileWritter2.close();
        }


    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        GenerateConfig config = new GenerateConfig();
        config.setPackagePath("com.silentgo.test.dao");
        config.setOutPath("/Users/teddyzhu/Downloads/model");
        config.setDriver("com.mysql.cj.jdbc.Driver");
        config.setPass("12345678");
        config.setUser("root");
        config.setDbName("lc4e");
        config.setUrl("jdbc:mysql://localhost:3306/lc4e?useUnicode=true&characterEncoding=utf-8&useSSL=true");
        new Generate(config).GenerateModelAndDao();

    }
}
