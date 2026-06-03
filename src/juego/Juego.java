package juego;

import java.awt.Color;
import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {

	private Entorno entorno;
	private Personaje personaje;
	private Enemigos[] enemigos;
	private Item[] items;

	private double camaraX;	
	private double largoMapa;
	private double castilloX;
	private double castilloY;
	
	private Mapa mapa;
	private HUD hud;

	private int minimoEnemigos;
	private int proximoItem;

	private boolean juegoTerminado;
	private boolean juegoGanado;

	Juego() {
		this.entorno = new Entorno(this, "Super Elizabeth Sis", 800, 600);

		this.largoMapa = 6800;
		this.camaraX = 0;

		this.castilloX = 6500;
		this.castilloY = 500;

		this.personaje = new Personaje(400, 300, 30, 50);
		
		this.mapa = new Mapa();
		this.hud = new HUD();
		this.enemigos = new Enemigos[15];
		this.items = new Item[6];

		this.minimoEnemigos = 6;
		this.proximoItem = 0;

		this.juegoTerminado = false;
		this.juegoGanado = false;

		this.entorno.iniciar();
	}

	private void reiniciarJuego() {
		this.camaraX = 0;

		this.personaje = new Personaje(400, 300, 30, 50);

		this.mapa = new Mapa();
		this.enemigos = new Enemigos[15];
		this.items = new Item[6];

		this.proximoItem = 0;

		this.juegoTerminado = false;
		this.juegoGanado = false;

	}

	public void tick() {
		entorno.colorFondo(Color.CYAN);

		if (juegoTerminado) {
			hud.mostrarPantallaPerdiste(entorno);

			if (entorno.sePresiono(entorno.TECLA_ENTER)) {
				reiniciarJuego();
			}

			return;
		}

		if (juegoGanado) {
			hud.mostrarPantallaGanaste(entorno);
			return;
		}

		personaje.controlarJugador(entorno, camaraX, largoMapa);
		personaje.actualizar();

		actualizarCamara();

		personaje.eliminarDisparoSiSalioDePantalla(entorno, camaraX);
		
		mapa.revisarPlataformas(personaje);
		mapa.revisarDisparosConPlataformas(personaje);
		revisarCaidaAlVacio();

		crearEnemigosSiFaltan();
		actualizarEnemigos();
		actualizarItems();

		revisarVictoria();

		dibujarTodo();
	}

	private void actualizarCamara() {
		double nuevaCamara = personaje.getX() - 400;

		if (nuevaCamara > camaraX) {
			camaraX = nuevaCamara;
		}

		if (camaraX < 0) {
			camaraX = 0;
		}

		if (camaraX > largoMapa - entorno.ancho()) {
			camaraX = largoMapa - entorno.ancho();
		}
	}

	private void revisarCaidaAlVacio() {
		if (personaje.cayoAlVacio(entorno)) {
			personaje.perderVida();

			if (personaje.getVidas() <= 0) {
				juegoTerminado = true;
			} else {
				personaje.reiniciarEnLugarSeguro();
			}
		}
	}

	private void crearEnemigosSiFaltan() {
		int vivos = 0;

		for (int i = 0; i < enemigos.length; i++) {
			if (enemigos[i] != null) {
				vivos++;
			}
		}

		if (vivos < minimoEnemigos) {
			for (int i = 0; i < enemigos.length; i++) {
				if (enemigos[i] == null) {
					enemigos[i] = new Enemigos(entorno, camaraX);
					break;
				}
			}
		}
	}

	private void actualizarEnemigos() {
		for (int i = 0; i < enemigos.length; i++) {
			if (enemigos[i] != null) {

				enemigos[i].mover();
				evitarQueEnemigoAtravieseIslas(enemigos[i]);

				if (enemigos[i].colisionaConPersonaje(personaje)) {
					enemigos[i] = null;
					personaje.perderVida();

					if (personaje.getVidas() <= 0) {
						juegoTerminado = true;
					}
				}

				if (enemigos[i] != null && personaje.getDisparo() != null
						&& personaje.getDisparo().colisionaConEnemigo(enemigos[i])) {

					crearItem(enemigos[i].getX(), enemigos[i].getY());
					enemigos[i] = null;
					personaje.setDisparo(null);
				}

				if (enemigos[i] != null) {
					revisarDisparoTriple(i);
				}

				if (enemigos[i] != null && personaje.getDisparoPlus() != null
						&& personaje.getDisparoPlus().colisionaConEnemigo(enemigos[i])) {

					crearItem(enemigos[i].getX(), enemigos[i].getY());
					enemigos[i] = null;
				}

				if (enemigos[i] != null && enemigos[i].seFueDePantalla(entorno, camaraX)) {
					enemigos[i] = null;
				}
			}
		}
	}

	private void evitarQueEnemigoAtravieseIslas(Enemigos enemigo) {
		for (int i = 0; i < mapa.getPlataformas().length; i++) {
			if (mapa.getPlataformas()[i] != null
					&& enemigo.colisionaConPlataforma(mapa.getPlataformas()[i])) {

				enemigo.subirUnPoco();
			}
		}
	}

	private void revisarDisparoTriple(int indiceEnemigo) {
		Proyectil[] especiales = personaje.getDisparosTriples();

		for (int i = 0; i < especiales.length; i++) {
			if (especiales[i] != null && enemigos[indiceEnemigo] != null
					&& especiales[i].colisionaConEnemigo(enemigos[indiceEnemigo])) {

				crearItem(enemigos[indiceEnemigo].getX(), enemigos[indiceEnemigo].getY());
				enemigos[indiceEnemigo] = null;
				especiales[i] = null;
				return;
			}
		}
	}

	private void crearItem(double x, double y) {
		if (Math.random() < 0.40) {
			double yItem = buscarPisoParaItem(x, y);

			if (Math.random() < 0.5) {
				items[proximoItem] = new Item(x, yItem, "VIDA");
			} else {
				items[proximoItem] = new Item(x, yItem, "PLUS");
			}

			proximoItem = proximoItem + 1;

			if (proximoItem >= items.length) {
				proximoItem = 0;
			}
		}
	}

	private double buscarPisoParaItem(double x, double y) {
		double mejorY = y;

		for (int i = 0; i < mapa.getPlataformas().length; i++) {
			if (mapa.getPlataformas()[i] != null) {
				boolean estaDentro =
						x > mapa.getPlataformas()[i].bordeIzquierdo()
						&& x < mapa.getPlataformas()[i].bordeDerecho();

				boolean estaDebajo =
						mapa.getPlataformas()[i].bordeSuperior() > y;

				if (estaDentro && estaDebajo) {
					mejorY = mapa.getPlataformas()[i].bordeSuperior() - 8;
					return mejorY;
				}
			}
		}

		return mejorY;
	}

	private void actualizarItems() {
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null && items[i].colisionaConPersonaje(personaje)) {

				if (items[i].getTipo().equals("VIDA")) {
					personaje.sumarVida();
				} else {
					personaje.sumarDisparoPlus();
				}

				items[i] = null;
			}
		}
	}

	private void revisarVictoria() {
		boolean tocaCastillo =
				personaje.bordeDerecho() > castilloX - 50
				&& personaje.bordeIzquierdo() < castilloX + 50
				&& personaje.bordeInferior() > castilloY - 70
				&& personaje.bordeSuperior() < castilloY + 70;

		if (tocaCastillo) {
			juegoGanado = true;
		}
	}

	private void dibujarTodo() {

		mapa.dibujar(entorno, camaraX);

		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				items[i].dibujar(entorno, camaraX);
			}
		}

		for (int i = 0; i < enemigos.length; i++) {
			if (enemigos[i] != null) {
				enemigos[i].dibujar(entorno, camaraX);
			}
		}

		dibujarCastillo();

		personaje.dibujar(entorno, camaraX);

		hud.dibujarVidas(entorno, personaje);
		hud.dibujarContadores(entorno, personaje);
	}

	private void dibujarCastillo() {
		entorno.dibujarRectangulo(castilloX - camaraX, castilloY, 100, 140, 0, Color.RED);
		entorno.dibujarRectangulo(castilloX - camaraX, castilloY - 80, 120, 30, 0, Color.DARK_GRAY);
	}

	public static void main(String[] args) {
		new Juego();
	}
}