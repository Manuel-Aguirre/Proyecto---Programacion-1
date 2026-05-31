package juego;

public class DetectarColisiones {

	// Esta clase la hice para guardar y saber si dos objetos del juego se están tocando.
	public static boolean rectanguloConRectangulo(
			double x1, double y1, double ancho1, double alto1,
			double x2, double y2, double ancho2, double alto2) {

		// Si se estan tocando o no, calculando la distancia.
		// Si los objetos se pisan tanto en X como en Y, hay colision.
		return Math.abs(x1 - x2) < ancho1 / 2 + ancho2 / 2
				&& Math.abs(y1 - y2) < alto1 / 2 + alto2 / 2;
	}

	public static boolean circuloConRectangulo(
			// Sirve esto para detectar si un proyectil circular toca el rectangulo.
			double circuloX, double circuloY, double radio,
			double rectX, double rectY, double rectAncho, double rectAlto) {

		// Busca el punto del rectangulo mas cercano al circulo.
		double xCercano = Math.max(rectX - rectAncho / 2, Math.min(circuloX, rectX + rectAncho / 2));
		double yCercano = Math.max(rectY - rectAlto / 2, Math.min(circuloY, rectY + rectAlto / 2));

		// Calcula la distancia entre el centro del circulo y el punto más cerca del rectangulo.
		double diferenciaX = circuloX - xCercano;
		double diferenciaY = circuloY - yCercano;

		// Si el proyectil llega hasta el rectangulo se considera colision.
		return diferenciaX * diferenciaX + diferenciaY * diferenciaY <= radio * radio;
	}
}