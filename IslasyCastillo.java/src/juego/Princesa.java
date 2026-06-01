package juego;

import entorno.Entorno;

public class Princesa {

	double x;
	double y;

	double ancho = 30;
	double alto = 50;

	double velocidadY = 0;

	boolean enPiso = false;

	public Princesa(double x, double y) {

		this.x = x;
		this.y = y;
	}

	public void mover(Entorno entorno, islas2[] islas) {

		double nuevoX = x;
		double nuevoY = y;

		// MOVIMIENTO

		if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {

			nuevoX -= 4;
		}

		if (entorno.estaPresionada(entorno.TECLA_DERECHA)) {

			nuevoX += 4;
		}

		// SALTO

		if (entorno.estaPresionada(entorno.TECLA_ARRIBA) && enPiso) {

			velocidadY = -16;
			enPiso = false;
		}

		// GRAVEDAD

		velocidadY += 0.5;
		nuevoY += velocidadY;

		enPiso = false;

		// COLISIONES

		for (int i = 0; i < islas.length; i++) {

			islas2 isla = islas[i];

			if (isla != null) {

				double izquierdaPersonaje = nuevoX - ancho / 2;
				double derechaPersonaje = nuevoX + ancho / 2;
				double arribaPersonaje = nuevoY - alto / 2;
				double abajoPersonaje = nuevoY + alto / 2;

				double izquierdaIsla = isla.x - isla.ancho / 2;
				double derechaIsla = isla.x + isla.ancho / 2;
				double arribaIsla = isla.y - isla.alto / 2;
				double abajoIsla = isla.y + isla.alto / 2;

				boolean colision =
						derechaPersonaje > izquierdaIsla &&
						izquierdaPersonaje < derechaIsla &&
						abajoPersonaje > arribaIsla &&
						arribaPersonaje < abajoIsla;

				if (colision) {

					// CAER ARRIBA DE LA ISLA

					if (velocidadY > 0 &&
						y + alto / 2 <= arribaIsla) {

						nuevoY = arribaIsla - alto / 2;

						velocidadY = 0;

						enPiso = true;
					}

					// GOLPEAR LA CABEZA

					if (velocidadY < 0 &&
						y - alto / 2 >= abajoIsla) {

						nuevoY = abajoIsla + alto / 2;

						velocidadY = 0;
					}

					// CHOCAR COSTADO IZQUIERDO

					if (x < isla.x &&
						derechaPersonaje > izquierdaIsla &&
						y + alto / 2 > arribaIsla + 5 &&
						y - alto / 2 < abajoIsla - 5) {

						nuevoX = izquierdaIsla - ancho / 2;
					}

					// CHOCAR COSTADO DERECHO

					if (x > isla.x &&
						izquierdaPersonaje < derechaIsla &&
						y + alto / 2 > arribaIsla + 5 &&
						y - alto / 2 < abajoIsla - 5) {

						nuevoX = derechaIsla + ancho / 2;
					}
				}
			}
		}

		// APLICAR MOVIMIENTO

		x = nuevoX;
		y = nuevoY;

		// CAER AL VACÍO

		if (y > 700) {

			// APARECE EN EL CENTRO
			x = 400;
			y = 100;

			// SIEMPRE CON ISLA DEBAJO
			for (int i = 0; i < islas.length; i++) {

				if (islas[i] != null) {

					x = islas[i].x;

					y = islas[i].y - islas[i].alto / 2 - alto / 2;

					break;
				}
			}

			velocidadY = 0;
		}
	}
}

