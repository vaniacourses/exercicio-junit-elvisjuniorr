package test.java.com.example.carrinho;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import main.java.com.example.carrinho.Carrinho;
import main.java.com.example.produto.Produto;
import main.java.com.example.produto.ProdutoNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class CarrinhoTest {

    private Carrinho carrinho;

    @BeforeEach
    void setUp() {
        carrinho = new Carrinho();
    }

    // Helper factory to avoid repetition (AAA - Arrange)
    private Produto produto(double preco, String id) {
        return new main.java.com.example.produto.Produto(id, preco);
    }

    @Nested
    @DisplayName("Adicionar / consultar quantidade")
    class AddAndCountTests {

        @Test
        @DisplayName("quando criar carrinho vazio -> qtde deve ser 0")
        void shouldStartWithZeroItems() {
            // Arrange done in setUp
            // Act
            int qtde = carrinho.getQtdeItems();
            // Assert
            assertEquals(0, qtde, "Carrinho recém-criado deve iniciar com zero items");
        }

        @Test
        @DisplayName("ao adicionar item -> quantidade incrementa para 1")
        void addItemShouldIncreaseCount() {
            // Arrange
            Produto p = produto(10.0, "A1");
            // Act
            carrinho.addItem(p);
            // Assert
            assertEquals(1, carrinho.getQtdeItems(), "Após adicionar 1 item, qtde deve ser 1");
        }

        @Test
        @DisplayName("ao adicionar múltiplos itens -> quantidade reflete número de inserções")
        void addMultipleItemsShouldReflectCount() {
            // Arrange
            carrinho.addItem(produto(1.0, "p1"));
            carrinho.addItem(produto(2.0, "p2"));
            carrinho.addItem(produto(3.0, "p3"));
            // Act & Assert
            assertEquals(3, carrinho.getQtdeItems(), "Quantidade deve refletir o número de itens adicionados");
        }
    }

    @Nested
    @DisplayName("Calculo do valor total")
    class TotalValueTests {

        @Test
        @DisplayName("carrinho vazio -> valor total = 0.0")
        void emptyCartTotalIsZero() {
            // Act
            double total = carrinho.getValorTotal();
            // Assert
            assertEquals(0.0, total, 0.000001, "Carrinho vazio deve ter valor total 0.0");
        }

        @Test
        @DisplayName("soma de preços positivos -> retorna soma exata (happy path)")
        void sumOfPositivePrices() {
            // Arrange
            carrinho.addItem(produto(10.50, "p1"));
            carrinho.addItem(produto(5.25, "p2"));
            // Act
            double total = carrinho.getValorTotal();
            // Assert (tolerância pequena para double)
            assertEquals(15.75, total, 0.000001, "Soma dos preços deve ser 15.75");
        }

        @Test
        @DisplayName("preço zero e preço positivo -> comportamento com boundary value (0.0)")
        void includesZeroPriceItem() {
            // Arrange
            carrinho.addItem(produto(0.0, "free"));
            carrinho.addItem(produto(2.0, "paid"));
            // Act
            double total = carrinho.getValorTotal();
            // Assert
            assertEquals(2.0, total, 0.000001, "Item com preço zero não altera o total além dos outros itens");
        }

        @Test
        @DisplayName("preços grandes -> não perder precisão relevante (boundary)")
        void largePricesSummation() {
            // Arrange
            double large = 1e12; // 1 trillion
            carrinho.addItem(produto(large, "big1"));
            carrinho.addItem(produto(1.0, "small"));
            // Act
            double total = carrinho.getValorTotal();
            // Assert
            assertEquals(large + 1.0, total, 1e-6, "Soma com valores grandes deve ser precisa o bastante");
        }
    }

    @Nested
    @DisplayName("Remoção de itens")
    class RemoveItemTests {

        @Test
        @DisplayName("remover item existente retorna sem exceção e decrementa quantidade (mesma instância)")
        void removeExistingItemBySameInstance() throws ProdutoNaoEncontradoException {
            // Arrange
            Produto p = produto(4.0, "x1");
            carrinho.addItem(p);
            // Precondition
            assertEquals(1, carrinho.getQtdeItems());
            // Act
            carrinho.removeItem(p);
            // Assert
            assertEquals(0, carrinho.getQtdeItems(), "Após remoção, quantidade deve decrementar");
        }

        @Test
        @DisplayName("remover item que é equal mas instância diferente -> deve remover (usa equals)")
        void removeEqualButDifferentInstance() throws ProdutoNaoEncontradoException {
            // Arrange
            Produto original = produto(7.0, "sameId");
            carrinho.addItem(original);

            // cria outro produto com mesmo id e preço diferente (ProdutoStub implementa equals por id)
            Produto equalDifferentInstance = produto(7.0, "sameId");
            // Act
            carrinho.removeItem(equalDifferentInstance);
            // Assert - item deve ser removido mesmo sendo outra instância se equals estiver definido por id
            assertEquals(0, carrinho.getQtdeItems(), "Remoção por objeto 'igual' deve funcionar");
        }

        @Test
        @DisplayName("remover item ausente -> lança ProdutoNaoEncontradoException")
        void removeNonExistentThrows() {
            // Arrange
            Produto inCart = produto(3.0, "inCart");
            Produto notInCart = produto(3.0, "notInCart");
            carrinho.addItem(inCart);
            // Act & Assert
            assertThrows(ProdutoNaoEncontradoException.class, () -> carrinho.removeItem(notInCart),
                    "Remover item não presente deve lançar ProdutoNaoEncontradoException");
        }
    }

    @Nested
    @DisplayName("Esvaziar carrinho")
    class ClearCartTests {

        @Test
        @DisplayName("esvazia() com items -> deixa carrinho com 0 itens e total 0.0")
        void esvaziaClearsAllItems() {
            // Arrange
            carrinho.addItem(produto(1.0, "a"));
            carrinho.addItem(produto(2.0, "b"));
            assertEquals(2, carrinho.getQtdeItems());
            // Act
            carrinho.esvazia();
            // Assert
            assertEquals(0, carrinho.getQtdeItems(), "Após esvazia, qtde deve ser 0");
            assertEquals(0.0, carrinho.getValorTotal(), 0.000001, "Após esvazia, total deve ser 0.0");
        }

        @Test
        @DisplayName("esvazia() em carrinho já vazio -> operação deve ser segura (idempotente)")
        void esvaziaOnEmptyCartIsIdempotent() {
            // Arrange - carrinho já vazio do setUp
            // Act
            carrinho.esvazia();
            // Assert - sem exceção e ainda 0
            assertEquals(0, carrinho.getQtdeItems(), "Esvaziar carrinho vazio não altera estado");
            assertEquals(0.0, carrinho.getValorTotal(), 0.000001, "Esvaziar carrinho vazio não altera total");
        }
    }
}
