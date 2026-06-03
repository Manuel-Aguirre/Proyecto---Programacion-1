package juego;

import entorno.Entorno;

public class Mapa {

	private Plataforma[] plataformas;

	public Mapa() {
		this.plataformas = new Plataforma[70];
		crearMapa();
	}
	
	private void crearMapa() {
		int indice = 0;

		plataformas[indice] = new Plataforma(400, 580, 620, 40);
		indice++;

		for (int x = 980; x <= 5900 && indice < plataformas.length; x += 420) {
			plataformas[indice] = new Plataforma(x, 580, 340, 40);
			indice++;
		}

		plataformas[indice] = new Plataforma(6100, 580, 420, 40);
		indice++;

		plataformas[indice] = new Plataforma(6500, 580, 600, 40);
		indice++;

		double xIsla = 650;
		int alturaAnterior = 490;

		while (indice < plataformas.length && xIsla < 6100) {

			xIsla += 230 + Math.random() * 260;

			int nivel = (int) (Math.random() * 4);
			int y;

			if (nivel == 0) {
				y = 485;
			} else if (nivel == 1) {
				y = 440;
			} else if (nivel == 2) {
				y = 410;
			} else {
				y = 390;
			}

			if (Math.abs(y - alturaAnterior) > 80) {
				if (y < alturaAnterior) {
					y = alturaAnterior - 60;
				} else {
					y = alturaAnterior + 60;
				}
			}

			double ancho = 120 + Math.random() * 110;

			plataformas[indice] = new Plataforma(xIsla, y, ancho, 25);
			indice++;

			alturaAnterior = y;

			if (Math.random() < 0.25) {
				xIsla += 120 + Math.random() * 120;
			}
		}
	}
	
	public void revisarPlataformas(Personaje personaje) {
		boolean estaSobreAlguna = false;

		for (int i = 0; i < plataformas.length; i++) {
			if (plataformas[i] != null) {

				if (personaje.estaSobrePlataforma(plataformas[i])) {
					personaje.aterrizarEn(plataformas[i].bordeSuperior());
					estaSobreAlguna = true;
				}

				if (personaje.chocaCabezaConPlataforma(plataformas[i])) {
					personaje.golpearCabeza(plataformas[i].bordeInferior());
				}

				if (personaje.chocaCostadoConPlataforma(plataformas[i])) {
					personaje.corregirChoqueCostado(plataformas[i]);
				}
			}
		}

		if (!estaSobreAlguna) {
			personaje.empezarACaer();
		}
	}

	public void revisarDisparosConPlataformas(Personaje personaje) {
		for (int i = 0; i < plataformas.length; i++) {
			if (plataformas[i] != null) {

				if (personaje.getDisparo() != null &&
						personaje.getDisparo().colisionaConPlataforma(plataformas[i])) {
					personaje.setDisparo(null);
				}

				Proyectil[] triples = personaje.getDisparosTriples();

				for (int j = 0; j < triples.length; j++) {
					if (triples[j] != null && triples[j].colisionaConPlataforma(plataformas[i])) {
						triples[j] = null;
					}
				}
			}
		}
	}

	public void dibujar(Entorno entorno, double camaraX) {
		for (int i = 0; i < plataformas.length; i++) {
			if (plataformas[i] != null) {
				plataformas[i].dibujar(entorno, camaraX);
			}
		}
	}

	public Plataforma[] getPlataformas() {
		return plataformas;
	}
}