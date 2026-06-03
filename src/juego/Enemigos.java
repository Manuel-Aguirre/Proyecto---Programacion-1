package juego;

import java.awt.Color;
import entorno.Entorno;

public class Enemigos {

	private double x;
	private double y;
	private double ancho;
	private double alto;
	private double velocidad;
	private String direccion;

	public Enemigos(Entorno entorno, double camaraX) {
		this.ancho = 30;
		this.alto = 30;
		this.velocidad = 2;

		if (Math.random() < 0.5) {
			this.x = camaraX - ancho;
			this.direccion = "DERECHA";
		} else {
			this.x = camaraX + entorno.ancho() + ancho;
			this.direccion = "IZQUIERDA";
		}

		this.y = 180 + Math.random() * 300;
	}

	public void dibujar(Entorno entorno, double camaraX) {
		entorno.dibujarRectangulo(x - camaraX, y, ancho, alto, 0, Color.BLUE);
	}

	public void mover() {
		if (direccion.equals("DERECHA")) {
			x += velocidad;
		} else {
			x -= velocidad;
		}
	}

	public void subirUnPoco() {
		y = y - 35;

		if (y < 80) {
			y = 80;
		}
	}

	public boolean colisionaConPersonaje(Personaje personaje) {
		return DetectarColisiones.rectanguloConRectangulo(
				x, y, ancho, alto,
				personaje.getX(), personaje.getY(),
				personaje.getAncho(), personaje.getAlto());
	}

	public boolean colisionaConPlataforma(Plataforma plataforma) {
		return DetectarColisiones.rectanguloConRectangulo(
				x, y, ancho, alto,
				plataforma.getX(), plataforma.getY(),
				plataforma.getAncho(), plataforma.getAlto());
	}

	public boolean seFueDePantalla(Entorno entorno, double camaraX) {
		return x < camaraX - 150 || x > camaraX + entorno.ancho() + 150;
	}

	public double bordeDerecho() {
		return x + ancho / 2;
	}

	public double bordeIzquierdo() {
		return x - ancho / 2;
	}

	public double bordeInferior() {
		return y + alto / 2;
	}

	public double bordeSuperior() {
		return y - alto / 2;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getAncho() {
		return ancho;
	}

	public double getAlto() {
		return alto;
	}
}