package entities;

public class Produto {
	private Descricao descricao;
	private double price;
	private int quantidadeEstoque;
	
	
	public Produto(Descricao descricao, double price, int quantidadeEstoque) {
		super();
		this.descricao = descricao;
		this.price = price;
		this.quantidadeEstoque = quantidadeEstoque;
	}


	public Descricao getDescricao() {
		return descricao;
	}



	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public int getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(int quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}


	public String toString() {
		return this.descricao + "\n" + this.price + "\n" + this.quantidadeEstoque;
	}
	
}
