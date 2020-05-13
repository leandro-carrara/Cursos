package br.com.boavista;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
public class CSV {
    private static final String SQL_INSERT = "INSERT INTO participantes ( " +
        " id , treinamento , nome , email , area) " +
        " VALUES (?, ?, ?, ?, ?)"; 
    
    public static void main(String[] args) {
        System.out.println("Iniciando processamento...");
        FileReader reader = null;
        BufferedReader buffer = null;
        Connection con = null;
        try {
            reader = new FileReader("c:/temp/TreinamentosRealizados.CSV");
            buffer = new BufferedReader(reader);
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:chamada.db");
                    
            Statement sqlDDL = con.createStatement();
            
            sqlDDL.executeUpdate("drop table if exists participantes");
            sqlDDL.executeUpdate("create table participantes ( id string, treinamento string, nome string, email string, area string)");
            PreparedStatement sqlInsert = con.prepareStatement(SQL_INSERT);
            
            String registro = buffer.readLine();
            int i = 0;
            
            while (registro != null) {
            	if (i <= 0) {
            		registro = buffer.readLine();
            	}
            	
                String[] dados = registro.split(";");
                
                sqlInsert.setString(1, dados[0]);
                sqlInsert.setString(2, dados[1]);
                sqlInsert.setString(3, dados[2]);
                sqlInsert.setString(4, dados[3]);
                sqlInsert.setString(5, dados[4]);
//                sqlInsert.setString(6, dados[5]);
                
                System.out.println("SqlInsert " + sqlInsert.toString());
                sqlInsert.executeUpdate();
                
                
                i ++;
                registro = buffer.readLine();
            }
        } catch(Exception e) {
            System.out.println("Erro ao processar arquivo!\n" + e.getMessage());
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (Exception e) {
                    System.out.println("Erro ao fechar arquivo de dados!\n" + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    System.out.println("Erro ao fechar conexão com o banco de dados! \n" + e.getMessage());
                }
            }
            System.out.println("Processamento concluído!");
        }           
    }
}