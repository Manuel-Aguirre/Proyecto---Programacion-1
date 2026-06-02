package juego;

import java.awt.Color;
import entorno.Entorno;

public class HUD {

	public void dibujarVidas(Entorno entorno, Personaje personaje) {
		entorno.cambiarFont("Arial", 20, Color.BLACK);
		entorno.escribirTexto("Vidas: " + personaje.getVidas(), 20, 30);

		for (int i = 0; i < personaje.getVidas(); i++) {
			entorno.dibujarRectangulo(30 + i * 22, 50, 16, 16, 0, Color.RED);
		}
	}

	public void dibujarContadores(Entorno entorno, Personaje personaje) {
		entorno.cambiarFont("Arial", 16, Color.BLACK);
		entorno.escribirTexto("Disparo triple: " + personaje.getDisparosParaTriple() + "/10", 20, 80);
		entorno.escribirTexto("Disparo plus: " + personaje.getDisparosPlus(), 20, 105);
	}

	public void mostrarPantallaPerdiste(Entorno entorno) {
		entorno.cambiarFont("Arial", 34, Color.BLACK);
		entorno.escribirTexto("Juego terminado: Perdiste!", 190, 260);

		entorno.cambiarFont("Arial", 20, Color.BLACK);
		entorno.escribirTexto("Presione ENTER para reintentar el juego", 200, 310);
	}

	public void mostrarPantallaGanaste(Entorno entorno) {
		entorno.cambiarFont("Arial", 40, Color.BLACK);
		entorno.escribirTexto("GANASTE!", 300, 280);
	}
}