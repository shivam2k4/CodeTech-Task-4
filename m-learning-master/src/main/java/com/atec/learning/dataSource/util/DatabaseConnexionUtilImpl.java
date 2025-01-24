package com.atec.learning.dataSource.util;

import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

@Service("dataBaseConnexionUtil")
public class DatabaseConnexionUtilImpl implements DatabaseConnexionUtil{
	
	@Value("${com.atec.recommandation.data.server}")
	protected String server;
	
	@Value("${com.atec.recommandation.data.user}")
	protected String user;
	
	@Value("${com.atec.recommandation.data.password}")
	protected String password;
	
	@Value("${com.atec.recommandation.data.dataBase}")
	protected String dataBase;

	public JDBCDataModel jdbcDataModelConnexion() {
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setServerName(server);
		dataSource.setUser(user);
		dataSource.setPassword(password);
		dataSource.setDatabaseName(dataBase);
		JDBCDataModel dataModel = new MySQLJDBCDataModel(dataSource,
				"user_track", "CUSTOMER", "ItemValue", "UserItemScore", null);
		
		return dataModel;
	}

}
