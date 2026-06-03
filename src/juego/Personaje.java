package juego;

import java.awt.Color;
import entorno.Entorno;

public class Personaje {

	private double x;
	private double y;
	private double yAnterior;
	private double ancho;
	private double alto;
	
	private double ultimoXSeguro;
	private double ultimoYSeguro;

	private double velocidadY;
	private boolean estaEnElAire;

	private int vidas;
	private int disparosNormalesRealizados;
	private int disparosPlus;

	private Proyectil disparo;
	private Proyectil disparoPlus;
	private Proyectil[] disparosTriples;

	private final double VELOCIDAD_MOVIMIENTO = 5;
	private final double FUERZA_SALTO = -12;
	private final double GRAVEDAD = 0.5;
	
	private double xAnterior;

	public Personaje(double x, double y, double ancho, double alto) {
		this.x = x;
		this.y = y;
		this.xAnterior = x;
		this.yAnterior = y;
		
		this.ancho = ancho;
		this.alto = alto;

		this.ultimoXSeguro = x;
		this.ultimoYSeguro = y;

		this.velocidadY = 0;
		this.estaEnElAire = true;

		this.vidas = 8;
		this.disparosNormalesRealizados = 0;
		this.disparosPlus = 0;

		this.disparo = null;
		this.disparoPlus = null;
		this.disparosTriples = new Proyectil[3];
		
	}

	public void controlarJugador(Entorno entorno, double camaraX, double largoMapa) {
		
		xAnterior = x;
		
		if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA) && bordeIzquierdo() > camaraX) {
			moverIzquierda();
		}

		if (entorno.estaPresionada(entorno.TECLA_DERECHA) && bordeDerecho() < largoMapa) {
			moverDerecha();
		}

		if (entorno.sePresiono(entorno.TECLA_ARRIBA)) {
			saltar();
		}

		if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO) && disparo == null) {
			disparar(entorno.mouseX() + camaraX, entorno.mouseY());
		}

		if (entorno.sePresionoBoton(entorno.BOTON_DERECHO)
				&& disparosPlus > 0
				&& disparoPlus == null) {

			disparoPlus = new Proyectil(x, y, entorno.mouseX() + camaraX, entorno.mouseY(), 28);
			disparoPlus.hacerAtravesador();
			disparoPlus.hacerMasLento();
			disparosPlus = disparosPlus - 1;
		}

		else if (entorno.sePresionoBoton(entorno.BOTON_DERECHO)
				&& disparosNormalesRealizados >= 10
				&& noHayDisparoTriple()) {

			dispararTriple(entorno.mouseX() + camaraX, entorno.mouseY());
			disparosNormalesRealizados = 0;
		}
	}

	public void actualizar() {
		aplicarGravedad();

		if (disparo != null) {
			disparo.mover();
		}

		if (disparoPlus != null) {
			disparoPlus.mover();
		}

		for (int i = 0; i < disparosTriples.length; i++) {
			if (disparosTriples[i] != null) {
				disparosTriples[i].mover();
			}
		}
	}

	public void dibujar(Entorno entorno, double camaraX) {
		entorno.dibujarRectangulo(x - camaraX, y, ancho, alto, 0, Color.PINK);

		if (disparo != null) {
			disparo.dibujar(entorno, camaraX);
		}

		if (disparoPlus != null) {
			disparoPlus.dibujar(entorno, camaraX);
		}

		for (int i = 0; i < disparosTriples.length; i++) {
			if (disparosTriples[i] != null) {
				disparosTriples[i].dibujar(entorno, camaraX);
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
		yAnterior = y;

		if (estaEnElAire) {
			velocidadY += GRAVEDAD;
			y += velocidadY;
		}
	}

	public void aterrizarEn(double yPiso) {
		y = yPiso - alto / 2;
		velocidadY = 0;
		estaEnElAire = false;

		ultimoXSeguro = x;
		ultimoYSeguro = y;
	}

	public void golpearCabeza(double bordeInferiorPlataforma) {
		y = bordeInferiorPlataforma + alto / 2;
		velocidadY = 0;
	}

	public void empezarACaer() {
		if (!estaEnElAire) {
			estaEnElAire = true;
			velocidadY = 0;
		}
	}

	public boolean estaSobrePlataforma(Plataforma plataforma) {
		boolean cruzaLaParteSuperior =
				yAnterior + alto / 2 <= plataforma.bordeSuperior()
				&& bordeInferior() >= plataforma.bordeSuperior();

		boolean estaDentroDelAncho =
				bordeDerecho() > plataforma.bordeIzquierdo()
				&& bordeIzquierdo() < plataforma.bordeDerecho();

		return velocidadY >= 0 && cruzaLaParteSuperior && estaDentroDelAncho;
	}

	public boolean chocaCabezaConPlataforma(Plataforma plataforma) {
		boolean cruzaLaParteInferior =
				yAnterior - alto / 2 >= plataforma.bordeInferior()
				&& bordeSuperior() <= plataforma.bordeInferior();

		boolean estaDentroDelAncho =
				bordeDerecho() > plataforma.bordeIzquierdo()
				&& bordeIzquierdo() < plataforma.bordeDerecho();

		return velocidadY < 0 && cruzaLaParteInferior && estaDentroDelAncho;
	}
	
	public boolean chocaCostadoConPlataforma(Plataforma plataforma) {

		boolean seSuperponeEnX =
				bordeDerecho() > plataforma.bordeIzquierdo()
				&& bordeIzquierdo() < plataforma.bordeDerecho();

		boolean seSuperponeEnY =
				bordeInferior() > plataforma.bordeSuperior()
				&& bordeSuperior() < plataforma.bordeInferior();

		boolean veniaDeCostado =
				xAnterior + ancho / 2 <= plataforma.bordeIzquierdo()
				|| xAnterior - ancho / 2 >= plataforma.bordeDerecho();

		return seSuperponeEnX && seSuperponeEnY && veniaDeCostado;
	}

	public void corregirChoqueCostado(Plataforma plataforma) {

		if (xAnterior < plataforma.getX()) {
			x = plataforma.bordeIzquierdo() - ancho / 2;
		} else {
			x = plataforma.bordeDerecho() + ancho / 2;
		}
	}

	public boolean cayoAlVacio(Entorno entorno) {
		return y > entorno.alto() + 100;
	}

	public void perderVida() {
		if (vidas > 0) {
			vidas = vidas - 1;
		}
	}

	public void sumarVida() {
		if (vidas < 8) {
			vidas = vidas + 1;
		}
	}

	public void sumarDisparoPlus() {
		disparosPlus++;
	}

	public void reiniciarEnLugarSeguro() {
		x = ultimoXSeguro;
		y = ultimoYSeguro;
		yAnterior = ultimoYSeguro;

		velocidadY = 0;
		estaEnElAire = true;

		disparo = null;
		disparoPlus = null;

		for (int i = 0; i < disparosTriples.length; i++) {
			disparosTriples[i] = null;
		}
	}

	public void disparar(double mouseX, double mouseY) {
		disparo = new Proyectil(x, y, mouseX, mouseY, 7);
		disparosNormalesRealizados = disparosNormalesRealizados + 1;
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

	public void eliminarDisparoSiSalioDePantalla(Entorno entorno, double camaraX) {
		if (disparo != null && disparo.seFueDePantalla(entorno, camaraX)) {
			disparo = null;
		}

		if (disparoPlus != null && disparoPlus.seFueDePantalla(entorno, camaraX)) {
			disparoPlus = null;
		}

		for (int i = 0; i < disparosTriples.length; i++) {
			if (disparosTriples[i] != null && disparosTriples[i].seFueDePantalla(entorno, camaraX)) {
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

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getAncho() {
		return ancho;
	}

	public double getAlto() {
		return alto;
	}

	public int getVidas() {
		return vidas;
	}

	public int getDisparosNormalesRealizados() {
		return disparosNormalesRealizados;
	}

	public int getDisparosParaTriple() {
		if (disparosNormalesRealizados > 10) {
			return 10;
		}
		return disparosNormalesRealizados;
	}

	public int getDisparosPlus() {
		return disparosPlus;
	}

	public Proyectil getDisparo() {
		return disparo;
	}

	public Proyectil getDisparoPlus() {
		return disparoPlus;
	}

	public Proyectil[] getDisparosTriples() {
		return disparosTriples;
	}

	public void setDisparo(Proyectil disparo) {
		this.disparo = disparo;
	}

	public void setDisparoPlus(Proyectil disparoPlus) {
		this.disparoPlus = disparoPlus;
	}
}