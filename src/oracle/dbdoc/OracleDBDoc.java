package oracle.dbdoc;
import java.awt.*;

import javax.swing.*;

import java.sql.*;
import java.util.*;
import java.io.*;

public class OracleDBDoc extends JFrame{
	
	/**
	 * @author: Mitodru Niyogi
	 */
	private static final long serialVersionUID = 1L;
	JLabel l;
	Connection con;
	Vector<String> tablenames;
	Vector<String> fields;
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	//																								//
	//							fill in the db name here											//
	//																								//
	//////////////////////////////////////////////////////////////////////////////////////////////////
	
	final static String dbName ="cl";
	final static String server ="localhost";
	final static String username ="system";
	final static String password ="tiger";
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	//																								//
	//																								//
	//																								//
	//////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	final static boolean slow=true;
	
	public OracleDBDoc(){
		super("Database documentation generator");
		setBounds(200,125,350,250);
		Container c=getContentPane();
		l=new JLabel("Connecting to database..");
		c.add(l);
		update(getGraphics());
	}
	
	void dothings(){

		try{if(slow)Thread.sleep(1000);}catch(InterruptedException exc){}
		
		connect();
		l.setText("Connection Successful... About to query for tables...");
		try{if(slow)Thread.sleep(1000);}catch(InterruptedException exc){}
		
		loadTableNames();
		writeDBfile();
		
		l.setText("Database file writing successful.");
		try{if(slow)Thread.sleep(1000);}catch(InterruptedException exc){}
		for(int i=0;i<tablenames.size();i++){
			writetablefile(i);
		
			System.out.println((String)tablenames.elementAt(i));
			
			l.setText("wrote file for table: "+(String)tablenames.elementAt(i));
			try{if(slow)Thread.sleep(1000);}catch(InterruptedException exc){}
		}
			
		l.setText("<html>Everything done successfully!!! <br>exiting in 5 seconds</html>");
		try{if(slow)Thread.sleep(5000);}catch(InterruptedException exc){}
		System.exit(0);
		
		


	}

	public Dimension getPreferredSize(){
		return new Dimension(350,125);
	}
	
	private static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        OracleDBDoc frame = new OracleDBDoc();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.dothings();

    }
    
    public static void main(String args[]){
	    createAndShowGUI();
    }

    public void connect(){
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe",username,password);
		}catch(SQLException e){
			l.setText("<html>Could not connect to database <br>exiting in 5 seconds</html>");
			try{if(slow)Thread.sleep(5000);}catch(InterruptedException exc){}
			System.exit(1);			
		}
		catch(ClassNotFoundException e){
			l.setText("<html>Driver not found<br>exiting in 5 seconds</html>");
			try{if(slow)Thread.sleep(5000);}catch(InterruptedException exc){}
			System.exit(1);		}
	}
	
	public void loadTableNames(){
		try{
			PreparedStatement ps=con.prepareStatement("select tablespace_name, table_name from user_tables");
			System.out.println("After table_name");
			ResultSet rs=ps.executeQuery();
			System.out.println("After executing table_name");
			l.setText("loaded table names");
			String tmp="";
			tablenames=new Vector<String>();
			while(rs.next()){
				tmp=rs.getString("table_name");
				tablenames.add(tmp);
			}
			System.out.println("No Sql Exception");
			try{if(slow)Thread.sleep(1000);}catch(InterruptedException exc){}
			l.setText("saved "+tablenames.size()+" table names");
			try{if(slow)Thread.sleep(1000);}catch(InterruptedException exc){}
			l.setText("trying to save database index file");


		}catch(SQLException t){
			l.setText("<html>Cannot load table names<br>exiting in 5 seconds</html>");
			try{if(slow)Thread.sleep(5000);}catch(InterruptedException exc){}
			System.exit(1);		}
	}
	
	void writeDBfile(){
		BufferedWriter out=null;
		try{
			out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("database.html")));
			
			out.write("<html><head><title>Database Unknown: Documentation</title>\r\n");
			out.write("<style>\r\n");
			out.write("a{}\r\n");
			out.write("a.top { font-family: Arial; font-size:14pt; font-weight:bold; color=\"#000000\"}\r\n");
			out.write("font.top { font-family: Lucida Sans; font-size:14pt; font-weight:bold; color=\"#000000\"}\r\n");
			out.write("</style></head>\r\n");
			out.write("<body>\r\n");
			out.write("<table><tr bgcolor=\"#ddddff\"><td bgcolor=\"#eeeeff\">\r\n");
			out.write("<font class=top>Database-Overview</font></td><td width=\"50%\">\r\n");
			out.write("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\r\n");
			out.write("<a class=top href=\""+(String)tablenames.elementAt(0)+".html\">Tables</a>\r\n");
			out.write("</td></tr></table>\r\n");
			out.write("<h2>Database dbName</h2>\r\n");
			out.write("<i>Give a brief  description of the db here...</i>\r\n");
			out.write("<table border=1><tr><td width=10% bgcolor=\"#bbbbff\" colspan=2>\r\n");
			out.write("<font size=\"5\"><b>Tables in the database.</b></font>\r\n");
			out.write("</td></tr>\r\n");
			int nooftab=tablenames.size();
			
			System.out.println(nooftab);
			
			for(int i=0;i<nooftab;i++){
				out.write("<tr><td><a href=\""+(String)tablenames.elementAt(i)+".html\">"+(String)tablenames.elementAt(i)+"</a></td>\r\n");
				out.write("<td>A brief description of the table</td></tr>\r\n");
			}
			out.write("</table></body></html>\r\n");
			l.setText("Finished writing database index file");
			try{if(slow)Thread.sleep(1000);}catch(InterruptedException exc){}
			
			
		}catch(IOException d){
			l.setText("<html>Could not save file, or IO problem <br>exiting in 5 seconds</html>");
			try{if(slow)Thread.sleep(5000);}catch(InterruptedException exc){}
			System.exit(1);		}
		finally{
			try{
			out.flush();
			out.close();
		}catch(IOException g){
			l.setText("<html>Could not flush files.<br>exiting in 5 seconds</html>");
			try{if(slow)Thread.sleep(5000);}catch(InterruptedException exc){}
			System.exit(1);
			}
		}
		
	}
	
	void writetablefile(int y){
		String tabname=(String)tablenames.elementAt(y);
		//System.out.println(tabname);
		boolean prev=true;
		boolean next=true;
		if(y==0)prev=false;
		if(y==(tablenames.size()-1))next=false;
		fields=new Vector<String>();
		BufferedWriter out=null;
		try{
			out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tabname+".html")));
			
			out.write("<html><head><title>Table: UserAddress- documentation</title>\r\n");
			out.write("<style>\r\n");
			out.write("a{}\r\n");
			out.write("a.top { font-family: Arial; font-size:14pt; font-weight:bold; color=\"#000000\"}\r\n");
			out.write("</style></head><body>\r\n");
			out.write("<table>\r\n");
			out.write("<tr bgcolor=\"#ddddff\"><td width=\"50%\">\r\n");
			out.write("<a class=top href=\"database.html\">Database-Overview</a>\r\n");
			out.write("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><font size=\"5\" face=\"Lucida Sans\"><span style=\"background-color: #EEEEFF\">Tables</span></font></b>&nbsp;&nbsp;&nbsp;\r\n");
			out.write("</td></tr></table>\r\n");
			out.write("<font size=2>\r\n");
			
			if(prev)			out.write("<a href=\""+(String)tablenames.elementAt(y-1)+".html\">"+"PREV table"+"</a>\r\n");
			else 				out.write("PREV table\r\n");
			if(next)			out.write("<a href=\""+(String)tablenames.elementAt(y+1)+".html\">"+"NEXT table"+"</a>\r\n");
			else 				out.write("NEXT table\r\n");

			out.write("</font>\r\n");
			out.write("<p><h2>Table "+tabname+"</h2><p>\r\n");
			out.write("<pre>   <a href=\"database.html\">Database </a>\r\n");
			out.write("   |\r\n");
			out.write("   +--"+tabname+"\r\n");
			out.write("</pre><hr>\r\n");
			out.write("<p><h4>"+tabname+"</h4>\r\n");
			out.write("<p>This table is used to store the followinng details about the student.\r\n");
			out.write("<p><hr><table border=1><tr><td width=10% bgcolor=\"#bbbbff\" colspan=2>\r\n");
			out.write("<font size=\"5\"><b>Fields Summary</b></font></td></tr>\r\n");
			
		ResultSet rs=null;
			try{
			//	System.out.println(" Desc exception");
				
				PreparedStatement ps=con.prepareStatement("select * from "+tabname);
				rs=ps.executeQuery();
				
				System.out.println("No Desc exception");
				while(rs.next()){
					System.out.println("No Field exception");
				String fn=rs.getString("Name");
					out.write("<tr><td><a href=\"#"+fn+"\">"+fn+"</a></td><td>This field is used to store ...  of the student.</td></tr>\r\n");
				}		
				
			//	System.out.println("No Field exception");
				
			}catch(Exception e){
				l.setText("<html>Could not retrieve field details<br>exiting in 5 seconds</html>");
				try{if(slow)Thread.sleep(5000);}catch(InterruptedException exc){}
				System.exit(1);
			}
		
			
			out.write("</table><hr><p><br>\r\n");
			out.write("<table border=1><tr><td width=10% bgcolor=\"#bbbbff\" colspan=2><font size=\"5\"><b>Fields Details.</b></font></td></tr></table>\r\n");
			out.write("\r\n");
			
			
			try{
				//rs.beforeFirst();
				while(rs.next()){
					System.out.println("Inside fields");
					String fn=rs.getString("Name");
					String type=rs.getString("Type");
					String maxlen="";
					if(type.indexOf('(')!=-1){
						 maxlen=type.substring(type.indexOf(    '('      )+1, type.indexOf(  ')'   )  );
						type=type.substring(0,type.indexOf('('));
					}
					
					out.write("<a name=\""+fn+"\">\r\n");
					out.write("<h3>"+fn+"</h3>\r\n")
					;
				 
				
				 
					out.write("<p>&nbsp;</p><ol>\r\n");
					out.write("This field stores the ... of the student \r\n");
					out.write("</ol>\r\n");
					out.write("<font size=\"4\"><b>Type:</b></font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\r\n");
				
					
					
					out.write(type+"\r\n<p>");
					if(maxlen.length()>0){
						out.write("<font size=\"4\"><b>Maximum field length:</b></font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+maxlen+"\r\n");
					}
					
					out.write("<h4>Used By</h4><ol>Bean1<br>Bean2</ol>\r\n");
					out.write("<hr>\r\n");
					out.write("\r\n");
				}	
			}catch(SQLException e){
			l.setText("<html>Problem in getting field properties.<br>exiting in 5 seconds</html>");
			try{if(slow)Thread.sleep(5000);}catch(InterruptedException exc){}
			System.exit(1);			}	

			out.write("<p>\r\n");
			out.write("<font size=\"1\">\r\n");
			out.write("Document created by <b>Mitodru Niyogi</b>  \r\n");
			out.write("<br>A part of the project of Software  Lab, GCECT Kolkata.\r\n");
			out.write("</body>\r\n");
			out.write("</html>\r\n");
			out.write("\r\n");
			out.write("\r\n");
			out.write("\r\n");
			
		}
		catch(IOException d){
			l.setText("<html>IO problem<br>exiting in 5 seconds</html>");
			try{if(slow)Thread.sleep(5000);}catch(InterruptedException exc){}
			System.exit(1);
		}
		finally{
			try{
			out.flush();
			out.close();
		}catch(IOException g){}
		}
		
	}
	
	   
}

	