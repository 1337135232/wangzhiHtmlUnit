package com.wangzhi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @discription jdbc 工具类 为了方便爬虫入库，数据库连接等信息暂时写死，等项目上线就从项目配置文件中读
 * 
 */
public class JdbcUtil {

	// # MySQL
	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true";
	private static String username = "root";
	private static String password = "zyfd";
	
	

	static {
		/**
		 * 以下为正式环境数据库配置文件读取
		 * */
		 try {
			 Class.forName(driver);
		 } catch (Exception e1) {
		 // TODO Auto-generated catch block
			 e1.printStackTrace();
		 }
	
	}

	/**
	 * 获取连接
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}

	/**
	 * 关闭连接
	 * @param conn
	 * @param stm
	 * @param rs
	 * @throws SQLException
	 */
	public static void close(Connection conn, Statement stm, ResultSet rs)
			throws SQLException {
		if (rs != null) {
			rs.close();
		}
		if (stm != null) {
			stm.close();
		}
		if (conn != null) {
			conn.close();
		}

	}

	/**
	 * @author huangyee
	 * @discription 批量sql插入
	 * @param sql
	 * @throws SQLException
	 */
	public static void executeSqlList(ArrayList<String> sql)
			throws SQLException {
		ArrayList<String> tmp = new ArrayList<String>();
		Connection conn = null;
		Statement stm = null;
		try {
			int flag = 0;
			conn = getConnection();
			stm = conn.createStatement();
			conn.setAutoCommit(false);// 关闭事务自定提交
			for (int i=0;i<sql.size();i++) {
				// System.out.println("DEBUG:insert sql: "+sql);
				flag++;
				stm.addBatch(sql.get(i));
				tmp.add(sql.get(i));
				if (flag == 300) {
					try {
						stm.executeBatch();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//将tmp中的语句写入文件
						insertErroFile(tmp);
						tmp.clear();
					}
					conn.commit();
					tmp.clear();
					flag = 0;
				}
			}
			if (flag > 0) {
				stm.executeBatch();
				conn.commit();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			conn.rollback();
			//将tmp中的语句写入文件
			insertErroFile(tmp);
			tmp.clear();
			throw e;
		} finally {
			sql.clear();
			close(conn, stm, null);
		}
	}

	public static void insertErroFile(ArrayList<String> sqlList){
//		File tmpFolder = new File(SysParamsFactory.getSystemParams(Constant.ERROR_LOG_PATH));
		File tmpFolder = new File("C://tmp//ErrorFile");
		if (!tmpFolder.exists())
			tmpFolder.mkdirs();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String date = df.format(new Date());
		try {
//			FileWriter writer = new FileWriter(SysParamsFactory.getSystemParams(Constant.ERROR_LOG_PATH)+date+".txt",true);
			FileWriter writer = new FileWriter(tmpFolder+"//"+date+".txt",true);
			String content = "";
			for(String sql:sqlList){
				content += sql+";"+"\n";
			}
			writer.write(content);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
		}
	}

	/**
	 * @discription 执行单条sql，如插入
	 * @param sql
	 */
	public static void executeSql(String sql) {
		Connection conn = null;
		Statement stm = null;
		try {
			conn = getConnection();
			stm = conn.createStatement();
			conn.setAutoCommit(false);
			stm.executeUpdate(sql);
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				close(conn, stm, null);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * 执行查询sql，讲结果封装在map里面，map的key是列名，value是值
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList<HashMap<String,String>> query(String sql) throws SQLException{
		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			while(rs.next()){
				ResultSetMetaData rsmd = rs.getMetaData();
				HashMap<String,String> map = new HashMap<String, String>();
				int columnCount = rsmd.getColumnCount();
				for(int i =1 ; i<=columnCount ;i++){
					String columnName = rsmd.getColumnName(i);
					map.put(columnName, rs.getString(columnName));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}finally{
			close(conn, stm, rs);
		}
		
		return list;
	}
	
	public static void main(String[] args) throws SQLException {
		Connection conn = getConnection();
		System.out.println(conn);
	}
}
 