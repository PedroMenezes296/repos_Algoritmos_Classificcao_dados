package entities;

public class Pedido {

	private Item item;
	private Cliente cliente;
	private TipoPagamento tipoPagamento;
	
	public Pedido(Item item, Cliente cliente, TipoPagamento tipoPagamento) {
		super();
		this.item = item;
		this.cliente = cliente;
		this.tipoPagamento = tipoPagamento;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(TipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}
	
	public String toString() {
		return "Pedido: \n"
				+ this.cliente + "\n" + this.item + "\n" + "Forma de pagamento: " + this.tipoPagamento;
	}
	
	
}
