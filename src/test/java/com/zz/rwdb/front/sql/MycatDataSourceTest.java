/**
 * 
 */
package com.zz.rwdb.front.sql;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import com.alibaba.druid.pool.DruidDataSourceFactory;

/**
 * @author Administrator
 *
 */
public class MycatDataSourceTest {

	MycatDataSource proxyDataSource;

	@Before
	public void init() {
		String dbType = "mysql";
		InputStream inputStream = MycatDataSourceTest.class
				.getResourceAsStream("writedb.properties");
		javax.sql.DataSource writeDataSource;
		javax.sql.DataSource readDataSource;
		writeDataSource = getDataSource(readProp(inputStream));

		inputStream = MycatDataSourceTest.class
				.getResourceAsStream("readdb.properties");

		readDataSource = getDataSource(readProp(inputStream));

		proxyDataSource = new MycatDataSource(dbType, writeDataSource,
				readDataSource);
	}

	private Properties readProp(InputStream inputStream) {
		Properties p = new Properties();
		// InputStream inputStream = null;
		try {
			// java应用
			/*
			 * String confile = ""; File file = new File(confile); inputStream =
			 * new BufferedInputStream(new FileInputStream(file));
			 */
			p.load(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return p;
	}

	@Test
	public void readDB() {

		String sql = "select name from t_test where id=?";
		try {
			Connection c;
			c = proxyDataSource.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, "zhu");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String name = rs.getString(1);
				System.out.println(name);
				assertEquals(name, "from test");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void writeDb() {
		String sql = "insert into t_test(id,name) values(?,?)";

		try {
			Connection c;
			c = proxyDataSource.getConnection();
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, "zhu2");
			ps.setString(2, "to hi_db");
			boolean result = ps.execute();
			assertEquals(false, result);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private DataSource getDataSource(Properties properties) {

		DataSource dataSource = null;
		try {
			dataSource = DruidDataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataSource;
	}
}