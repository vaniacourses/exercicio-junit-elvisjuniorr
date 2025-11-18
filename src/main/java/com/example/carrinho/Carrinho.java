package main.java.com.example.carrinho;

import java.util.ArrayList;
import java.util.Iterator;

import main.java.com.example.produto.Produto;
import main.java.com.example.produto.ProdutoNaoEncontradoException;

public class Carrinho {
	
	private ArrayList items;
	
	public Carrinho() {
		items = new ArrayList();
	}
	
	public double getValorTotal() {
		double valorTotal = 0.0;
		
		for (Iterator i = items.iterator(); i.hasNext();) {
			Produto item = (Produto) i.next();
			valorTotal += item.getPreco();
		}
		
		return valorTotal;
	}
	
	public void addItem(Produto item) {
		items.add(item);
	}
	
	public void removeItem(Produto item) throws ProdutoNaoEncontradoException {
		if (!items.remove(item)) {
			throw new ProdutoNaoEncontradoException();
		}
	}
	
	public int getQtdeItems() {
		return items.size();
	}
	
	public void esvazia() {
		items.clear();
	}
	

}
