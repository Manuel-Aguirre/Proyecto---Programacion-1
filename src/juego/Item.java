package juego;

import java.awt.Color;
import entorno.Entorno;

public class Item {

	private double x;
	private double y;
	private double ancho;
	private double alto;
	private String tipo;

	public Item(double x, double y, String tipo) {
		this.x = x;
		this.y = y;
		this.ancho = 15;
		this.alto = 15;
		this.tipo = tipo;
	}

	public void dibujar(Entorno entorno, double camaraX) {
		if (tipo.equals("VIDA")) {
			entorno.dibujarRectangulo(x - camaraX, y, ancho, alto, 0, Color.GREEN);
		} else {
			entorno.dibujarRectangulo(x - camaraX, y, ancho, alto, 0, Color.ORANGE);
		}
	}

	public boolean colisionaConPersonaje(Personaje personaje) {
		return DetectarColisiones.rectanguloConRectangulo(
				x, y, ancho, alto,
				personaje.getX(), personaje.getY(),
				personaje.getAncho(), personaje.getAlto());
	}

	public String getTipo() {
		return tipo;
	}
}