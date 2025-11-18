package test.java.com.example.carrinho;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.java.com.example.carrinho.Carrinho;
import main.java.com.example.produto.Produto;
import main.java.com.example.produto.ProdutoNaoEncontradoException;

@DisplayName("Classe para teste do carrinho")
public class CarrinhoTest {

    private Carrinho carrinho;
    private Produto p1;
    private Produto p2;

    @BeforeEach
    public void inicializa() {
        carrinho = new Carrinho();
        p1 = new Produto("Produto1", 10.00);
        p2 = new Produto("Produto2", 20.00);
    }

    @Test
    @DisplayName("Testa adicionar itens no carrinho")
    public void testAddItem() {
        carrinho.addItem(p1);
        assertEquals(1, carrinho.getQtdeItems());

        carrinho.addItem(p2);
        assertEquals(2, carrinho.getQtdeItems());
    }

    @Test
    @DisplayName("Testa remover item existente")
    public void testRemoveItem() throws ProdutoNaoEncontradoException {
        carrinho.addItem(p1);
        carrinho.addItem(p2);

        carrinho.removeItem(p1);
        assertEquals(1, carrinho.getQtdeItems());

        carrinho.removeItem(p2);
        assertEquals(0, carrinho.getQtdeItems());
    }

    @Test
    @DisplayName("Testa remover item inexistente")
    public void testRemoveItemInexistente() {
        assertThrows(ProdutoNaoEncontradoException.class, () -> {
            carrinho.removeItem(p1);
        });
    }

    @Test
    @DisplayName("Testa valor total do carrinho")
    public void testGetValorTotal() {
        carrinho.addItem(p1);
        carrinho.addItem(p2);

        assertEquals(30.00, carrinho.getValorTotal());
    }

    @Test
    @DisplayName("Testa quantidade de itens")
    public void testGetQtdeItems() {
        assertEquals(0, carrinho.getQtdeItems());

        carrinho.addItem(p1);
        assertEquals(1, carrinho.getQtdeItems());

        carrinho.addItem(p2);
        assertEquals(2, carrinho.getQtdeItems());
    }

    @Test
    @DisplayName("Testa esvaziar carrinho")
    public void testEsvazia() {
        carrinho.addItem(p1);
        carrinho.addItem(p2);
        carrinho.esvazia();
        assertEquals(0, carrinho.getQtdeItems());
        assertEquals(0.00, carrinho.getValorTotal());
    }
}
