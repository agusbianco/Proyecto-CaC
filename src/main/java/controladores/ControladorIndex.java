package controladores;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelos.Producto;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;



/**
 * Servlet implementation class ControladorIndex
 */
@WebServlet("/Home")
public class ControladorIndex extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControladorIndex() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher disp = null;		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tecnotiendadb?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "tecnotienda", "tecnotienda");
			final String QUERY = "select * from productos where destacado limit 3"; //es equivalente a destacado = true
			PreparedStatement ps = con.prepareStatement(QUERY);
			ResultSet rs = ps.executeQuery();
			
			List<Producto> productosDestacados = new ArrayList<Producto>();
			Producto producto;
			while (rs.next()) {
				producto = new Producto(rs.getLong("id"), rs.getString("nombre"));
				producto.setPrecio(rs.getDouble("precio"));
				producto.setImg(rs.getString("img"));
				producto.setDescripcion(rs.getString("descripcion"));
				producto.setStock(rs.getInt("stock"));
				producto.setDestacado(rs.getBoolean("destacado"));
				producto.setCategoria(rs.getString("categoria"));
				productosDestacados.add(producto);

			}				
			request.setAttribute("productosDestacados", productosDestacados);				
			disp = request.getRequestDispatcher("index.jsp");
			disp.forward(request, response);
			
		} catch (Exception e) {
			response.getWriter().append(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
