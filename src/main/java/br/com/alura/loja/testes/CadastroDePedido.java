package br.com.alura.loja.testes;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.alura.loja.dao.CategoriaDao;
import br.com.alura.loja.dao.ClienteDao;
import br.com.alura.loja.dao.PedidoDao;
import br.com.alura.loja.dao.ProdutoDao;
import br.com.alura.loja.model.Categoria;
import br.com.alura.loja.model.Cliente;
import br.com.alura.loja.model.ItemPedido;
import br.com.alura.loja.model.Pedido;
import br.com.alura.loja.model.Produto;
import br.com.alura.loja.util.JPAUtil;
import br.com.alura.loja.vo.RelatorioDeVendasVo;

public class CadastroDePedido {

	public static void main(String[] args) {
		//cadastra o Produto e sua categoria
		popularBancoDeDados();
		
		//Carrega o Produto
		EntityManager em = JPAUtil.getEntityManager();
		ProdutoDao produtoDao = new ProdutoDao(em);
		ClienteDao clienteDao = new ClienteDao(em);
		
		Produto produto = produtoDao.buscarPorId(1l);
		Produto produto2 = produtoDao.buscarPorId(2l);
		Produto produto3 = produtoDao.buscarPorId(3l);
		
		Cliente cliente = clienteDao.buscarPorId(1l);
		
		em.getTransaction().begin();
		
		//Cria o Pedido
		Pedido pedido = new Pedido(cliente);
		pedido.adicionarItem(new ItemPedido(10, pedido, produto));
		pedido.adicionarItem(new ItemPedido(40, pedido, produto2));
		
		Pedido pedido2 = new Pedido(cliente);
		pedido.adicionarItem(new ItemPedido(2, pedido, produto3));
		
		PedidoDao pedidoDao = new PedidoDao(em);
		pedidoDao.cadastrar(pedido);
		pedidoDao.cadastrar(pedido2);
		
		em.getTransaction().commit();
		
		BigDecimal totalVendido = pedidoDao.valorTotalVendido();
		System.out.println("VALOR TOTAL: " +totalVendido);
		
		List<RelatorioDeVendasVo> relatorio = pedidoDao.relatorioDeVendas();
		relatorio.forEach(System.out::println);
	}

	private static void popularBancoDeDados() {
		Categoria celulares = new Categoria("CELULARES");
		Categoria videogames = new Categoria("VIDEOGAMES");
		Categoria informatica = new Categoria("INFORMATICA");
		
		Produto celular = new Produto("Xiome Redmi", "Muito Legal", new BigDecimal("800"), celulares);
		Produto videogame = new Produto("PS5", "Playstation 5", new BigDecimal("5000"), videogames);
		Produto macbook = new Produto("Macbook", "Mac pro reti", new BigDecimal("10000"), informatica);
		
		//Cria o cliente
		Cliente cliente = new Cliente("Rodrigo", "12345678911");
		
		EntityManager em = JPAUtil.getEntityManager();
		ProdutoDao produtoDao = new ProdutoDao(em);
		CategoriaDao categoriaDao = new CategoriaDao(em);
		ClienteDao clienteDao = new ClienteDao(em);
		
		em.getTransaction().begin();
		
		categoriaDao.cadastrar(celulares);
		categoriaDao.cadastrar(videogames);
		categoriaDao.cadastrar(informatica);
		
		produtoDao.cadastrar(celular);
		produtoDao.cadastrar(videogame);
		produtoDao.cadastrar(macbook);
		
		clienteDao.cadastrar(cliente);
		
		em.getTransaction().commit();
		em.close();
	}
}