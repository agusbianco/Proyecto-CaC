package controladores;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelos.Producto;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


/**
 * Servlet implementation class ControladoraProducto
 */
@WebServlet("/Productos")
public class ControladoraProducto extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControladoraProducto() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session= request.getSession(true);
		RequestDispatcher disp = null;		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tecnotiendadb?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "tecnotienda", "tecnotienda");
			final String QUERY = "select * from productos"; //es equivalente a destacado = true
			PreparedStatement ps = con.prepareStatement(QUERY);
			ResultSet rs = ps.executeQuery();
			
			List<Producto> productos = new ArrayList<Producto>();
			Producto producto;
			while (rs.next()) {
				producto = new Producto(rs.getLong("id"), rs.getString("nombre"));
				producto.setPrecio(rs.getDouble("precio"));
				producto.setImg(rs.getString("img"));
				producto.setDescripcion(rs.getString("descripcion"));
				producto.setStock(rs.getInt("stock"));
				producto.setDestacado(rs.getBoolean("destacado"));
				producto.setCategoria(rs.getString("categoria"));
				
				productos.add(producto);

			}
			session.setAttribute("productos", productos);				
			disp = request.getRequestDispatcher("/vistas/products.jsp");
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
