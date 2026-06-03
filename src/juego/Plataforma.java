package juego;

import java.awt.Color;
import entorno.Entorno;

public class Plataforma {

	private double x;
	private double y;
	private double ancho;
	private double alto;

	public Plataforma(double x, double y, double ancho, double alto) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
	}

	public void dibujar(Entorno entorno, double camaraX) {
		Color verdePasto = new Color(40, 170, 40);
		entorno.dibujarRectangulo(x - camaraX, y, ancho, alto, 0, verdePasto);
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