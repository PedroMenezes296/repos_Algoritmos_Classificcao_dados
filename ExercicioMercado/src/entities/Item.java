package entities;

import exception.EstoqueInsuficienteException;

public class Item {

	private int quantidade;
	private Produto produto;
	
	public Item(int quantidade, Produto produto) {
		super();
		this.quantidade = quantidade;
		this.produto = produto;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	public double precoPedido() {
		return this.quantidade * this.produto.getPrice();
	}
	
	public int calculaQuantidade() {
		if(this.quantidade <= this.produto.getQuantidadeEstoque()) {
			this.produto.setQuantidadeEstoque(this.produto.getQuantidadeEstoque() - quantidade);
		}else {
			 throw new EstoqueInsuficienteException(
			            "Estoque insuficiente para o produto " + produto.getDescricao()
			            + ". Disponível: " + produto.getQuantidadeEstoque()
			            + ", Solicitado: " + this.quantidade
			        );
		}
		return this.produto.getQuantidadeEstoque();
	}
	
	public String toString() {
		return "Produto Pedido: " + this.produto.getDescricao() + 
				"\nQuantidade em estoque: " + this.produto.getQuantidadeEstoque() +
				"\nQuantidade pedida: " + this.quantidade +
				"\nPreço final: " + this.precoPedido() +
				"\nQuantidade em estoque atualizada: " + this.calculaQuantidade();
	}
}
