package com.lianjia;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 
 
public class MysqlDemo {
    public static void main(String[] args) throws Exception {
        Connection conn = null;
        String sql;
        // MySQL��JDBC URL��д��ʽ��jdbc:mysql://�������ƣ����Ӷ˿�/���ݿ������?����=ֵ
        // ������������Ҫָ��useUnicode��characterEncoding
        
        String url = "jdbc:mysql://172.27.4.49:6521/lianjia?useUnicode=true&characterEncoding=UTF8&autoReconnect=true&failOverReadOnly=false";
        String username = "root";
        String password = "lianjia";
        
        //String url = "jdbc:mysql://172.16.6.144:3306/lianjia?useUnicode=true&characterEncoding=UTF8&autoReconnect=true&failOverReadOnly=false";
        //String username = "web_w";
        //String password = "123456";        
        
        try {
            // ֮����Ҫʹ������������䣬����ΪҪʹ��MySQL����������������Ҫ��������������
            // ����ͨ��Class.forName�������ؽ�ȥ��Ҳ����ͨ����ʼ������������������������ʽ������
            //Class.forName("com.mysql.jdbc.Driver");// ��̬����mysql����
            // or:
            com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();
            // or��
            // new com.mysql.jdbc.Driver();
 
            System.out.println("�ɹ�����MySQL��������");
            // һ��Connection����һ�����ݿ�����
            conn = DriverManager.getConnection(url, username, password);
            // Statement������кܶ෽��������executeUpdate����ʵ�ֲ��룬���º�ɾ����
            Statement stmt = conn.createStatement();
            sql = "select * from baike_article";
            ResultSet rs = stmt.executeQuery(sql);// executeQuery�᷵�ؽ���ļ��ϣ����򷵻ؿ�ֵ
            System.out.println("ѧ��\t���� = " + rs.getRow());
            while (rs.next()) {
                 System.out.println(rs.getString(1) + "\t" + rs.getString(2));// ��������ص���int���Ϳ�����getInt()
            }
        } catch (SQLException e) {
            System.out.println("MySQL��������");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
 
    }
 
}