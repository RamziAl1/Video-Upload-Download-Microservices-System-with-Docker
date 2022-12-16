package com.containorization.upload.dao;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.*;

public class VideoDataDAO {
    private DataSource ds;

    public VideoDataDAO() throws SQLException {
        ds = getDataSource();
    }

    private DataSource getDataSource() throws SQLException {
        MysqlDataSource dn = new MysqlDataSource();
        dn.setServerName("sql-service");
        dn.setDatabaseName("videoDB");
        dn.setUser("root");
        dn.setPassword("123456");
        dn.setUseSSL(false);
        dn.setAllowPublicKeyRetrieval(true);
        return dn;
    }

    public void addVideoData(String videoName, String pathURL) throws SQLException {
        try (Connection conn = ds.getConnection()) {
            String sql = "INSERT INTO videos(video_name, pathURL) VALUES(?,?)";
            PreparedStatement pStmt = conn.prepareCall(sql);
            pStmt.setString(1, videoName);
            pStmt.setString(2, pathURL);
            pStmt.executeUpdate();
        }
    }
}
