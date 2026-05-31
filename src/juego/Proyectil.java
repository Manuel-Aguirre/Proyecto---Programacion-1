package juego;

import java.awt.Color;
import entorno.Entorno;

public class Proyectil {

	private double x;
	private double y;
	private double radio;

	private double velocidadX;
	private double velocidadY;

	private final double VELOCIDAD = 8;

	public Proyectil(double xInicial, double yInicial, double mouseX, double mouseY, double radio) {
		this.x = xInicial;
		this.y = yInicial;
		this.radio = radio;

		double diferenciaX = mouseX - xInicial;
		double diferenciaY = mouseY - yInicial;

		double distancia = Math.sqrt(diferenciaX * diferenciaX + diferenciaY * diferenciaY);

		if (distancia == 0) {
			distancia = 1;
		}

		velocidadX = diferenciaX / distancia * VELOCIDAD;
		velocidadY = diferenciaY / distancia * VELOCIDAD;
	}

	public void mover() {
		x = x + velocidadX;
		y = y + velocidadY;
	}

	public void dibujar(Entorno entorno) {
		entorno.dibujarCirculo(x, y, radio * 2, Color.BLUE);
	}

	public boolean seFueDePantalla(Entorno entorno) {
		return x + radio < 0 || x - radio > entorno.ancho()
				|| y + radio < 0 || y - radio > entorno.alto();
	}

	public boolean colisionaConRectangulo(double rectX, double rectY, double rectAncho, double rectAlto) {
		return DetectarColisiones.circuloConRectangulo(x, y, radio, rectX, rectY, rectAncho, rectAlto);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getRadio() {
		return radio;
	}
}