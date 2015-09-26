import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class OutputServlet extends HttpServlet {
 //Connection c;

  public void doGet(HttpServletRequest req, HttpServletResponse res)
                               throws ServletException, IOException {
Connection c;
try
	{
    			Class.forName("oracle.jdbc.driver.OracleDriver");		
		c = DriverManager.getConnection("jdbc:Oracle:thin:@localhost:1521:XE","system","manager");
			res.setContentType("text/plain");
    			PrintWriter out = res.getWriter();
			HttpSession session = req.getSession();
    			String args = (String)session.getAttribute("table");
			String op=req.getParameter("opfile");
			File file = new File(op);
			if (!file.exists()) 			
				file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
	
		int p,i;
		//System.out.println(args);		
		
		StringBuffer sb = new StringBuffer("Select * from ");
		sb.append(args);
		String s = new String(sb);
		//System.out.println(s);
		PreparedStatement ps = c.prepareStatement(s);		
		ps.execute();
		ResultSet rs = ps.getResultSet();
		ResultSetMetaData rsmd = rs.getMetaData();
		int ncol = rsmd.getColumnCount();
				
		while(rs.next())
		{
			
			for(i=1;i<ncol;i++)
			{
				String sp = rs.getString(i);
				bw.write(sp);
				bw.write(",");
			}
			String pd = rs.getString(i);
			bw.write(pd);
			bw.newLine();	
		}	
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	res.sendRedirect("localhost:8081/miniproject/index.html#end");
}
}