package juego;

import java.awt.Color;
import entorno.Entorno;

public class Personaje {

	private double x;
	private double y;
	private double ancho;
	private double alto;

	private double velocidadY;
	private boolean estaEnElAire;

	private int vidas;
	private int disparosNormalesRealizados;

	private Proyectil disparo;
	private Proyectil[] disparosTriples;

	private final double VELOCIDAD_MOVIMIENTO = 5;
	private final double FUERZA_SALTO = -10;
	private final double GRAVEDAD = 0.5;

	public Personaje(double x, double y, double ancho, double alto) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;

		this.velocidadY = 0;
		this.estaEnElAire = true;

		this.vidas = 8;
		this.disparosNormalesRealizados = 0;

		this.disparo = null;
		this.disparosTriples = new Proyectil[3];
	}

	public void controlarJugador(Entorno entorno) {
		if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA) && bordeIzquierdo() > 0) {
			moverIzquierda();
		}

		if (entorno.estaPresionada(entorno.TECLA_DERECHA) && bordeDerecho() < entorno.ancho()) {
			moverDerecha();
		}

		if (entorno.sePresiono(entorno.TECLA_ARRIBA)) {
			saltar();
		}

		// Click izquierdo hace un disparo normal.
		if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO) && disparo == null) {
			disparar(entorno.mouseX(), entorno.mouseY());
		}

		// Click derecho solo funciona después de 10 disparos normales.
		if (entorno.sePresionoBoton(entorno.BOTON_DERECHO)
				&& disparosNormalesRealizados >= 10
				&& noHayDisparoTriple()) {

			dispararTriple(entorno.mouseX(), entorno.mouseY());
			disparosNormalesRealizados = 0;
		}
	}

	public void actualizar() {
		aplicarGravedad();

		if (disparo != null) {
			disparo.mover();
		}

		for (int i = 0; i < disparosTriples.length; i++) {
			if (disparosTriples[i] != null) {
				disparosTriples[i].mover();
			}
		}
	}

	public void dibujar(Entorno entorno) {
		entorno.dibujarRectangulo(x, y, ancho, alto, 0, Color.PINK);

		if (disparo != null) {
			disparo.dibujar(entorno);
		}

		for (int i = 0; i < disparosTriples.length; i++) {
			if (disparosTriples[i] != null) {
				disparosTriples[i].dibujar(entorno);
			}
		}
	}

	public void moverIzquierda() {
		x -= VELOCIDAD_MOVIMIENTO;
	}

	public void moverDerecha() {
		x += VELOCIDAD_MOVIMIENTO;
	}

	public void saltar() {
		if (!estaEnElAire) {
			velocidadY = FUERZA_SALTO;
			estaEnElAire = true;
		}
	}

	public void aplicarGravedad() {
		if (estaEnElAire) {
			velocidadY += GRAVEDAD;
			y += velocidadY;
		}
	}

	public void aterrizarEn(double yPiso) {
		y = yPiso - alto / 2;
		velocidadY = 0;
		estaEnElAire = false;
	}

	public void empezarACaer() {
		if (!estaEnElAire) {
			estaEnElAire = true;
			velocidadY = 0;
		}
	}

	public boolean estaSobrePlataforma(Plataforma plataforma) {
		boolean estaDentroDelAncho =
				bordeDerecho() > plataforma.getX() - plataforma.getAncho() / 2
				&& bordeIzquierdo() < plataforma.getX() + plataforma.getAncho() / 2;

		boolean tocaLaParteDeArriba =
				bordeInferior() >= plataforma.bordeSuperior()
				&& bordeSuperior() < plataforma.bordeSuperior();

		return estaDentroDelAncho && tocaLaParteDeArriba;
	}

	public boolean estaCayendo() {
		return velocidadY >= 0;
	}

	public boolean cayoAlVacio(Entorno entorno) {
		return y > entorno.alto() + 80;
	}

	public void perderVida() {
		if (vidas > 0) {
			vidas--;
		}
	}
	
	public void sumarVida() {
		if (vidas < 8) {
			vidas++;
		}
	}

	public void reiniciarPosicion(double nuevaX, double nuevaY) {
		x = nuevaX;
		y = nuevaY;
		velocidadY = 0;
		estaEnElAire = true;
		disparo = null;

		for (int i = 0; i < disparosTriples.length; i++) {
			disparosTriples[i] = null;
		}
	}

	public void disparar(double mouseX, double mouseY) {
		disparo = new Proyectil(x, y, mouseX, mouseY, 7);
		disparosNormalesRealizados++;
	}

	public void dispararTriple(double mouseX, double mouseY) {
		disparosTriples[0] = new Proyectil(x, y, mouseX, mouseY - 40, 4);
		disparosTriples[1] = new Proyectil(x, y, mouseX, mouseY, 4);
		disparosTriples[2] = new Proyectil(x, y, mouseX, mouseY + 40, 4);
	}

	public boolean noHayDisparoTriple() {
		for (int i = 0; i < disparosTriples.length; i++) {
			if (disparosTriples[i] != null) {
				return false;
			}
		}
		return true;
	}

	public void eliminarDisparoSiSalioDePantalla(Entorno entorno) {
		if (disparo != null && disparo.seFueDePantalla(entorno)) {
			disparo = null;
		}

		for (int i = 0; i < disparosTriples.length; i++) {
			if (disparosTriples[i] != null && disparosTriples[i].seFueDePantalla(entorno)) {
				disparosTriples[i] = null;
			}
		}
	}

	public double bordeDerecho() {
		return x + ancho / 2;
	}

	public double bordeIzquierdo() {
		return x - ancho / 2;
	}

	public double bordeSuperior() {
		return y - alto / 2;
	}

	public double bordeInferior() {
		return y + alto / 2;
	}

	public int getVidas() {
		return vidas;
	}

	public int getDisparosNormalesRealizados() {
		return disparosNormalesRealizados;
	}

	public Proyectil getDisparo() {
		return disparo;
	}
	
	public void setDisparo(Proyectil disparo) {
		this.disparo = disparo;
	}
}