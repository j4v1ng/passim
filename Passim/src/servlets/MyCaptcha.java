package servlets;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MyCaptcha extends HttpServlet {

	private static final long serialVersionUID = -3590580494413667064L;
	private int height = 0;
	private int width = 0;	

	public static final String CAPTCHA_KEY = "captcha_key_name";

	public void init(ServletConfig config) throws ServletException {

		super.init(config);

		height = Integer
				.parseInt(getServletConfig().getInitParameter("height"));
		width = Integer.parseInt(getServletConfig().getInitParameter("width"));
		
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws IOException, ServletException {
		// Expire response
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Max-Age", 0);

		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		//Change the color of the background
		int rgb = 0xFFFFFFFF; // white
		for (int i = 0; i < height; i++) {				
			for (int j = 0; j < width; j++) {
				image.setRGB(j, i, rgb);
			}
		}
		
		
		Graphics2D graphics2D = image.createGraphics();
		Hashtable<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
		Random r = new Random();
		String token = Long.toString(Math.abs(r.nextLong()), 36);
		String ch = token.substring(0, 6);
		Color c = new Color(140, 172, 11);
		GradientPaint gp = new GradientPaint(30, 30, c, 15, 25, new Color(191,224,55),
				true);
		graphics2D.setPaint(gp);
		Font font = new Font("Verdana", Font.CENTER_BASELINE, 26);
		graphics2D.setFont(font);
		graphics2D.drawString(ch, 2, 20);
		graphics2D.dispose();

		HttpSession session = req.getSession(true);
		session.setAttribute(CAPTCHA_KEY, ch);

		OutputStream outputStream = response.getOutputStream();
		ImageIO.write(image, "jpeg", outputStream);
		outputStream.close();
	}

}
