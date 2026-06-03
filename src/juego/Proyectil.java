package juego;

import java.awt.Color;
import entorno.Entorno;

public class Proyectil {

	private double x;
	private double y;
	private double radio;

	private double velocidadX;
	private double velocidadY;

	private boolean atraviesa;

	private final double VELOCIDAD = 8;

	public Proyectil(double xInicial, double yInicial, double mouseX, double mouseY, double radio) {
		this.x = xInicial;
		this.y = yInicial;
		this.radio = radio;
		this.atraviesa = false;

		double diferenciaX = mouseX - xInicial;
		double diferenciaY = mouseY - yInicial;

		double distancia = Math.sqrt(diferenciaX * diferenciaX + diferenciaY * diferenciaY);

		if (distancia == 0) {
			distancia = 1;
		}

		velocidadX = diferenciaX / distancia * VELOCIDAD;
		velocidadY = diferenciaY / distancia * VELOCIDAD;
	}

	public void hacerAtravesador() {
		atraviesa = true;
	}
	
	public void hacerMasLento() {
		velocidadX = velocidadX * 0.55;
		velocidadY = velocidadY * 0.55;
	}

	public boolean esAtravesador() {
		return atraviesa;
	}

	public void mover() {
		x = x + velocidadX;
		y = y + velocidadY;
	}

	public void dibujar(Entorno entorno, double camaraX) {
		if (atraviesa) {
			entorno.dibujarCirculo(x - camaraX, y, radio * 2, Color.ORANGE);
		} else {
			entorno.dibujarCirculo(x - camaraX, y, radio * 2, Color.BLUE);
		}
	}

	public boolean seFueDePantalla(Entorno entorno, double camaraX) {
		return x + radio < camaraX
				|| x - radio > camaraX + entorno.ancho()
				|| y + radio < 0
				|| y - radio > entorno.alto();
	}

	public boolean colisionaConEnemigo(Enemigos enemigo) {
		if (enemigo == null) {
			return false;
		}

		return DetectarColisiones.circuloConRectangulo(
				x, y, radio,
				enemigo.getX(), enemigo.getY(),
				enemigo.getAncho(), enemigo.getAlto());
	}

	public boolean colisionaConPlataforma(Plataforma plataforma) {
		return DetectarColisiones.circuloConRectangulo(
				x, y, radio,
				plataforma.getX(), plataforma.getY(),
				plataforma.getAncho(), plataforma.getAlto());
	}
}