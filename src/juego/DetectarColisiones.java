package juego;

public class DetectarColisiones {

	public static boolean rectanguloConRectangulo(
			double x1, double y1, double ancho1, double alto1,
			double x2, double y2, double ancho2, double alto2) {

		return Math.abs(x1 - x2) < ancho1 / 2 + ancho2 / 2
				&& Math.abs(y1 - y2) < alto1 / 2 + alto2 / 2;
	}

	public static boolean circuloConRectangulo(
			double circuloX, double circuloY, double radio,
			double rectX, double rectY, double rectAncho, double rectAlto) {

		double xCercano = Math.max(rectX - rectAncho / 2,
				Math.min(circuloX, rectX + rectAncho / 2));

		double yCercano = Math.max(rectY - rectAlto / 2,
				Math.min(circuloY, rectY + rectAlto / 2));

		double diferenciaX = circuloX - xCercano;
		double diferenciaY = circuloY - yCercano;

		return diferenciaX * diferenciaX + diferenciaY * diferenciaY <= radio * radio;
	}
}