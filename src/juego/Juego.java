package juego;

import java.awt.Color;
import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {

	private Entorno entorno;
	// Personaje que controlará el jugador.
	private Personaje personaje;
	
	// Inicio arreglo de enemigos e items
	private Enemigos[] e;
	private int minEnemigos = 8;
	private Item[] item;
	private int proximoItem = 0;

	// Plataformas de prueba para probar movimiento y salto del personaje.
	private Plataforma plataformaIzquierda;
	private Plataforma plataformaDerecha;

	// Se muestra en pantalla que perdiste.
	// Si vale true se deja de ejecutar el juego.
	private boolean juegoTerminado;

	Juego() {
		// Ventana de juego de 800x600.
		this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);

		this.personaje = new Personaje(400, 300, 30, 50);
		e = new Enemigos[15];
		item = new Item[4];

		// Plataforma de prueba.
		// Agujero a la derecha, entre x=500 y x=620.
		
		// Deje un hueco en la plataforma para probar el vacio y su caida.
		this.plataformaIzquierda = new Plataforma(250, 580, 500, 40);
		this.plataformaDerecha = new Plataforma(710, 580, 180, 40);

		// El jugador todavía no pierde.
		// False = juego sigue, true = juego termino.
		this.juegoTerminado = false;

		// Se empieza a ejecutar el juego.
		this.entorno.iniciar();
	}

	public void tick() {
		// Cambie el color a Cyan del fondo.
		entorno.colorFondo(Color.CYAN);

		// Si el jugador pierde todas las vidas se muestra:
		if (juegoTerminado) {
			mostrarPantallaPerdiste();
			return;
		}

		// Teclado y Mouse.
		personaje.controlarJugador(entorno);
		// Movimiento, gravedad y proyectiles.
		personaje.actualizar();
		// Elimina disparos que salieron de la pantalla.
		personaje.eliminarDisparoSiSalioDePantalla(entorno);

		// Verifica las colisiones con las mismas.
		revisarPlataformas();

		// Si el personaje cae fuera del mapa pierde una vida.
		if (personaje.cayoAlVacio(entorno)) {
			personaje.perderVida();

			// Si ya no quedan vidas al jugador, juego terminado.
			if (personaje.getVidas() <= 0) {
				juegoTerminado = true;
			} else {
				// Si quedan vidas todavía aparece de nuevo en el centro.
				personaje.reiniciarPosicion(400, 300);
			}
		}

		// Dibujado de plataformas.
		plataformaIzquierda.dibujar(entorno);
		plataformaDerecha.dibujar(entorno);

		// Dibujado del personaje.
		personaje.dibujar(entorno);

		// Dibuja las vidas que restan todavía
		dibujarVidas();
		
		// Creacion enemigos
		int contadorVivos = 0;
		for (int i = 0; i < e.length; i++) {
			if (e[i] != null) {
				contadorVivos++;
			}
		}

		if (contadorVivos < minEnemigos) {
			for (int i = 0; i < e.length; i++) {
				if (e[i] == null) {
					e[i] = new Enemigos(entorno.ancho(), entorno.alto()); 
					break; // Sino se llena de enemigos
				}
			}
		}

		// Mover, dibujar
		for (int i = 0; i < e.length; i++) {
			if (e[i] != null) {
				e[i].mover();
				e[i].dibujar(entorno);
				
				// Colision con disparo y disparo triple
				if(personaje.getDisparo() != null && personaje.getDisparo().colisionaConEnemigo(e[i])) {
					if (Math.random() < 0.35) {
						item[proximoItem] = new Item(e[i].getX(), e[i].getY());
						proximoItem = (proximoItem + 1) % 4;
					}
					e[i] = null;
					personaje.setDisparo(null);
				}
				/*
				if(personaje.getDisparosTriples() != null && personaje.getDisparosTriples().colisionaConEnemigo(e[i])) {
					if (Math.random() < 0.35) {
						item[proximoItem] = new Item(e[i].getX(), e[i].getY());
						proximoItem = (proximoItem + 1) % 4;
					}
					e[i] = null;
					personaje.setDisparosTriples(null);
				}*/	
						
				// Colision con plataforma
				if (plataformaIzquierda != null && e[i] != null) {
					if (e[i].colisionaPorIzquierda(plataformaIzquierda) || e[i].colisionaPorDerecha(plataformaIzquierda)){
						e[i] = null;
					}	
				}
				
				if (plataformaDerecha != null && e[i] != null) {
					if (e[i].colisionaPorIzquierda(plataformaDerecha) || e[i].colisionaPorDerecha(plataformaDerecha)){
						e[i] = null;
					}	
				}
						
				// Colision con princesa
				if (e[i] != null) {
					if (e[i].colisionaPorIzquierda(personaje) || e[i].colisionaPorDerecha(personaje) || e[i].colisionaPorArriba(personaje) || e[i].colisionaPorAbajo(personaje)){
						e[i] = null;
						personaje.perderVida(); // la princesa pierde una vida
					}
				}
									
				// Si sale de la pantalla se vuelve null
				if (e[i] != null) {
					if (e[i].seFueDePantalla(entorno)) {
						e[i] = null;
					}	
				}
			}	
		}	
						
		// Dropeo de item 
				
		for (int j = 0; j < item.length; j++) {
			if (item[j] != null) {
				item[j].dibujar(entorno);
			
				// Verificamos si la princesa lo toca para recolectarlo
				if (item[j].colisionaConPersonaje(personaje)) {
					personaje.sumarVida(); 				
					item[j] = null;
				}	
			}
		}
	}

	private void revisarPlataformas() {
		boolean tocaIzquierda = personaje.estaSobrePlataforma(plataformaIzquierda);
		boolean tocaDerecha = personaje.estaSobrePlataforma(plataformaDerecha);

		// Si esta cayendo y toca la plataforma se lo pone encima de ella.
		if (personaje.estaCayendo() && tocaIzquierda) {
			personaje.aterrizarEn(plataformaIzquierda.bordeSuperior());
		} else if (personaje.estaCayendo() && tocaDerecha) {
			personaje.aterrizarEn(plataformaDerecha.bordeSuperior());
		} else if (!tocaIzquierda && !tocaDerecha) {
			// Si no esta apoyado en ninguna plataforma, cae.
			personaje.empezarACaer();
		}
	}

	private void dibujarVidas() {
		// Muestra las vidas que quedan todavía.
		entorno.cambiarFont("Arial", 20, Color.BLACK);
		entorno.escribirTexto("Vidas: " + personaje.getVidas(), 20, 30);

		// Dibuja un cuadrado de color rojo por cada vida (8).
		for (int i = 0; i < personaje.getVidas(); i++) {
			entorno.dibujarRectangulo(30 + i * 22, 50, 16, 16, 0, Color.RED);
		}
	}

	private void mostrarPantallaPerdiste() {
		// Mensaje mostrando cuando ya no tenemos más vidas.
		entorno.cambiarFont("Arial", 34, Color.BLACK);
		entorno.escribirTexto("Juego terminado: Perdiste!", 190, 280);

		entorno.cambiarFont("Arial", 18, Color.BLACK);
		entorno.escribirTexto("Cerrá el juego y volvé a abrirlo para jugar de nuevo.", 170, 320);
	}

	public static void main(String[] args) {
		new Juego();
	}
}