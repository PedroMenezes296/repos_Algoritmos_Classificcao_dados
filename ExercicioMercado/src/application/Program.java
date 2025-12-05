package application;

import java.util.Locale;
import java.util.Scanner;

import entities.Cliente;
import entities.Descricao;
import entities.Item;
import entities.Pedido;
import entities.Produto;
import entities.TipoPagamento;

public class Program {

	public static void main(String[] args) {
		
		
		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.print("\nInforme o seu nome: ");
			String name = sc.nextLine();
			System.out.print("Informe o seu CPF: ");
			String cpf = sc.nextLine();
			Cliente cliente = new Cliente(name, cpf);
			
	        for (Descricao d : Descricao.values()) {
	            System.out.println(d);
	        }
			System.out.print("Informe qual produto voce quer: ");
			String desc = sc.nextLine().toLowerCase();
			System.out.print("Quantos itens desse produto voce quer: ");
			int quantidade =sc.nextInt();
			Item item = new Item(quantidade, definirProdutoEscolhido(desc));
			sc.nextLine(); 
			
			for(TipoPagamento x : TipoPagamento.values()) {
	            System.out.println(x);
			}
			System.out.print("Informe qual o tipo de pagamento: ");				
			String resposta = sc.nextLine().toUpperCase();
			TipoPagamento tipoPagamento = TipoPagamento.valueOf(resposta);
			
			
			Pedido pedido = new Pedido(item, cliente, tipoPagamento);
			System.out.println(pedido);
		}
		
	}

	public static Produto definirProdutoEscolhido(String resposta) {
		Produto arroz = new Produto(Descricao.ARROZ, 16.00 ,20);
		Produto feijao = new Produto(Descricao.FEIJAO, 4.00 ,20);
		Produto farinha = new Produto(Descricao.FARINHA, 12.00 ,20);
		Produto leite = new Produto(Descricao.LEITE, 5.00 ,20);
		
		if(resposta.equals("arroz")) {
			return arroz;
		} else if(resposta.equals("feijao")){
			return feijao;
		} else if(resposta.equals("farinha")){
			return farinha;
		} else if(resposta.equals("leite")){
			return leite;
		} else {
			return null;
		}
	}

}
