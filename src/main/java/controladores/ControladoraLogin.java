package controladores;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelos.Mensaje;
import modelos.Usuario;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



/**
 * Servlet implementation class ControladoraLogin
 */
@WebServlet("/Login")
public class ControladoraLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControladoraLogin() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.invalidate();
		response.sendRedirect("Home");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		HttpSession session = request.getSession();
		RequestDispatcher disp = null;
		
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tecnotiendadb?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "tecnotienda", "tecnotienda");
			final String QUERY = "select * from usuarios where email = ? and clave = ?";
			PreparedStatement ps = con.prepareStatement(QUERY);
			ps.setString(1, email);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			Usuario usuario;
			
			if (rs.next()) {
				usuario = new Usuario(rs.getString("email"), rs.getString("alias"));				
				usuario.setId(rs.getLong("id"));
				usuario.setNombre(rs.getString("nombre"));
				usuario.setApellido(rs.getString("apellido"));
				usuario.setCodigoPostal(rs.getString("codigo_postal"));
				
				
				session.setAttribute("usuario", usuario);
				disp = request.getRequestDispatcher("Home");
			} else {
				Mensaje mensaje;			
				mensaje  = new Mensaje("success", "Usuario/Clave no v�lidas", "Home");
				request.setAttribute("mensaje", mensaje);
				disp = request.getRequestDispatcher("/vistas/mensaje.jsp");
				disp.forward(request, response);
			}
			disp.forward(request, response);
		} catch (Exception e) {
			response.getWriter().append(e.getMessage());
			e.printStackTrace();
		}
	}

}
