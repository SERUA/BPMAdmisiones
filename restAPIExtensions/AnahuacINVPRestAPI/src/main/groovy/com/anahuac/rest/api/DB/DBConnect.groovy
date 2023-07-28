package com.anahuac.rest.api.DB

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

import javax.naming.Context
import javax.naming.InitialContext
import javax.sql.DataSource

import org.slf4j.Logger
import org.slf4j.LoggerFactory


class DBConnect {
	private static final Logger LOGGER = LoggerFactory.getLogger(DBConnect.class);
	
	/* The data source. /
	 
		 /**
		  * Gets the connection.
		  *
		  * @return the connection
		  * @throws NullPointerException the null pointer exception
		  */
		 /**
		  * Gets the connection.
		  *
		  * @return the connection
		  * @throws NullPointerException the null pointer exception
		  */
		 public final Connection getConnection() throws Exception {
			 Context initContext = new InitialContext();
			 //BDM
			 DataSource dataSource = (DataSource) initContext.lookup("java:/comp/env/NotManagedBizDataDS");
			 return dataSource.getConnection();
		 }
		 public final Connection getConnectionBonita() throws Exception {
			 Context initContext = new InitialContext();
			 //Bonita instancias etc...
			 DataSource dataSource = (DataSource) initContext.lookup("java:/comp/env/bonitaSequenceManagerDS");
			 return dataSource.getConnection();
		 }
		 public void closeObj(Connection con, Statement stm, ResultSet rs, PreparedStatement pstm) {
			 try {
				 if (null != rs) {
					 rs.close();
				 }
				 if (null != stm) {
					 stm.close();
				 }
				 if (null != con) {
					 con.setAutoCommit(true);
					 con.close();
				 }
				 if (null != pstm) {
					 pstm.close();
				 }
			 } catch (Exception e) {
				 //LOGGER.error e.getMessage();
			 }
		 }
	
}
