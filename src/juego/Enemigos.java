package juego;

import java.awt.Color;

import entorno.Entorno;

public class Enemigos {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private int velocidad;
	private String direccion;


	public Enemigos(int anchoPantalla, int altoPantalla) {
		this.ancho = 30;
		this.alto = 30;
		this.velocidad = 1;
		
		if (Math.random() < 0.5) {
			this.x = 0 - this.ancho / 2;
			this.direccion = "DERECHA"; 
		} else {
			this.x = 800 + this.ancho / 2;
			this.direccion = "IZQUIERDA";         
		}

		this.y = (int) (Math.random() * 300) + 250;
	}
	
	public void dibujar(Entorno e) {
		e.dibujarRectangulo(x, y, ancho, alto, 0, Color.BLUE);
	}
	
	public void mover() {
		if (this.direccion.equals("DERECHA")) {
			this.x = this.x + this.velocidad;
		} else {
			this.x = this.x - this.velocidad;
		}
	}

	public boolean colisionaPorIzquierda(Plataforma pf) {
		if(pf==null) {
			return false;
		}
		if(bordeIzquierdo() <= pf.bordeDerecho() && bordeIzquierdo() >= pf.bordeDerecho()-5) {
			if(bordeInferior() > pf.bordeSuperior() && bordeSuperior() < pf.bordeInferior()) {
				return true;				
			}
		}
		return false;
	}

	
	public boolean colisionaPorDerecha(Plataforma pf) {
		if(pf==null) {
			return false;
		}
		if(bordeDerecho() >= pf.bordeIzquierdo() && bordeDerecho() < pf.bordeIzquierdo()+5) {
			if(bordeInferior() > pf.bordeSuperior() && bordeSuperior() < pf.bordeInferior()) {
				return true;				
			}
		}
		return false;
	}
	
	public boolean colisionaPorArriba(Plataforma pf) {
		if(pf==null) {
			return false;
		}
		if(bordeSuperior() <= pf.bordeInferior() && bordeSuperior() > pf.bordeInferior()-5) {
			if(bordeDerecho() > pf.bordeIzquierdo() && bordeIzquierdo() < pf.bordeDerecho()) {
				return true;				
			}
		}
		return false;
	}
	
	public boolean colisionaPorAbajo(Plataforma pf) {
		if(pf==null) {
			return false;
		}
		if(bordeInferior() >= pf.bordeSuperior() && bordeInferior() < pf.bordeSuperior()+5) {
			if(bordeDerecho() > pf.bordeIzquierdo() && bordeIzquierdo() < pf.bordeDerecho()) {
				return true;				
			}
		}
		return false;
	}
	
	
	public boolean colisionaPorIzquierda(Personaje p) {
		if(bordeIzquierdo() <= p.bordeDerecho() && bordeIzquierdo() >= p.bordeDerecho()-5) {
			if(bordeInferior() > p.bordeSuperior() && bordeSuperior() < p.bordeInferior()) {
				return true;				
			}
		}
		return false;
	}

	
	public boolean colisionaPorDerecha(Personaje p) {
		if(bordeDerecho()>= p.bordeIzquierdo() && bordeDerecho()< p.bordeIzquierdo()+5) {
			if(bordeInferior()> p.bordeSuperior() && bordeSuperior()< p.bordeInferior()) {
				return true;				
			}
		}
		return false;
	}
	
	public boolean colisionaPorArriba(Personaje p) {
		if(bordeSuperior() <= p.bordeInferior()&& bordeSuperior() > p.bordeInferior()-5) {
			if(bordeDerecho() > p.bordeIzquierdo() && bordeIzquierdo() < p.bordeDerecho()) {
				return true;				
			}
		}
		return false;
	}
	
	public boolean colisionaPorAbajo(Personaje p) {
		if(bordeInferior() >= p.bordeSuperior()&& bordeInferior() < p.bordeSuperior()+5) {
			if(bordeDerecho() > p.bordeIzquierdo() && bordeIzquierdo() < p.bordeDerecho()) {
				return true;				
			}
		}
		return false;
	}
	
	public int bordeDerecho() {
		return this.x + this.ancho / 2;
	}
	public int bordeIzquierdo() {
		return this.x - this.ancho / 2;
	}
	public int bordeInferior() {
		return this.y + this.alto / 2;
	}
	public int bordeSuperior() {
		return this.y - this.alto / 2;
	}

	public boolean seFueDePantalla(Entorno entorno) {
		if (this.direccion.equals("DERECHA") && bordeIzquierdo() > entorno.ancho()) {
			return true;
		}
		if (this.direccion.equals("IZQUIERDA") && bordeDerecho() < 0) {
			return true;
		}
		return false;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}
