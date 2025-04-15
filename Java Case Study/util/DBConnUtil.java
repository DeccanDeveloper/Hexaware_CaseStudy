public static Connection getDBConn() throws Exception {
    String connStr = DBPropertyUtil.getConnectionString("db.properties");
    return DriverManager.getConnection(connStr);
}
