package test.java.com.example.calculadora;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.java.com.example.calculadora.Calculadora;

@DisplayName("Classe para teste da calculadora")
public class CalculadoraTest {
	
	private Calculadora calc;
	
	@BeforeEach
	public void inicializa() {
		calc = new Calculadora();
	}
	
	@DisplayName("Testa a soma de dois números")
	@Test
	public void testSomaDoisNumeros() {
		int soma = calc.soma(4, 5);		
		Assertions.assertEquals(9, soma);		
	}

	@Test
    @DisplayName("Testa a subtração de dois números")
    public void testSubtracao() {
        assertEquals(3, calc.subtracao(8, 5));

        assertEquals(-2, calc.subtracao(3, 5));
    }

	@Test
    @DisplayName("Testa a multiplicação de dois números")
    public void testMultiplicacao() {
        assertEquals(20, calc.multiplicacao(4, 5));

        assertEquals(0, calc.multiplicacao(0, 5));

        assertEquals(-12, calc.multiplicacao(-3, 4));
    }

	
	@Test
	public void testDivisaoDoisNumeros() {
		int divisao = calc.divisao(8, 4);
		assertTrue(divisao == 2);
	}
	
	@Test
	public void testDivisaoPorZero() {
		try {
			int divisao = calc.divisao(8, 0);
			fail("Exceção não lançada");
		}catch (ArithmeticException e) {
			assertEquals("/ by zero", e.getMessage());
		}		
	}
	
	@Test
	public void testDivisaoPorZeroComAssertThrows() {
		assertThrows(ArithmeticException.class,
				() -> calc.divisao(8, 0));
	}

	@Test
    @DisplayName("Testa o somatório de 0 até n")
    public void testSomatoria() {
        assertEquals(6, calc.somatoria(3)); 

        assertEquals(0, calc.somatoria(0));
    }

	@Test
    @DisplayName("Testa se um número é positivo")
    public void testEhPositivo() {
        assertTrue(calc.ehPositivo(5));

        assertFalse(calc.ehPositivo(-3));
    }

	@Test
    @DisplayName("Testa o método de comparação de números")
    public void testCompara() {
        assertEquals(0, calc.compara(5, 5));

        assertEquals(1, calc.compara(10, 5));

        assertEquals(-1, calc.compara(3, 7));
    }

}
