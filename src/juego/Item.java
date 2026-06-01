package juego;

import java.awt.Color;

import entorno.Entorno;

public class Item {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	
	public Item(int x, int y) {
		this.x = x; 
		this.y = y;
		this.ancho = 15;
		this.alto = 15;
	}
	
	public void dibujar(Entorno e) {
		e.dibujarRectangulo(x, y, ancho, alto, 0, Color.GREEN);
	}
	
	public boolean colisionaConPersonaje(Personaje p) {
		// Chequeamos si el personaje choca con el item
		boolean colisionX = bordeDerecho() > p.bordeIzquierdo() && bordeIzquierdo() < p.bordeDerecho();
		boolean colisionY = bordeInferior() > p.bordeSuperior() && bordeSuperior() < p.bordeInferior();
		
		if (colisionX && colisionY) {
			return true;
		} else {
			return false;
		}
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
	
}