package juego;

import java.awt.Color;
import java.util.Random;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {

	private Entorno entorno;

	private islas2[] islas;

	private Princesa princesa;

	private Random random;

	private double camaraX = 0;

	private double castilloX = 2200;
	private double castilloY = 480;

	private boolean juegoGanado = false;

	// CONSTRUCTOR

	public Juego() {

		entorno = new Entorno(
				this,
				"Islas flotantes",
				800,
				600);

		random = new Random();

		islas = new islas2[25];

		crearMapa();

		princesa = new Princesa(100, 100);

		entorno.iniciar();
	}

	// CREAR MAPA

	public void crearMapa() {

		int indice = 0;

		// PISO

		for (int x = 100; x <= 2100; x += 200) {

			islas[indice] = new islas2(
					x,
					560,
					120,
					20);

			indice++;
		}

		// ISLAS FLOTANTES

		for (int i = indice; i < islas.length; i++) {

			int ancho = random.nextInt(120) + 80;

			int x = random.nextInt(1800) + 200;

			int nivel = random.nextInt(3);

			int y;

			if (nivel == 0) {

				y = 420;
			}
			else if (nivel == 1) {

				y = 300;
			}
			else {

				y = 180;
			}

			islas[i] = new islas2(
					x,
					y,
					ancho,
					20);
		}
	}

	// TICK

	public void tick() {

		// MOVER PERSONAJE

		if (!juegoGanado) {

			princesa.mover(entorno, islas);
		}

		// CAMARA

		camaraX = princesa.x - 400;

		if (camaraX < 0) {

			camaraX = 0;
		}

		// FONDO

		entorno.dibujarRectangulo(
				400,
				300,
				1600,
				1200,
				0,
				Color.CYAN);

		// DIBUJAR ISLAS

		for (int i = 0; i < islas.length; i++) {

			islas2 isla = islas[i];

			if (isla != null) {

				entorno.dibujarRectangulo(
						isla.x - camaraX,
						isla.y,
						isla.ancho,
						isla.alto,
						0,
						Color.GRAY);
			}
		}

		// DIBUJAR CASTILLO

		entorno.dibujarRectangulo(
				castilloX - camaraX,
				castilloY,
				120,
				160,
				0,
				Color.RED);

		// DIBUJAR PERSONAJE

		entorno.dibujarRectangulo(
				400,
				princesa.y,
				princesa.ancho,
				princesa.alto,
				0,
				Color.PINK);

		// GANAR

		boolean choqueCastillo =
				princesa.x + princesa.ancho / 2 >
				castilloX - 60 &&

				princesa.x - princesa.ancho / 2 <
				castilloX + 60 &&

				princesa.y + princesa.alto / 2 >
				castilloY - 80 &&

				princesa.y - princesa.alto / 2 <
				castilloY + 80;

		if (choqueCastillo) {

			juegoGanado = true;
		}

		// TEXTO GANASTE

		if (juegoGanado) {

			entorno.cambiarFont(
					"Arial",
					40,
					Color.BLACK);

			entorno.escribirTexto(
					"GANASTE",
					280,
					200);
		}
	}

	// MAIN

	public static void main(String[] args) {

		new Juego();
	}
}